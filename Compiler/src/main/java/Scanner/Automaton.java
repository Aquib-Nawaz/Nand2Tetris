package Scanner;

public interface Automaton {
    int match(String s);
    NextTransition nextTransition(int state, char c);
}
