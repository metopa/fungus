grammar Fungus;

@parser::header
{
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLRootNode;
import com.oracle.truffle.sl.nodes.SLStatementNode;
import com.oracle.truffle.sl.parser.SLNodeFactory;
import com.oracle.truffle.sl.parser.SLParseError;
}

@lexer::header
{
}

@parser::members
{
private SLNodeFactory factory;
private Source source;

private static final class BailoutErrorListener extends BaseErrorListener {
    private final Source source;
    BailoutErrorListener(Source source) {
        this.source = source;
    }
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throwParseError(source, line, charPositionInLine, (Token) offendingSymbol, msg);
    }
}

public void SemErr(Token token, String message) {
    assert token != null;
    throwParseError(source, token.getLine(), token.getCharPositionInLine(), token, message);
}

private static void throwParseError(Source source, int line, int charPositionInLine, Token token, String message) {
    int col = charPositionInLine + 1;
    String location = "-- line " + line + " col " + col + ": ";
    int length = token == null ? 1 : Math.max(token.getStopIndex() - token.getStartIndex(), 0);
    throw new SLParseError(source, line, col, length, String.format("Error(s) parsing script:%n" + location + message));
}

public static Map<String, RootCallTarget> parseLanguage(SLLanguage language, Source source) {
    FungusLexer lexer = new FungusLexer(CharStreams.fromString(source.getCharacters().toString()));
    FungusParser parser = new FungusParser(new CommonTokenStream(lexer));
    lexer.removeErrorListeners();
    parser.removeErrorListeners();
    BailoutErrorListener listener = new BailoutErrorListener(source);
    lexer.addErrorListener(listener);
    parser.addErrorListener(listener);
    parser.factory = new SLNodeFactory(language, source);
    parser.source = source;
    parser.fungus();
    return parser.factory.getAllFunctions();
}
}

// TODO: @op support
// TODO: array support

fungus:
    top_level_decl+ EOF
    ;

top_level_decl:
    func_decl    |
    var_decl ';' |
    struct_decl
    ;

var_decl:
    'var' IDENTIFIER '=' expr ;

struct_decl:
    'struct' IDENTIFIER '{'
        ident_list
    '}'
    ;

func_decl:
    'func' IDENTIFIER
    '(' opt_ident_list ')'
    block[false]
    ;

ident_list:
    IDENTIFIER (',' IDENTIFIER)*
    ;

opt_ident_list:
    ident_list |
    /* epsilon */
    ;

block [boolean inLoop]:
    '{' stmt[inLoop]* '}'
    ;

stmt[boolean inLoop]:
    block[inLoop]         |
    if_block[inLoop]      |
    while_block           |
    for_block             |
    line_stmt[inLoop] ';'
    ;

line_stmt[boolean inLoop]:
    var_decl                 |
    return_stmt              |
    assert_stmt              |
    debugger_stmt            |
    {$inLoop}? break_stmt    |
    {$inLoop}? continue_stmt |
    expr
    ;

if_block[boolean inLoop]:
    'if' '(' condition=expr ')'
      thenBranch=block[inLoop]
      ('else' elseBranch=block[inLoop])?
    ;

while_block:
    'while' '(' condition=expr ')'
        body=block[true]
    ;

for_block:
    'for' '(' for_prologue? ';' (condition=expr)? ';' (postIter=expr)? ')'
        body=block[true]
    ;

for_prologue:
    var_decl |
    expr
    ;

return_stmt:
    'return' expr
    ;

assert_stmt:
    'assert' expr
    ;

debugger_stmt:
    'halt'
    ;

break_stmt:
    'break'
    ;

continue_stmt:
    'continue'
    ;

expr:
    // parenthesis
    '(' expr ')'                                      |
    // value access
    unop                                              |
    // user-defined binop
    // expr @op expr                                  |
    // power
    <assoc=right>expr '^' expr                        |
    // multiplication
    expr ('*' | '/' | '%') expr                       |
    // addition
    expr ('+' | '-') expr                             |
    // comparison
    expr ('<' | '<=' | '==' | '!=' | '>=' | '>') expr |
    // AND
    expr '&&' expr                                    |
    // OR
    expr '||' expr                                    |
    // assignment
    <assoc=right>expr '=' expr
    ;

unop:
    '-' unop |
    '+' unop |
    '!' unop |
    value
    ;

value:
    IDENTIFIER type_access      |
    STRING_LITERAL              |
    FLOAT                       |
    INT                         |
    ('true' | 'false' | 'null')
    ;

type_access:
    '(' (expr (',' expr)*)? ')' type_access |
    '[' expr ']' type_access                |
    '.' IDENTIFIER type_access              |
    /* epsilon */
    ;

WS : [ \t\r\n\u000C]+ -> skip;
COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;

fragment LETTER : [A-Z] | [a-z] | '_' | '$';
fragment DIGIT : [0-9];
fragment HEX_DIGIT : [0-9] | [a-f] | [A-F];
fragment EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;
fragment STRING_CHAR : ~('"' | '\\' | '\r' | '\n');

IDENTIFIER : LETTER (LETTER | DIGIT)*;
STRING_LITERAL : '"' (STRING_CHAR | '\\' . )* '"';
INT: DIGIT+ | '0x' HEX_DIGIT+;
FLOAT:
    ((DIGIT+ '.' DIGIT*) | ('.' DIGIT+)) EXPONENT? |
    DIGIT+ EXPONENT
    ;