package src;

public enum TokenType {
    // Keywords
    VAR, IF, ELSE, WHILE, FOREACH, AS,ECHO, FSCANF,
    // Identifiers and literals
    IDENTIFIER, NUMBER, STRING,
    // Operators
    ASSIGN, PLUS, MINUS, MUL, DIV,
    INC, DEC, EQ, NEQ, LT, GT, LE, GE,
    AND, OR,
    // Delimiters
    LPAREN, RPAREN,DOT,QUESTION,COLON, LBRACE, RBRACE,LBRACKET, RBRACKET,COMMA, SEMICOLON,
    // Special
    EOF
}