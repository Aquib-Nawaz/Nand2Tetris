package Parser;

public class MethodNotSupportedException extends RuntimeException {

    public MethodNotSupportedException(String method, InstructionType instructionType) {
        super("method " + method + " is not available for " + instructionType+".");
    }
}