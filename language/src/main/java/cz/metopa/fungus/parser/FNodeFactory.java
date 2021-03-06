package cz.metopa.fungus.parser;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.FLanguage;
import cz.metopa.fungus.builtin.FAssertBuiltinNodeGen;
import cz.metopa.fungus.nodes.*;
import cz.metopa.fungus.nodes.controlflow.*;
import cz.metopa.fungus.nodes.expression.*;
import cz.metopa.fungus.nodes.expression.binop.*;
import cz.metopa.fungus.nodes.expression.constants.*;
import cz.metopa.fungus.nodes.expression.unop.FNotNodeGen;
import cz.metopa.fungus.nodes.expression.unop.FUnaryMinusNodeGen;
import cz.metopa.fungus.nodes.expression.unop.FUnaryPlusNodeGen;
import cz.metopa.fungus.runtime.FFunction;
import cz.metopa.fungus.runtime.FNull;
import java.util.*;
import java.util.function.Function;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.StringEscapeUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FNodeFactory {
    private static class LexicalScope {
        private final LexicalScope parent;
        private final Map<String, FrameSlot> slots;
        private final List<FStatementNode> statements;
        private final FrameDescriptor descriptor;
        private final int parameterCount;

        public LexicalScope(LexicalScope parent, FrameDescriptor descriptor, int parameterCount) {
            this.parent = parent;
            this.slots = new HashMap<>();
            this.statements = new ArrayList<>();
            this.descriptor = descriptor;
            this.parameterCount = parameterCount;
        }

        public FrameSlot resolveSlot(String name) throws FException {
            FrameSlot slot = descriptor.findFrameSlot(name);
            if (slot != null) {
                return slot;
            }

            throw FException.parsingError("unknown variable name: " + name);
        }

        public FrameSlot addSlot(String name) throws FException {
            try {
                FrameSlot slot = descriptor.addFrameSlot(name);
                slots.put(name, slot);
                return slot;
            } catch (IllegalArgumentException e) {
                throw FException.parsingError("redefined " + name);
            }
        }

        public void addStatement(FStatementNode stmt) { statements.add(stmt); }

        public List<FStatementNode> getStatements() { return statements; }

        public LexicalScope getParent() { return parent; }

        public LexicalScope createChild() {
            return new LexicalScope(this, descriptor, parameterCount);
        }

        public int getParameterCount() { return parameterCount; }

        public FrameDescriptor getFrameDescriptor() { return descriptor; }
    }

    private final Source source;
    private final FLanguage language;
    private final Map<String, FFunction> allFunctions;
    private final Map<String, Function<List<FExpressionNode>, FExpressionNode>> builtins;
    private LexicalScope currentScope;

    public FNodeFactory(FLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
        this.builtins = new HashMap<>();
    }

    public Map<String, FFunction> getAllFunctions() { return allFunctions; }

    public void declareStructure(String name, List<Token> fieldNameTokens, int startIndex,
                                 int stopIndex) {
        if (fieldNameTokens.isEmpty()) {
            throw FException.parsingError("Missing structure fields");
        }

        List<String> fieldNames = new ArrayList<>();
        for (Token t : fieldNameTokens) {
            fieldNames.add(t.getText());
        }

        FConstructorNode constructor = new FConstructorNode(name, fieldNames);
        registerFunction(name, fieldNames.size(), constructor, null);
    }

    public void addGlobalVariable(String name, FExpressionNode initialValue, int startIndex,
                                  int stopIndex) {
        throw new NotImplementedException();
    }

    public void startFunction(String funcName, List<Token> parameters) {
        currentScope = new LexicalScope(null, new FrameDescriptor(), parameters.size());
        startBlock();

        if (funcName.equals("main") && !parameters.isEmpty()) {
            throw FException.parsingError("main function must not have parameters");
        }

        Set<String> paramNames = new HashSet<>();

        for (int i = 0; i < parameters.size(); i++) {
            String paramName = parameters.get(i).getText();
            if (!paramNames.add(paramName)) {
                throw FException.parsingError("Duplicate parameter name: " + paramName);
            }
            FReadArgumentNode readArg = new FReadArgumentNode(i);
            FStatementNode assignment = declareVariable(paramName, null, readArg, -1, -1);
            currentScope.addStatement(assignment);
        }
    }

    public void finishFunction(String name, FStatementNode body, int startIndex, int stopIndex) {
        currentScope.addStatement(body);
        FBlockNode funcRootBlock = finishBlock(startIndex, stopIndex);

        assert currentScope.getParent() == null : "Wrong scoping of blocks in parser";

        final FFunctionBodyNode funcBodyNode = new FFunctionBodyNode(funcRootBlock);
        funcBodyNode.setSourceSection(startIndex, stopIndex);
        registerFunction(name, currentScope.getParameterCount(), funcBodyNode,
                         currentScope.getFrameDescriptor());
        currentScope = null;
    }

    public void registerFunction(String name, Integer parameterCount, FExpressionNode rootStatement,
                                 FrameDescriptor frameDescriptor) {
        if (name.startsWith("@") && !Objects.equals(parameterCount, 2)) {
            throw FException.parsingError("user-defined binary operator must have 2 parameters");
        }
        final FRootNode rootNode = new FRootNode(language, frameDescriptor, rootStatement, name);
        if (builtins.containsKey(name)) {
            throw FException.parsingError("builtin " + name + " redefined");
        }
        if (allFunctions.containsKey(name)) {
            throw FException.parsingError(name + " redefined");
        }
        allFunctions.put(name, new FFunction(name, Truffle.getRuntime().createCallTarget(rootNode),
                                             parameterCount));
    }

    public void registerBuiltin(String name,
                                Function<List<FExpressionNode>, FExpressionNode> factory) {
        if (builtins.containsKey(name)) {
            throw FException.internalError("builtin " + name + " redefined");
        }
        builtins.put(name, factory);
    }

    public void startBlock() {
        assert currentScope != null;
        currentScope = currentScope.createChild();
    }

    public FBlockNode finishBlock(int startIndex, int stopIndex) {
        FStatementNode[] statements = currentScope.getStatements().toArray(new FStatementNode[0]);
        FBlockNode blockNode = new FBlockNode(statements);
        blockNode.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));

        currentScope = currentScope.getParent();
        return blockNode;
    }

    public void addStatement(FStatementNode stmt) { currentScope.addStatement(stmt); }

    public <T extends FStatementNode> T withLocation(T node, int startIndex, int stopIndex) {
        node.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return node;
    }

    public FStatementNode createWhile(FExpressionNode condition, FBlockNode body, int startIndex,
                                      int stopIndex) {
        return new FWhileNode(condition, body);
    }

    public FStatementNode createIf(FExpressionNode condition, FStatementNode thenBranch,
                                   FStatementNode elseBranch, int startIndex) {
        return new FIfNode(condition, thenBranch, elseBranch);
    }

    public FStatementNode createFor(FStatementNode prologue, FExpressionNode condition,
                                    FStatementNode postIter, FBlockNode body, int startIndex,
                                    int stopIndex) {
        return new FForNode(prologue, condition, postIter, body);
    }

    public FStatementNode declareVariable(String name, List<FExpressionNode> arrayShape,
                                          FExpressionNode initialValue, int startIndex,
                                          int stopIndex) {
        if (builtins.containsKey(name)) {
            throw FException.parsingError("builtin " + name + " redefined");
        }
        if (initialValue == null) {
            initialValue = new FNullConstantNode();
        }

        FrameSlot slot = currentScope.addSlot(name);
        if (arrayShape == null || arrayShape.isEmpty()) {
            return FWriteLocalVariableNodeGen.create(initialValue, slot);
        } else {
            return FWriteLocalVariableNodeGen.create(new FAllocArrayNode(arrayShape, initialValue),
                                                     slot);
        }
    }

    public FStatementNode createReturn(FExpressionNode returnValue, int startIndex, int stopIndex) {
        return new FReturnNode(returnValue);
    }

    public FStatementNode createAssert(FExpressionNode expressionNode, String source,
                                       int startIndex, int stopIndex) {
        return FAssertBuiltinNodeGen.create(source, expressionNode);
    }

    public FStatementNode createBreak(int startIndex, int stopIndex) { return new FBreakNode(); }

    public FStatementNode createContinue(int startIndex, int stopIndex) {
        return new FContinueNode();
    }

    public FExpressionNode createBinOp(String op, FExpressionNode lhs, FExpressionNode rhs) {
        switch (op) {
        case "^":
            return FPowNodeGen.create(lhs, rhs);
        case "*":
            return FMultiplicationNodeGen.create(lhs, rhs);
        case "/":
            return FDivNodeGen.create(lhs, rhs);
        case "%":
            return FModNodeGen.create(lhs, rhs);
        case "+":
            return FAddNodeGen.create(lhs, rhs);
        case "-":
            return FSubNodeGen.create(lhs, rhs);
        case "<":
            return FLessNodeGen.create(lhs, rhs);
        case "<=":
            return FLessEqNodeGen.create(lhs, rhs);
        case "==":
            return FEqNodeGen.create(lhs, rhs);
        case "!=":
            return FNEqNodeGen.create(lhs, rhs);
        case ">=":
            return FGreaterEqNodeGen.create(lhs, rhs);
        case ">":
            return FGreaterNodeGen.create(lhs, rhs);
        case "&&":
            return new FAndNode(lhs, rhs);
        case "||":
            return new FOrNode(lhs, rhs);
        default:
            throw FException.internalError("Unknown binary operator token: " + op);
        }
    }

    public FExpressionNode createUnOp(String op, FExpressionNode rhs, int startIndex,
                                      int stopIndex) {
        switch (op) {
        case "+":
            return FUnaryPlusNodeGen.create(rhs);
        case "-":
            return FUnaryMinusNodeGen.create(rhs);
        case "!":
            return FNotNodeGen.create(rhs);
        default:
            throw FException.internalError("Unknown unary operator token: " + op);
        }
    }

    public FExpressionNode createCall(String funcName, List<FExpressionNode> arguments,
                                      int startIndex, int stopIndex) {
        if (builtins.containsKey(funcName)) {
            return builtins.get(funcName).apply(arguments);
        }

        final FFunctionRef funcNode = new FFunctionRef(language, funcName);
        final FExpressionNode result =
            new FInvokeNode(funcNode, arguments.toArray(new FExpressionNode[0]));
        result.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return result;
    }

    public FExpressionNode createRead(String name, int startIndex, int stopIndex) {
        FrameSlot slot = currentScope.resolveSlot(name);
        return FReadLocalVariableNodeGen.create(slot);
    }

    public FStringConstantNode createStringLiteral(Token token, boolean removeQuotes) {
        String literal = token.getText();

        if (removeQuotes) {
            // Remove the trailing and ending "
            assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
            literal = literal.substring(1, literal.length() - 1);
        }

        literal = StringEscapeUtils.unescapeJava(literal);

        final FStringConstantNode result = new FStringConstantNode(literal.intern());
        srcFromToken(result, token);
        return result;
    }

    public FExpressionNode createFloatLiteral(Token token) {
        return new FFloatConstantNode(token.getText());
    }

    public FExpressionNode createIntLiteral(Token token) {
        return new FIntConstantNode(token.getText());
    }

    public FExpressionNode createBoolLiteral(Token token) {
        return new FBooleanConstantNode(token.getText());
    }

    public FExpressionNode createNullLiteral(Token token) { return new FNullConstantNode(); }

    public FExpressionNode createMemberAccess(FExpressionNode lhs, String field, int startIndex,
                                              int stopIndex) {
        return FFieldAccessNodeGen.create(field, lhs);
    }

    public FStatementNode createAssignment(String name, List<Object> valueModifiers,
                                           FExpressionNode value, int startIndex, int stopIndex) {
        FrameSlot slot = currentScope.resolveSlot(name);
        if (valueModifiers == null || valueModifiers.isEmpty()) {
            return FWriteLocalVariableNodeGen.create(value, slot);
        } else {
            FExpressionNode expr = FReadLocalVariableNodeGen.create(slot);
            for (Object m : valueModifiers.subList(0, valueModifiers.size() - 1)) {
                if (m instanceof FExpressionNode) {
                    expr = FArrayAccessNodeGen.create(expr, (FExpressionNode)m);
                } else if (m instanceof String) {
                    expr = FFieldAccessNodeGen.create((String)m, expr);
                } else {
                    throw FException.internalError("Unknown assignment typeModifier type: " +
                                                   m.getClass().getName());
                }
            }

            Object m = valueModifiers.get(valueModifiers.size() - 1);
            if (m instanceof FExpressionNode) {
                return FArrayWriteNodeGen.create(expr, (FExpressionNode)m, value);
            } else if (m instanceof String) {
                return FFieldWriteNodeGen.create((String)m, expr, value);
            } else {
                throw FException.internalError("Unknown assignment typeModifier type: " +
                                               m.getClass().getName());
            }
        }
    }

    public FExpressionNode createArrayAccess(FExpressionNode lhs, FExpressionNode index,
                                             int startIndex, int stopIndex) {
        return FArrayAccessNodeGen.create(lhs, index);
    }

    private static void srcFromToken(FStatementNode node, Token token) {
        node.setSourceSection(token.getStartIndex(), token.getText().length());
    }

    private static int calculateLength(int startIndex, int stopIndex) {
        assert startIndex <= stopIndex;
        return stopIndex - startIndex + 1;
    }
}
