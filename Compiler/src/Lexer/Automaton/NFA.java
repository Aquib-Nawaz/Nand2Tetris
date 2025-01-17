package Lexer.Automaton;
import java.util.*;

public class NFA  {
    private static final int NUM_CHARS = 256;
    private static final Integer INT_MAX = 2147483647;
    private final List<int[]> transitions;
    private final HashMap<Integer, Integer> finalStates;
    private final List<List<Integer>> epsTrasitions;
    public NFA(){
        this.transitions = new ArrayList<>();
        this.epsTrasitions = new ArrayList<>();
        this.finalStates = new HashMap<>();
    }
    public int createState(){
        var stateTransitions = new int[NUM_CHARS];
        Arrays.fill(stateTransitions, -1);
        this.transitions.add(stateTransitions);
        this.epsTrasitions.add(new ArrayList<>());
        return this.transitions.size()-1;
    }

    public void addTransition(int from, int to, char symbol){
        assert from >= 0 && from < this.transitions.size();
        assert to >= 0 && to < this.transitions.size();
        this.transitions.get(from)[symbol] = to;
    }

    public void addEpsilonTransition(int from, int to){
        assert from >= 0 && from < this.transitions.size();
        assert to >= 0 && to < this.transitions.size();
        this.epsTrasitions.get(from).add(to);
    }

    public void addFinalState(int state, int rule){
        assert state >= 0 && state < this.transitions.size();
        this.finalStates.put(state, rule);
    }

    public List<Integer> closure(List<Integer>states){
        List<Integer> ret;
        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        for(int state: states){
            q.add(state);
            visited.add(state);
        }
        ret = new ArrayList<>();
        while(!q.isEmpty()){
            int curr = q.remove();
            ret.add(curr);
            for(int next: epsTrasitions.get(curr)){
                if(!visited.contains(next)){
                    q.add(next);
                    visited.add(next);
                }
            }
        }
        return ret;
    }

    public int matchRule(List<Integer> states){
        var ret = states.stream().mapToInt(x->finalStates.getOrDefault(x, INT_MAX)).min();
        return (ret.isEmpty() || ret.getAsInt() == INT_MAX) ? -1 : ret.getAsInt();
    }
    public int match(String input){
        List<Integer>curStates = new ArrayList<>();
        curStates.add(0);
        for(int i=0; i<input.length()&&!curStates.isEmpty(); i++){
            curStates = closure(curStates);
            curStates = move(curStates, input.charAt(i));
        }
        curStates = closure(curStates);
        if(!curStates.isEmpty()){
            return matchRule(curStates);
        }
        return -1;
    }

    public List<Integer> move(List<Integer>states, char input){
        HashSet<Integer> ret = new HashSet<>();
        for(int state: states){
            if(transitions.get(state)[input] != -1){
                ret.add(transitions.get(state)[input]);
            }
        }
        return ret.stream().toList();
    }

    public void addDotTransition(int from, int to) {
        assert from >= 0 && from < this.transitions.size();
        assert to >= 0 && to < this.transitions.size();
        var temp = transitions.get(from);
        Arrays.fill(temp, to);
        temp['\n'] = -1;
    }

    public void addMultipeTransitions(int curState, int newState, List<Character> characters) {
        var temp = transitions.get(curState);
        for (char c : characters) {
            temp[c] = newState;
        }
    }

    public void addOppositeTransitions(int curState, int newState, List<Character> characters) {
        var temp = transitions.get(curState);
        Arrays.fill(temp, newState);
        for (char c : characters) {
            temp[c] = -1;
        }
    }
}
