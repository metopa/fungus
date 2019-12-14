package cz.metopa.fungus.parser;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLStatementNode;
import com.oracle.truffle.sl.nodes.controlflow.SLBlockNode;
import com.oracle.truffle.sl.nodes.controlflow.SLBreakNode;
import com.oracle.truffle.sl.nodes.controlflow.SLContinueNode;
import com.oracle.truffle.sl.nodes.controlflow.SLDebuggerNode;
import com.oracle.truffle.sl.nodes.controlflow.SLIfNode;
import com.oracle.truffle.sl.nodes.controlflow.SLReturnNode;
import com.oracle.truffle.sl.nodes.controlflow.SLWhileNode;
import com.oracle.truffle.sl.nodes.expression.SLStringLiteralNode;
import org.antlr.v4.runtime.Token;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FNodeFactory {
  public void addGlobalVariable(String name, SLExpressionNode initialValue, int startIndex,
                                int stopIndex) {
  }

  /* State while parsing a source unit. */
  private final Source source;
  private final Map<String, RootCallTarget> allFunctions;
  private final SLLanguage language;

  public FNodeFactory(SLLanguage language, Source source) {
    this.language = language;
    this.source = source;
    this.allFunctions = new HashMap<>();
  }

  public Map<String, RootCallTarget> getAllFunctions() {
    return allFunctions;
  }

  public void declareStructure(String name, List<Token> parameters, int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public void startFunction(String name, List<Token> parameters) {
    throw new NotImplementedException();
  }

  public void finishFunction(SLBlockNode body, int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public void startBlock() {
    throw new NotImplementedException();
  }

  public SLBlockNode finishBlock(List<SLStatementNode> body, int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public <T extends SLStatementNode> T withLocation(T node, int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public SLWhileNode createWhile(SLExpressionNode condition, SLBlockNode body, int startIndex,
                                 int stopIndex) {
    throw new NotImplementedException();
  }

  public SLIfNode createIf(SLExpressionNode condition, SLStatementNode thenBranch,
                           SLStatementNode elseBranch, int startIndex) {
    throw new NotImplementedException();
  }

  public SLStatementNode createFor(SLStatementNode prologue, SLExpressionNode condition,
                                   SLExpressionNode postIter, SLBlockNode body, int startIndex,
                                   int stopIndex) {
    throw new NotImplementedException();
  }

  public SLStatementNode declareVariable(String name, SLExpressionNode initialValue,
                                         int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public SLReturnNode createReturn(SLExpressionNode returnValue, int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public SLStatementNode createAssert(SLExpressionNode expressionNode, int startIndex,
                                      int stopIndex) {
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

  public SLExpressionNode createUnOp(String op, SLExpressionNode rhs, int startIndex,
                                     int stopIndex) {
    throw new NotImplementedException();
  }

  public SLExpressionNode createCall(String function, List<SLExpressionNode> arguments,
                                     int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public SLExpressionNode createRead(String name, int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }

  public SLStringLiteralNode createStringLiteral(Token token) {
    throw new NotImplementedException();
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

  public SLExpressionNode createMemberAccess(SLExpressionNode lhs, String field, int startIndex,
                                             int stopIndex) {
    throw new NotImplementedException();
  }

  public SLExpressionNode createArrayAccess(SLExpressionNode lhs, SLExpressionNode index,
                                            int startIndex, int stopIndex) {
    throw new NotImplementedException();
  }
}
