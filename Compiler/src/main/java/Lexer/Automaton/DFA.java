package Lexer.Automaton;

import scanner.Automaton;
import scanner.NextTransition;

import java.util.*;

public class DFA implements Automaton {

    private final List<int[]> transitions;
    private final HashMap<Integer, Integer> finalStates;
    private static final int NUM_CHARS = 256;
    private static int[] initialTransition (){
        int[] ret = new int[NUM_CHARS];
        Arrays.fill(ret, -1);
        return ret;
    }
    public DFA(NFA  nfa) {
        this.transitions = new ArrayList<>();
        finalStates = new HashMap<>();

        List<TreeSet<Integer>> states = new ArrayList<>();
        states.add(new TreeSet<>());
        states.add(new TreeSet<>(nfa.closure(List.of(0))));
        transitions.add(initialTransition());
        transitions.add(initialTransition());
        int j=0;
        while (j<transitions.size()){
            var curState = states.get(j).stream().toList();
            for(char i=0; i<NUM_CHARS; i++){
                var tempState = new TreeSet<>(nfa.closure(nfa.move(curState, i)));
                int nextState = -1;
                for(int k=0; k<states.size(); k++){
                    if(states.get(k).equals(tempState)){
                        nextState = k;
                        break;
                    }
                }
                if(nextState == -1){
                    nextState = states.size();
                    states.add(tempState);
                    transitions.add(initialTransition());
                }
                transitions.get(j)[i] = nextState;
            }
            int rule = nfa.matchRule(curState);
            if(rule != -1){
                finalStates.put(j, rule);
            }
            j++;
        }
        System.out.printf("Number of states: %d%n", j);
    }
    public int match(String s) {
        int state=1;
        for(int i=0; i<s.length()&&state!=-1; i++){
            state = transitions.get(state)[s.charAt(i)];
        }
        return finalState(state);
    }

    private int finalState(int state) {
        if(state !=-1 && finalStates.containsKey(state)){
            return finalStates.get(state);
        }
        return -1;
    }

    public NextTransition nextTransition(int state, char input){
        state = transitions.get(state)[input];
        return new NextTransition(state, finalState(state));
    }
}
