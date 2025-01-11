package nand2tetris.scanner;

public interface Automaton {
    public int match(String s);
    public NextTransition nextTransition(int state, char c);
}
