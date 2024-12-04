package Parser;

public class InstructionNotFoundException extends RuntimeException {
    public InstructionNotFoundException(){
        super("Next Instruction Not Found");
    }
}
