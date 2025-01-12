package Parser;

public abstract class Token {
    protected int id=-1;
    protected String name;
    protected Token(String name) {
        this.name = name;
    }

    public abstract boolean isTerminal();
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
