package src;

import java.util.*;

public class lexical {
    private String input;
    private int pos = 0;
    private int line = 1;
    private List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords = Map.of(
            "var", TokenType.VAR,
            "if", TokenType.IF,
            "else", TokenType.ELSE,
            "while", TokenType.WHILE,
            "foreach", TokenType.FOREACH,
            "as", TokenType.AS
    );

    public lexical(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (c == '\n') {
                line++;
                pos++;
            } else if (espace(c)) {
                pos++;
            } else if (c == '$') {
                pos++; // skip the '$'
                Token token = readIdentifierOrKeyword();
                token.value = "$" + token.value;
                token.type = TokenType.IDENTIFIER;
                tokens.add(token);
            } else if (letter(c) || c == '_') {
                tokens.add(readIdentifierOrKeyword());
            } else if (nomber(c)) {
                tokens.add(readNumber());
            } else if (c == '"') {
                tokens.add(readString());
            } else {
                switch (c) {
                    case '+':
                        if (peekNext() == '+') {
                            tokens.add(new Token(TokenType.INC, "++", line));
                            pos += 2;
                        } else {
                            tokens.add(new Token(TokenType.PLUS, "+", line));
                            pos++;
                        }
                        break;
                    case '-':
                        if (peekNext() == '-') {
                            tokens.add(new Token(TokenType.DEC, "--", line));
                            pos += 2;
                        } else {
                            tokens.add(new Token(TokenType.MINUS, "-", line));
                            pos++;
                        }
                        break;
                    case '=':
                        if (peekNext() == '=') {
                            tokens.add(new Token(TokenType.EQ, "==", line));
                            pos += 2;
                        } else {
                            tokens.add(new Token(TokenType.ASSIGN, "=", line));
                            pos++;
                        }
                        break;
                    case '!':
                        if (peekNext() == '=') {
                            tokens.add(new Token(TokenType.NEQ, "!=", line));
                            pos += 2;
                        } else {
                            throw new RuntimeException("Unknown character: " + c + " at line " + line);
                        }
                        break;
                    case '<':
                        if (peekNext() == '=') {
                            tokens.add(new Token(TokenType.LE, "<=", line));
                            pos += 2;
                        } else {
                            tokens.add(new Token(TokenType.LT, "<", line));
                            pos++;
                        }
                        break;
                    case '>':
                        if (peekNext() == '=') {
                            tokens.add(new Token(TokenType.GE, ">=", line));
                            pos += 2;
                        } else {
                            tokens.add(new Token(TokenType.GT, ">", line));
                            pos++;
                        }
                        break;
                    case '*': tokens.add(new Token(TokenType.MUL, "*", line)); pos++; break;
                    case '/': tokens.add(new Token(TokenType.DIV, "/", line)); pos++; break;
                    case '&':
                        if (peekNext() == '&') {
                            tokens.add(new Token(TokenType.AND, "&&", line)); pos += 2;
                        } else {
                            throw new RuntimeException("Unknown character: " + c + " at line " + line);
                        }
                        break;
                    case '|':
                        if (peekNext() == '|') {
                            tokens.add(new Token(TokenType.OR, "||", line)); pos += 2;
                        } else {
                            throw new RuntimeException("Unknown character: " + c + " at line " + line);
                        }
                        break;
                    case '?': tokens.add(new Token(TokenType.QUESTION, "?", line)); pos++; break;
                    case ':': tokens.add(new Token(TokenType.COLON, ":", line)); pos++; break;
                    case '.': tokens.add(new Token(TokenType.DOT, ".", line)); pos++; break;
                    case ',': tokens.add(new Token(TokenType.COMMA, ",", line)); pos++; break;
                    case '[': tokens.add(new Token(TokenType.LBRACKET, "[", line)); pos++; break;
                    case ']': tokens.add(new Token(TokenType.RBRACKET, "]", line)); pos++; break;
                    case '(': tokens.add(new Token(TokenType.LPAREN, "(", line)); pos++; break;
                    case ')': tokens.add(new Token(TokenType.RPAREN, ")", line)); pos++; break;
                    case '{': tokens.add(new Token(TokenType.LBRACE, "{", line)); pos++; break;
                    case '}': tokens.add(new Token(TokenType.RBRACE, "}", line)); pos++; break;
                    case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", line)); pos++; break;
                    default:
                        throw new RuntimeException("Unknown character: " + c + " at line " + line);
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private char peekNext() {
        return pos + 1 < input.length() ? input.charAt(pos + 1) : '\0';
    }

    // Méthodes manuelles pour remplacer Character.*

    private boolean espace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\f';
    }

    private boolean letter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean nomber(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean letterOrDigit(char c) {
        return letter(c) || nomber(c);
    }

    private Token readIdentifierOrKeyword() {
        int start = pos;
        while (pos < input.length() && (letterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_'))
            pos++;
        String word = input.substring(start, pos);
        return new Token(keywords.getOrDefault(word, TokenType.IDENTIFIER), word, line);
    }

    private Token readNumber() {
        int start = pos;
        while (pos < input.length() && nomber(input.charAt(pos)))
            pos++;
        return new Token(TokenType.NUMBER, input.substring(start, pos), line);
    }

    private Token readString() {
        pos++; // skip opening "
        StringBuilder sb = new StringBuilder();

        while (pos < input.length() && input.charAt(pos) != '"') {
            sb.append(input.charAt(pos));
            pos++;
        }

        if (pos >= input.length()) {
            throw new RuntimeException("Unclosed string literal at line " + line);
        }

        pos++; // skip closing "
        return new Token(TokenType.STRING, sb.toString(), line);
    }
}