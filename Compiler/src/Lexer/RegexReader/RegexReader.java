package Lexer.RegexReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RegexReader {
    private final String regex;
    private static final List<Character> specialChars = List.of(
            '[',']','\\','^','*','+','$','?','.','(',')','|');
    private static final Map<Character, Integer> escapedSequence = Map.of(
            '0', 0,'t', 9, 'r', 13, 'n', 10, 'v', 11 );
    public RegexReader(String regex){
        this.regex = regex;
    }

    public NFA getNFA(){
        var ret = new NFA();
        Stack<Integer> startStates = new Stack<>();
        Stack<Integer> endStates = new Stack<>();
        startStates.push(ret.createState());
        int curState = ret.createState();
        ret.addEpsilonTransition(startStates.peek(), curState);
        int runningStartState = curState;
        int runningEndState = ret.createState();
        ret.addFinalState(runningEndState, 0);
        for(int i=0; i<regex.length(); i++){
            char c = regex.charAt(i);
            switch (c){
                case '*':{
                    var newState = ret.createState();
                    assert runningStartState != -1;
                    ret.addEpsilonTransition(runningStartState, newState);
                    ret.addEpsilonTransition(curState, newState);
                    ret.addEpsilonTransition(newState, runningStartState);
                    curState = newState;
                    runningStartState = -1;
                    break;
                }
                case '|':
                    ret.addEpsilonTransition(curState, runningEndState);
                    runningStartState = curState = ret.createState();
                    ret.addEpsilonTransition(startStates.peek() ,curState);
                    break;
                case '(':
                    startStates.push(curState);
                    endStates.push(runningEndState);
                    runningStartState = ret.createState();
                    ret.addEpsilonTransition(curState, runningStartState);
                    curState = runningStartState;
                    runningEndState = ret.createState();
                    break;
                case ')':
                    var prevEndState = endStates.pop();
                    var prevStartState = startStates.pop();
                    ret.addEpsilonTransition(curState, runningEndState);
                    curState = runningEndState;
                    runningStartState = prevStartState;
                    runningEndState = prevEndState;
                    break;
                case '[':{
                    var record = bracketCharacters(i+1);
                    var newState = ret.createState();
                    if(record.opposite){
                        ret.addOppositeTransitions(curState, newState, record.characters);
                    }
                    else{
                        ret.addMultipeTransitions(curState, newState, record.characters);
                    }
                    i = record.idx;
                    runningStartState = curState;
                    curState = newState;
                    break;
                }
                case '.':{
                    var newState = ret.createState();
                    ret.addDotTransition(curState, newState);
                    runningStartState = curState;
                    curState = newState;
                    break;
                }
                case '?':{
                    var newState = ret.createState();
                    assert runningStartState != -1;
                    ret.addEpsilonTransition(runningStartState, newState);
                    ret.addEpsilonTransition(curState, newState);
                    curState = newState;
                    runningStartState = -1;
                    break;
                }
                case '\\':
                    c = getEscapedCharValue(++i);
                default:
                    var newState = ret.createState();
                    ret.addTransition(curState, newState, c);
                    runningStartState = curState;
                    curState = newState;
                    break;
            }
        }
        ret.addEpsilonTransition(curState, runningEndState);
        return ret;
    }

    private char getEscapedCharValue(int i){
        assert i < regex.length();
        char c = regex.charAt(i);

        if(escapedSequence.containsKey(c)){
            c = (char)(int)escapedSequence.get(c);
        }
        else if(!specialChars.contains(c)){
            throw new IllegalArgumentException("Invalid escape sequence: " + c);
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
}
