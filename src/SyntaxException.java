package src;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String message, int line) {

        super("Line " + (line) + ": " + message);
    }
    public SyntaxException(String message) {
        super(message);
    }
}