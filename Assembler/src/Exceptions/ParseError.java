package Exceptions;

public class ParseError extends RuntimeException{

    public ParseError(String cause) {
        super("File Parse Error: " + cause);
    }
}
