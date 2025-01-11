package Lexer.RegexReader;

import Lexer.Automaton.DFA;
import Lexer.Automaton.NFA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RegexReader {
    private String regex;
    private final NFA nfa;
    private final  int S_STATE;
    private static final List<Character> specialChars = List.of(
            '[',']','\\','^','*','+','$','?','.','(',')','|');
    private static final Map<Character, Integer> escapedSequence = Map.of(
            '0', 0,'t', 9, 'r', 13, 'n', 10, 'v', 11 );
    public RegexReader(){
        nfa = new NFA();
        S_STATE = nfa.createState(); // start state
    }

    public NFA addRegex(String regex, int rule){
        this.regex = regex;
        Stack<Integer> startStates = new Stack<>();
        Stack<Integer> endStates = new Stack<>();
        startStates.push(S_STATE);
        int curState = nfa.createState();
        nfa.addEpsilonTransition(S_STATE, curState);
        int runningStartState = curState;
        int runningEndState = nfa.createState();
        nfa.addFinalState(runningEndState, rule);
        for(int i=0; i<regex.length(); i++){
            char c = regex.charAt(i);
            switch (c){
                case '*':{
                    var newState = nfa.createState();
                    assert runningStartState != -1;
                    nfa.addEpsilonTransition(runningStartState, newState);
                    nfa.addEpsilonTransition(curState, newState);
                    nfa.addEpsilonTransition(newState, runningStartState);
                    curState = newState;
                    runningStartState = -1;
                    break;
                }
                case '|':
                    nfa.addEpsilonTransition(curState, runningEndState);
                    runningStartState = curState = nfa.createState();
                    nfa.addEpsilonTransition(startStates.peek() ,curState);
                    break;
                case '(':
                    startStates.push(curState);
                    endStates.push(runningEndState);
                    runningStartState = nfa.createState();
                    nfa.addEpsilonTransition(curState, runningStartState);
                    curState = runningStartState;
                    runningEndState = nfa.createState();
                    break;
                case ')':
                    var prevEndState = endStates.pop();
                    var prevStartState = startStates.pop();
                    nfa.addEpsilonTransition(curState, runningEndState);
                    curState = runningEndState;
                    runningStartState = prevStartState;
                    runningEndState = prevEndState;
                    break;
                case '[':{
                    var record = bracketCharacters(i+1);
                    var newState = nfa.createState();
                    if(record.opposite){
                        nfa.addOppositeTransitions(curState, newState, record.characters);
                    }
                    else{
                        nfa.addMultipeTransitions(curState, newState, record.characters);
                    }
                    i = record.idx;
                    runningStartState = curState;
                    curState = newState;
                    break;
                }
                case '.':{
                    var newState = nfa.createState();
                    nfa.addDotTransition(curState, newState);
                    runningStartState = curState;
                    curState = newState;
                    break;
                }
                case '?':{
                    var newState = nfa.createState();
                    assert runningStartState != -1;
                    nfa.addEpsilonTransition(runningStartState, newState);
                    nfa.addEpsilonTransition(curState, newState);
                    curState = newState;
                    runningStartState = -1;
                    break;
                }
                case '\\':
                    c = getEscapedCharValue(++i);
                default:
                    var newState = nfa.createState();
                    nfa.addTransition(curState, newState, c);
                    runningStartState = curState;
                    curState = newState;
                    break;
            }
        }
        nfa.addEpsilonTransition(curState, runningEndState);
        return nfa;
    }

    private char getEscapedCharValue(int i){
        assert i < regex.length();
        char c = regex.charAt(i);

        if(escapedSequence.containsKey(c)){
            c = (char)(int)escapedSequence.get(c);
        }
        else if(!specialChars.contains(c)){
            throw new IllegalStateException("Invalid escape sequence: " + c);
        }
        return c;
    }
    private record Record(int idx, List<Character> characters, boolean opposite){}

    private Record bracketCharacters(int idx){
        char lastChar = (char)-1;
        List<Character> ret = new ArrayList<>();
        boolean opposite = false;
        if(idx<regex.length() && regex.charAt(idx) == '^'){
            opposite = true;
            idx++;
        }
        while (idx < regex.length() && regex.charAt(idx) != ']') {
            char c = regex.charAt(idx);
            if (c=='-'){
                idx++;
                assert(idx < regex.length());
                c = regex.charAt(idx);
                if(c=='\\') {
                    assert(idx+1 < regex.length());
                    c = getEscapedCharValue(++idx);
                }
                for(int i =  lastChar+1; i<=c; i++){
                    ret.add((char)i);
                }
                lastChar = (char) -1;
                idx++;
                continue;
            }
            if(c=='\\') {
                ++idx;
                assert(idx < regex.length());
                c = getEscapedCharValue(idx);
            }
            lastChar = c;
            ret.add(c);
            idx++;
        }
        assert(idx < regex.length() && regex.charAt(idx) == ']');
        return new Record(idx, ret, opposite);
    }

    public DFA getDFA(){
        return new DFA(nfa);
    }
}
