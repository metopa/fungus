package cz.metopa.fungus.parser;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.FLanguage;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FRootNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.nodes.controlflow.FBlockNode;
import cz.metopa.fungus.nodes.controlflow.FFunctionBodyNode;
import cz.metopa.fungus.nodes.controlflow.FInvokeNode;
import cz.metopa.fungus.nodes.expression.*;
import cz.metopa.fungus.nodes.expression.constants.*;
import cz.metopa.fungus.runtime.FFunction;
import org.antlr.v4.runtime.Token;
import org.apache.commons.text.StringEscapeUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FNodeFactory {
    private static class LexicalScope {
        private final LexicalScope parent;
        private final Map<String, FrameSlot> slots;
        private final List<FStatementNode> statements;
        private final FrameDescriptor descriptor;

        public LexicalScope(LexicalScope parent, FrameDescriptor descriptor) {
            this.parent = parent;
            this.slots = new HashMap<>();
            this.statements = new ArrayList<>();
            this.descriptor = descriptor;
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

        public void addStatement(FStatementNode stmt) {
            statements.add(stmt);
        }

        public List<FStatementNode> getStatements() {
            return statements;
        }

        public LexicalScope getParent() {
            return parent;
        }

        public LexicalScope createChild() {
            return new LexicalScope(this, descriptor);
        }

        public FrameDescriptor getFrameDescriptor() {
            return descriptor;
        }
    }

    private final Source source;
    private final FLanguage language;
    private final Map<String, FFunction> allFunctions;
    private LexicalScope currentScope;

    public FNodeFactory(FLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
    }

    public Map<String, FFunction> getAllFunctions() {
        return allFunctions;
    }

    public void declareStructure(String name, List<Token> parameters, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public void addGlobalVariable(String name, FExpressionNode initialValue, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public void startFunction(List<Token> parameters) {
        currentScope = new LexicalScope(null, new FrameDescriptor());
        startBlock();

        for (int i = 0; i < parameters.size(); i++) {
            FReadArgumentNode readArg = new FReadArgumentNode(i);
            FStatementNode assignment = declareVariable(parameters.get(i).getText(), readArg, -1, -1);
            currentScope.addStatement(assignment);
        }
    }

    /**
     * Declaration creates FFunction: {name, callTarget} -> save to registry. On load gets callTarget
     * Invoke creates FInvokeNode: {name, arguments} -> on execute: lookup function in context, cache it.
     * run DirectCall()
     * Builtin: pass custom FStatement + FrameDescriptor
     */
    public void finishFunction(String name, FStatementNode body, int startIndex, int stopIndex) {
        currentScope.addStatement(body);
        FBlockNode funcRootBlock = finishBlock(startIndex, stopIndex);

        assert currentScope.getParent() == null : "Wrong scoping of blocks in parser";

        final FFunctionBodyNode funcBodyNode = new FFunctionBodyNode(funcRootBlock);
        funcBodyNode.setSourceSection(startIndex, stopIndex);
        registerFunction(name, funcBodyNode, currentScope.getFrameDescriptor());
        currentScope = null;
    }

    public void registerFunction(String name, FExpressionNode rootStatement, FrameDescriptor frameDescriptor) {
        final FRootNode rootNode = new FRootNode(language, frameDescriptor, rootStatement, name);
        if (allFunctions.containsKey(name)) {
            throw FException.parsingError(name + " redefined");
        }
        allFunctions.put(name, new FFunction(name, Truffle.getRuntime().createCallTarget(rootNode)));
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

    public void addStatement(FStatementNode stmt) {
        currentScope.addStatement(stmt);
    }

    public <T extends FStatementNode> T withLocation(T node, int startIndex, int stopIndex) {
        node.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return node;
    }

    public FStatementNode createWhile(FExpressionNode condition, FBlockNode body, int startIndex, int stopIndex) {
        /*if (condition == null || body == null) {
            return null;
        }

        final FWhileNode whileNode = new FWhileNode(condition, body);
        whileNode.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return whileNode;*/

        throw new NotImplementedException();
    }

    public FStatementNode createIf(FExpressionNode condition, FStatementNode thenBranch, FStatementNode elseBranch,
                                   int startIndex) {
        throw new NotImplementedException();
    }

    public FStatementNode createFor(FStatementNode prologue, FExpressionNode condition, FExpressionNode postIter,
                                    FBlockNode body, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FStatementNode declareVariable(String name, FExpressionNode initialValue, int startIndex, int stopIndex) {
        FrameSlot slot = currentScope.addSlot(name);
        return FWriteLocalVariableNodeGen.create(initialValue, slot);
    }

    public FStatementNode createReturn(FExpressionNode returnValue, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FStatementNode createAssert(FExpressionNode expressionNode, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FStatementNode createDebuggerHalt(int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FStatementNode createBreak(int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FStatementNode createContinue(int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FExpressionNode createBinOp(String opText, FExpressionNode lhs, FExpressionNode rhs) {
        throw new NotImplementedException();
    }

    public FExpressionNode createUnOp(String op, FExpressionNode rhs, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FExpressionNode createCall(String funcName, List<FExpressionNode> arguments, int startIndex, int stopIndex) {
        if (funcName == null || arguments == null || arguments.contains(null)) {
            return null;
        }

        final FFunctionRef funcNode = new FFunctionRef(language, funcName);

        final FExpressionNode result = new FInvokeNode(funcNode, arguments.toArray(new FExpressionNode[0]));

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

    public FExpressionNode createNullLiteral(Token token) {
        return new FNullConstantNode();
    }

    public FExpressionNode createMemberAccess(FExpressionNode lhs, String field, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public FExpressionNode createArrayAccess(FExpressionNode lhs, FExpressionNode index, int startIndex,
                                             int stopIndex) {
        throw new NotImplementedException();
    }

    /**
     * Creates source description of a single token.
     */
    private static void srcFromToken(FStatementNode node, Token token) {
        node.setSourceSection(token.getStartIndex(), token.getText().length());
    }

    private static int calculateLength(int startIndex, int stopIndex) {
        assert startIndex <= stopIndex;
        return stopIndex - startIndex + 1;
    }
}
