package Parser;

public class Symbol {
    protected int id=-1;
    protected String name;
    boolean isTerminal;
    public boolean isTerminal() {
        return isTerminal;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public Symbol(String name, boolean isTerminal) {
        this.name = name;
        this.isTerminal = isTerminal;
    }

    @Override
    public String toString() {
        return name;
    }
}
