package Parser;

public class NonTerminal extends Token {

    public NonTerminal(String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}
