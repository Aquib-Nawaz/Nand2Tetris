package Lexer.RegexReader;

public class RegexReader {
    private final String regex;
    public RegexReader(String regex){
        this.regex = regex;
    }

    public NFA getNFA(){
        var ret = new NFA();
        int curState = ret.createState();
        ret.pushStartState(curState);
        for(int i=0; i<regex.length(); i++){
            char c = regex.charAt(i);
            switch (c){
                case '*':
                    ret.addEpsilonTransition(ret.peekStartState(), curState);
                    ret.addTransition(curState, curState, regex.charAt(i-1));
                    break;
                case '|':
                    ret.popStartState();
                    var startState = ret.peekStartState();
                    if(startState==-1){
                        ret.pushStartState(0);
                        startState = 0;
                        ret.addFinalState(curState, 0);
                    }
                    curState = startState;
                    ret.modifyStartState(startState);
                    break;
                case '(':
                    ret.pushStartState(curState);
                    break;
                case ')':
                    ret.popStartState();

                    break;
                case '[':
                    ret.addEpsilonTransition(curState, ret.createState());
                    curState = ret.createState();
                    break;
                case ']':
                    ret.addEpsilonTransition(curState, ret.createState());
                    ret.addEpsilonTransition(ret.createState(), curState);
                    curState = ret.createState();
                    break;
                case '{':
                    ret.addEpsilonTransition(curState, ret.createState());
                    curState = ret.createState();
                    break;
                case '}':
                    ret.addEpsilonTransition(curState, ret.createState());
                    ret.addEpsilonTransition(ret.createState(), curState);
                    curState = ret.createState();
                    break;

                default:
                    var newState = ret.createState();
                    ret.addTransition(curState, newState, c);
                    curState = newState;
            }
        }

        ret.addFinalState(curState, 0);
        return ret;
    }
}
