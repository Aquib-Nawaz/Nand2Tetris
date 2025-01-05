package Lexer.RegexReader;

import java.util.Stack;

public class RegexReader {
    private final String regex;
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
                case '[':
                    break;
                case '.':{
                    var newState = ret.createState();
                    ret.addDotTransition(curState, newState);
                    runningStartState = curState;
                    curState = newState;
                    break;
                }
                case '?':{
                    var newState = ret.createState();
                    ret.addEpsilonTransition(runningStartState, newState);
                    ret.addEpsilonTransition(curState, newState);
                    curState = newState;
                    runningStartState = -1;
                    break;
                }
                case '\\':
                    assert i+1 < regex.length();
                    c = regex.charAt(++i);
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
}
