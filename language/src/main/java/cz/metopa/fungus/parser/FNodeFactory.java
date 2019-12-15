package cz.metopa.fungus.parser;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLRootNode;
import com.oracle.truffle.sl.nodes.SLStatementNode;
import com.oracle.truffle.sl.nodes.controlflow.*;
import com.oracle.truffle.sl.nodes.expression.SLFunctionLiteralNode;
import com.oracle.truffle.sl.nodes.expression.SLInvokeNode;
import com.oracle.truffle.sl.nodes.expression.SLStringLiteralNode;
import org.antlr.v4.runtime.Token;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FNodeFactory {


    private static class LexicalScope {
        private final LexicalScope parent;
        private final FrameDescriptor slots;
        private final List<SLStatementNode> statements;

        public LexicalScope(LexicalScope parent) {
            this.parent = parent;
            this.slots = new FrameDescriptor();
            this.statements = new ArrayList<>();
        }

        public FrameSlot resolveSlot(String name) throws RuntimeException {
            FrameSlot slot = slots.findFrameSlot(name);
            if (slot != null) {
                return slot;
            }
            if (parent != null) {
                return parent.resolveSlot(name);
            }

            throw new RuntimeException("Unknown variable name: " + name);
        }

        public FrameSlot addSlot(String name) throws RuntimeException {
            try {
                return slots.addFrameSlot(name);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Name redefinition error: " + name);
            }
        }

        public void addStatement(SLStatementNode stmt) {
            statements.add(stmt);
        }

        public List<SLStatementNode> getStatements() {
            return statements;
        }

        public LexicalScope getParent() {
            return parent;
        }

        public FrameDescriptor getFrameDescriptor() {
            return slots;
        }
    }

    private final Source source;
    private final SLLanguage language;
    private final Map<String, RootCallTarget> allFunctions;
    private LexicalScope currentScope;

    public FNodeFactory(SLLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
        this.currentScope = new LexicalScope(null);
    }

    public Map<String, RootCallTarget> getAllFunctions() {
        return allFunctions;
    }

    public void declareStructure(String name, List<Token> parameters, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public void addGlobalVariable(String name, SLExpressionNode initialValue, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public void startFunction(List<Token> parameters) {
        startBlock();
        if (!parameters.isEmpty()) {
            throw new RuntimeException("Function parameters are not supported yet");
        }
    }

    public void finishFunction(String name, SLBlockNode body, int startIndex, int stopIndex) {
        currentScope.addStatement(body);
        LexicalScope funcRootScope = currentScope;
        SLBlockNode funcRootBlock = finishBlock(startIndex, stopIndex);
        assert currentScope.getParent() == null : "Wrong scoping of blocks in parser";

        final SLFunctionBodyNode funcBodyNode = new SLFunctionBodyNode(funcRootBlock);
        funcBodyNode.setSourceSection(startIndex, stopIndex);

        // TODO: get source section from body node
        final SLRootNode rootNode = new SLRootNode(language, currentScope.getFrameDescriptor(), funcBodyNode,
                funcBodyNode.getSourceSection(), name);

        allFunctions.put(name, Truffle.getRuntime().createCallTarget(rootNode));
    }

    public void startBlock() {
        assert currentScope != null;
        currentScope = new LexicalScope(currentScope);
    }

    public SLBlockNode finishBlock(int startIndex, int stopIndex) {
        SLStatementNode[] statements = currentScope.getStatements().toArray(new SLStatementNode[0]);
        SLBlockNode blockNode = new SLBlockNode(statements);
        blockNode.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));

        currentScope = currentScope.getParent();
        return blockNode;
    }

    public void addStatement(SLStatementNode stmt) {
        currentScope.addStatement(stmt);
    }

    public <T extends SLStatementNode> T withLocation(T node, int startIndex, int stopIndex) {
        node.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return node;
    }

    public SLWhileNode createWhile(SLExpressionNode condition, SLBlockNode body, int startIndex, int stopIndex) {
        if (condition == null || body == null) {
            return null;
        }

        condition.addStatementTag();
        final SLWhileNode whileNode = new SLWhileNode(condition, body);
        whileNode.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return whileNode;
    }

    public SLIfNode createIf(SLExpressionNode condition, SLStatementNode thenBranch, SLStatementNode elseBranch,
                             int startIndex) {
        throw new NotImplementedException();
    }

    public SLStatementNode createFor(SLStatementNode prologue, SLExpressionNode condition, SLExpressionNode postIter,
                                     SLBlockNode body, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLStatementNode declareVariable(String name, SLExpressionNode initialValue, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLReturnNode createReturn(SLExpressionNode returnValue, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLStatementNode createAssert(SLExpressionNode expressionNode, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLDebuggerNode createDebuggerHalt(int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLBreakNode createBreak(int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLContinueNode createContinue(int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createBinOp(String opText, SLExpressionNode lhs, SLExpressionNode rhs) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createUnOp(String op, SLExpressionNode rhs, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createCall(String funcName, List<SLExpressionNode> arguments, int startIndex,
                                       int stopIndex) {
        if (funcName == null || arguments == null || arguments.contains(null)) {
            return null;
        }

        final SLExpressionNode funcNode = new SLFunctionLiteralNode(language, funcName);

        final SLExpressionNode result = new SLInvokeNode(funcNode, arguments.toArray(new SLExpressionNode[0]));

        result.setSourceSection(startIndex, calculateLength(startIndex, stopIndex));
        return result;
    }

    public SLExpressionNode createRead(String name, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLStringLiteralNode createStringLiteral(Token token, boolean removeQuotes) {
        String literal = token.getText();

        if (removeQuotes) {
            // Remove the trailing and ending "
            assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
            literal = literal.substring(1, literal.length() - 1);
        }

        final SLStringLiteralNode result = new SLStringLiteralNode(literal.intern());
        srcFromToken(result, token);
        result.addExpressionTag();
        return result;
    }

    public SLExpressionNode createFloatLiteral(Token token) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createIntLiteral(Token token) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createBoolLiteral(Token token) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createNullLiteral(Token token) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createMemberAccess(SLExpressionNode lhs, String field, int startIndex, int stopIndex) {
        throw new NotImplementedException();
    }

    public SLExpressionNode createArrayAccess(SLExpressionNode lhs, SLExpressionNode index, int startIndex,
                                              int stopIndex) {
        throw new NotImplementedException();
    }

    /**
     * Creates source description of a single token.
     */
    private static void srcFromToken(SLStatementNode node, Token token) {
        node.setSourceSection(token.getStartIndex(), token.getText().length());
    }

    private static int calculateLength(int startIndex, int stopIndex) {
        assert startIndex <= stopIndex;
        return stopIndex - startIndex + 1;
    }
}
