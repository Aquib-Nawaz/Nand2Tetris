package Parser;

public class Token {
    protected String name;
    public Token(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
