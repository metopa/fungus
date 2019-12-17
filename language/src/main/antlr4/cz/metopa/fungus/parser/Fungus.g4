grammar Fungus;

@parser::header
{
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.RootCallTarget;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.FLanguage;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FRootNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.nodes.controlflow.FBlockNode;
import cz.metopa.fungus.nodes.controlflow.FFunctionBodyNode;
import cz.metopa.fungus.nodes.controlflow.FInvokeNode;
import cz.metopa.fungus.nodes.expression.FFunctionRef;
import cz.metopa.fungus.nodes.expression.FReadArgumentNode;
import cz.metopa.fungus.nodes.expression.constants.FStringConstantNode;
import cz.metopa.fungus.runtime.FFunction;
import cz.metopa.fungus.parser.FNodeFactory;
}

@lexer::header
{
}

@parser::members
{
private FNodeFactory factory;
private Source source;

private static final class BailoutErrorListener extends BaseErrorListener {
    private final Source source;
    BailoutErrorListener(Source source) {
        this.source = source;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        throwParseError(source, line, charPositionInLine, (Token) offendingSymbol, msg);
    }
}

public void SemErr(Token token, String message) {
    assert token != null;
    throwParseError(source, token.getLine(), token.getCharPositionInLine(), token, message);
}

private static void throwParseError(Source source, int line, int charPositionInLine, Token token, String message) {
    throw FException.parsingError(message);
}

public static Map<String, FFunction> parseLanguage(FLanguage language, Source source) {
    FungusLexer lexer = new FungusLexer(CharStreams.fromString(source.getCharacters().toString()));
    FungusParser parser = new FungusParser(new CommonTokenStream(lexer));
    lexer.removeErrorListeners();
    parser.removeErrorListeners();
    BailoutErrorListener listener = new BailoutErrorListener(source);
    lexer.addErrorListener(listener);
    parser.addErrorListener(listener);
    parser.factory = new FNodeFactory(language, source);
    parser.source = source;
    parser.fungus();
    return parser.factory.getAllFunctions();
}
}

fungus:
    top_level_decl+ EOF
    ;

top_level_decl:
    func_decl       |
    global_var_decl |
    struct_decl
    ;

global_var_decl:
    s='var' IDENT '=' expr e=';'
                               { factory.addGlobalVariable($IDENT.getText(), $expr.result, $s.getStartIndex(), $e.getStopIndex()); }
    ;

struct_decl:
    s='struct' IDENT '{'
        ident_list
    e='}'                      { factory.declareStructure($IDENT.getText(), $ident_list.result, $s.getStartIndex(), $e.getStopIndex()); }
    ;

func_decl:
    s='func' IDENT
    '(' opt_ident_list ')'     { factory.startFunction($opt_ident_list.result); }
    block[false]               { factory.finishFunction($IDENT.getText(), $block.result, $s.getStartIndex(), $block.result.getStopIndex()); }
    ;

ident_list returns [List<Token> result]:
                               { $result = new ArrayList<>(); }
    IDENT                      { $result.add($IDENT); }
    (',' IDENT                 { $result.add($IDENT); } )*
    ;

opt_ident_list returns [List<Token> result]:
    ident_list    { $result = $ident_list.result; } |
    /* epsilon */ { $result = new ArrayList<>();  }
    ;

block [boolean inLoop] returns [FBlockNode result]:
    s='{'                      { factory.startBlock(); }
    (stmt[$inLoop]             { factory.addStatement($stmt.result); })*
    e='}'                      { $result = factory.finishBlock($s.getStartIndex(), $e.getStopIndex()); }
    ;

stmt[boolean inLoop] returns [FStatementNode result]:
    block[$inLoop]             { $result = $block.result;         } |
    if_block[$inLoop]          { $result = $if_block.result;      } |
    while_block                { $result = $while_block.result;   } |
    for_block                  { $result = $for_block.result;     } |
    return_stmt                { $result = $return_stmt.result;   } |
    assert_stmt                { $result = $assert_stmt.result;   } |
    {$inLoop}? break_stmt      { $result = $break_stmt.result;    } |
    {$inLoop}? continue_stmt   { $result = $continue_stmt.result; } |
    var_decl e=';'             { $result = factory.withLocation($var_decl.result, $var_decl.result.getStartIndex(), $e.getStopIndex()); } |
    expr e=';'                 { $result = factory.withLocation($expr.result, $expr.result.getStartIndex(), $e.getStopIndex()); }
    ;

if_block[boolean inLoop] returns [FStatementNode result]
locals [FExpressionNode condition,
        FStatementNode  thenBranch,
        FStatementNode  elseBranch]:
    s='if' '(' expr ')'        { $condition = $expr.result; }
    block[$inLoop]             { $thenBranch = $block.result; }
    ('else' block[$inLoop]     { $elseBranch = $block.result; })?
                               { $result = factory.createIf($condition, $thenBranch, $elseBranch, $s.getStartIndex()); }
    ;

while_block returns [FStatementNode result]:
    s='while' '(' condition=expr ')'
    body=block[true]           { $result = factory.createWhile($condition.result, $body.result, $s.getStartIndex(), $body.result.getStopIndex()); }
    ;

for_block returns [FStatementNode result]
locals [FStatementNode  prologue,
        FExpressionNode condition,
        FExpressionNode postIter]:
    s='for' '(' (for_prologue  { $prologue = $for_prologue.result; })? ';'
              (expr            { $condition = $expr.result;        })? ';'
              (expr            { $postIter = $expr.result;         })? ')'
        body=block[true]       { $result = factory.createFor($prologue, $condition, $postIter, $body.result, $s.getStartIndex(), $body.result.getStopIndex()); }
    ;

for_prologue returns [FStatementNode result]:
    var_decl                   { $result = $var_decl.result; } |
    expr                       { $result = $expr.result;     }
    ;

var_decl returns [FStatementNode result]:
    s='var' IDENT '=' expr     { $result = factory.declareVariable($IDENT.getText(), $expr.result, $s.getStartIndex(), $expr.result.getStopIndex()); }
    ;

return_stmt returns [FStatementNode result]
locals [FExpressionNode retval]:
    s='return' (expr           { $retval = $expr.result; })? e=';'
                               { $result = factory.createReturn($retval, $s.getStartIndex(), $e.getStopIndex()); }
    ;

assert_stmt returns [FStatementNode result]:
    s='assert' '(' expr ')' e=';'
                               { $result = factory.createAssert($expr.result, $s.getStartIndex(), $e.getStopIndex()); }
    ;

break_stmt returns [FStatementNode result]:
    s='break' e=';'            { $result = factory.createBreak($s.getStartIndex(), $e.getStopIndex()); }
    ;

continue_stmt returns [FStatementNode result]:
    s='continue' e=';'         { $result = factory.createContinue($s.getStartIndex(), $e.getStopIndex()); }
    ;

expr returns [FExpressionNode result]:
    // parenthesis
    s='(' expr e=')'           { $result = factory.withLocation($expr.result, $s.getStartIndex(),$e.getStopIndex()); } |
    // value access
    unop                       { $result = $unop.result; } |
    // user-defined binop
    // expr @op expr           |
    // power
    <assoc=right>lhs=expr op='^' rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); } |
    // multiplication
    lhs=expr op=('*' | '/' | '%') rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); } |
    // addition
    lhs=expr op=('+' | '-') rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); } |
    // comparison
    lhs=expr op=('<=' | '<' | '==' | '!=' | '>=' | '>') rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); } |
    // AND
    lhs=expr op='&&' rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); } |
    // OR
    lhs=expr op='||' rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); } |
    // assignment
    <assoc=right>lhs=expr op='=' rhs=expr
                               { $result = factory.createBinOp($op.getText(), $lhs.result, $rhs.result); }
    ;

unop returns [FExpressionNode result]:
    op=('-' | '+' | '!') unop  { $result = factory.createUnOp($op.getText(), $unop.result, $op.getStartIndex(), $unop.result.getStopIndex()); } |
    value                      { $result = $value.result; }
    ;

value returns [FExpressionNode result]
locals [FExpressionNode readOp]:
    IDENT '(' func_call_arguments e=')'
                               { $result = factory.createCall($IDENT.getText(), $func_call_arguments.result, $IDENT.getStartIndex(), $e.getStopIndex()); } |
    IDENT                      { $readOp = factory.createRead($IDENT.getText(), $IDENT.getStartIndex(), $IDENT.getStopIndex()); }
      type_access[$readOp]     { $result = $type_access.result;                                } |
    STRING_LITERAL             { $result = factory.createStringLiteral($STRING_LITERAL, true); } |
    FLOAT_LITERAL              { $result = factory.createFloatLiteral($FLOAT_LITERAL);         } |
    INT_LITERAL                { $result = factory.createIntLiteral($INT_LITERAL);             } |
    t=('true' | 'false')       { $result = factory.createBoolLiteral($t);                      } |
    t='null'                   { $result = factory.createNullLiteral($t);                      }
    ;

func_call_arguments returns [List<FExpressionNode> result]:
                               { $result = new ArrayList<>(); }
    (expr                      { $result.add($expr.result);   }
      (',' expr                { $result.add($expr.result);   })*
    )?
    ;

type_access[FExpressionNode lhs] returns [FExpressionNode result]:
    '[' expr e=']'             { $lhs = factory.createArrayAccess($lhs, $expr.result, $lhs.getStartIndex(), $e.getStopIndex()); }
      type_access[$lhs]        { $result = $type_access.result; } |
    '.' IDENT                  { $lhs = factory.createMemberAccess($lhs, $IDENT.getText(), $lhs.getStartIndex(), $IDENT.getStopIndex()); }
      type_access[lhs]         { $result = $type_access.result; } |
    /* epsilon */              { $result = $lhs; }
    ;

WS : [ \t\r\n\u000C]+ -> skip;
COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;

fragment LETTER : [A-Z] | [a-z] | '_' | '$';
fragment DIGIT : [0-9];
fragment HEX_DIGIT : [0-9] | [a-f] | [A-F];
fragment EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;
fragment STRING_CHAR : ~('"' | '\\' | '\r' | '\n');

IDENT : LETTER (LETTER | DIGIT)*;
STRING_LITERAL : '"' (STRING_CHAR | '\\' . )* '"';
INT_LITERAL: DIGIT+ | '0x' HEX_DIGIT+;
FLOAT_LITERAL:
    ((DIGIT+ '.' DIGIT*) | ('.' DIGIT+)) EXPONENT? |
    DIGIT+ EXPONENT
    ;