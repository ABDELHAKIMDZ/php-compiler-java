package src;

import java.util.*;

public class syntaxe {
    private List<Token> tokens;
    private int pos = 0;

    public syntaxe(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token consume(TokenType type) {
        Token t = tokens.get(pos);
        if (t.type != type)
            throw new SyntaxException("Expected " + type + " but got " + t.type, t.line);
        pos++;
        return t;
    }

    public void analyseProgram() {
        while (peek().type != TokenType.EOF) {
            analyseStatement();
        }
    }

    private void analyseStatement() {
        Token t = peek();
        switch (t.type) {
            case VAR:
                analyseVarDecl();
                consume(TokenType.SEMICOLON);
                break;
            case IDENTIFIER:
                analyseAssignmentOrIncDec();
                consume(TokenType.SEMICOLON);
                break;
            case IF:
                analyseIf();
                break;
            case WHILE:
                analyseWhile();
                break;
            case FOREACH:
                analyseForeach();
                break;
            default:
                if (peek().type == TokenType.EOF) {
                    throw new SyntaxException("Unexpected end of file. Did you forget to close a block with '}'?", peek().line);
                }else
                {throw new SyntaxException("Unknown statement: " + t.type, t.line);}
        }
    }

    private void analyseVarDecl() {
        consume(TokenType.VAR);
        consume(TokenType.IDENTIFIER);
        if (peek().type == TokenType.ASSIGN) {
            consume(TokenType.ASSIGN);
            analyseExpression();
        }
    }

    private void analyseAssignmentOrIncDec() {
        Token identifier = consume(TokenType.IDENTIFIER); // Stocker le token
        Token t = peek();

        if (t.type == TokenType.ASSIGN) {
            consume(TokenType.ASSIGN);
            analyseExpression();
        } else if (t.type == TokenType.INC) {
            consume(TokenType.INC);
        } else if (t.type == TokenType.DEC) {
            consume(TokenType.DEC);
        } else {
            throw new SyntaxException("Expected =, ++, or -- but got " + t.type, t.line);
        }
    }

    private void analyseIf() {
        consume(TokenType.IF);
        consume(TokenType.LPAREN);
        analyseExpression();
        consume(TokenType.RPAREN);
        consume(TokenType.LBRACE);
        while (peek().type != TokenType.RBRACE) {
            analyseStatement();
        }
        consume(TokenType.RBRACE);
        if (peek().type == TokenType.ELSE) {
            consume(TokenType.ELSE);
            consume(TokenType.LBRACE);
            while (peek().type != TokenType.RBRACE) {
                analyseStatement();
            }
            consume(TokenType.RBRACE);
        }
    }

    private void analyseWhile() {
        consume(TokenType.WHILE);
        consume(TokenType.LPAREN);
        analyseExpression();
        consume(TokenType.RPAREN);
        consume(TokenType.LBRACE);
        while (peek().type != TokenType.RBRACE) {
            analyseStatement();
        }
        consume(TokenType.RBRACE);
    }

    private void analyseForeach() {
        consume(TokenType.FOREACH);
        consume(TokenType.LPAREN);
        consume(TokenType.IDENTIFIER);
        consume(TokenType.AS);
        consume(TokenType.IDENTIFIER);
        consume(TokenType.RPAREN);
        consume(TokenType.LBRACE);
        while (peek().type != TokenType.RBRACE) {
            analyseStatement();
        }
        consume(TokenType.RBRACE);
    }

    private void analyseExpression() {
        if (peek().type == TokenType.RPAREN || peek().type == TokenType.RBRACKET ||
                peek().type == TokenType.SEMICOLON || peek().type == TokenType.RBRACE) {
            throw new SyntaxException("Expression expected", peek().line);
        }
        analysePrimary();
        while (isOperator(peek().type)) {
            pos++;
            analysePrimary();
        }
    }

    private void analysePrimary() {
        Token t = peek();
        if (t.type == TokenType.NUMBER || t.type == TokenType.STRING) {
            pos++;
        } else if (t.type == TokenType.IDENTIFIER) {
            pos++;
            // Support for array indexing: items[i]
            while (peek().type == TokenType.LBRACKET) {
                consume(TokenType.LBRACKET);
                analyseExpression();
                consume(TokenType.RBRACKET);
            }
        } else if (t.type == TokenType.LPAREN) {
            consume(TokenType.LPAREN);
            analyseExpression();
            consume(TokenType.RPAREN);
        } else if (t.type == TokenType.LBRACKET) {
            // Array literal
            consume(TokenType.LBRACKET);
            if (peek().type != TokenType.RBRACKET) {
                analyseExpression();
                while (peek().type == TokenType.COMMA) {
                    consume(TokenType.COMMA);
                    analyseExpression();
                }
            }
            consume(TokenType.RBRACKET);

        } else {
            throw new SyntaxException("Unexpected token in exapression: " + t.type, t.line);
        }
    }

    // Utility to test if a token type is a binary operator
    private boolean isOperator(TokenType type) {
        return type == TokenType.PLUS ||
                type == TokenType.MINUS ||
                type == TokenType.MUL ||
                type == TokenType.DIV ||
                type == TokenType.EQ ||
                type == TokenType.NEQ ||
                type == TokenType.LT ||
                type == TokenType.GT ||
                type == TokenType.LE ||
                type == TokenType.GE ||
                type == TokenType.AND ||
                type == TokenType.OR ||
                type == TokenType.DOT;      // <-- ADD THIS LINE
    }
}