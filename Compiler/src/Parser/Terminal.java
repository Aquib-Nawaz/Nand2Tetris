package Parser;

public class Terminal extends Token{

    public String value;
    public Terminal(String name) {
        super(name);
    }

    public Terminal(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
