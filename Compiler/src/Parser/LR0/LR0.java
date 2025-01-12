package Parser.LR0;

import Parser.Exceptions.ShiftReduceException;
import Parser.NonTerminal;
import Parser.Rule;
import Parser.Terminal;
import Parser.Token;

import javax.print.DocFlavor;
import java.util.*;

public class LR0 {
    private List<HashMap<String, Integer>>parsingTable;
    private int nonTerminals;
    private List <Rule> rules;
    private Map<Integer, List<Integer>> ruleMap;
    private Map<Integer, Integer> reduceStates;
    private HashSet<Integer> acceptingStates;
    public LR0(List<Rule> rules) throws ShiftReduceException {
        if(rules == null || rules.isEmpty()) return;
        parsingTable = new ArrayList<>();
        this.rules = rules;
        this.rules.add(new Rule(new NonTerminal("S'"),
                List.of(rules.getFirst().lhs(), new Terminal("$"))));
        this.rules.getLast().lhs().setId(0);
        initializeId();
        initializeRuleMap();
        createParisngTable();
    }

    void setTokenToId(Token token){
        if(token.isTerminal() || token.getId()!=-1)return;
        token.setId(nonTerminals++);
    }
    private void initializeId() {
        nonTerminals = 1;
        for (Rule rule : rules) {
            Token token = rule.lhs();
            setTokenToId(token);
            for (Token token1 : rule.rhs()) {
                setTokenToId(token1);
            }
        }
    }

    private void initializeRuleMap(){
        ruleMap = new HashMap<>();
        for (int i = 0; i < rules.size(); i++) {
            int lhs = rules.get(i).lhs().getId();
            var map = ruleMap.getOrDefault(lhs, new ArrayList<>());
            map.add(i);
            ruleMap.put(lhs, map);
        }
    }

    public HashSet<LR0Item> closure(Collection<LR0Item> items){
        if(items == null || items.isEmpty()) return new HashSet<>();
        Queue<Integer> toExplore = new LinkedList<>();
        HashSet<Integer> vis = new HashSet<>();
        var ret = new ArrayList<LR0Item>();
        for(LR0Item item: items){
            var pos = item.pos();
            var rule = rules.get(item.ruleNum()).rhs();
            ret.add(item);

            if(pos < rule.size()){
                var next = rule.get(pos);
                if(next.isTerminal()) continue;
                toExplore.add(next.getId());
            }
        }
        while(!toExplore.isEmpty()){
            var next = toExplore.remove();
            if(vis.contains(next)) continue;
            for (int i : ruleMap.getOrDefault(next, new ArrayList<>())) {
                ret.add(new LR0Item(i, 0));
                var child = rules.get(i).rhs().getFirst();
                if(!child.isTerminal() && !vis.contains(child.getId())){
                    toExplore.add(child.getId());
                }
            }
            vis.add(next);
        }
        return new HashSet<>(ret);
    }

    public int getNumNonTerminals(){
        return nonTerminals;
    }

    public HashSet<LR0Item> goTo(Collection<LR0Item> items, Token token){

        List<LR0Item> ret = new ArrayList<>();
        for(LR0Item item: items){
            var rule = rules.get(item.ruleNum());
            var pos = item.pos();
            var next = rule.rhs().get(pos);
            if(Objects.equals(next.toString(), token.toString())){
                ret.add(new LR0Item(item.ruleNum(), pos + 1));
            }
        }
        return closure(ret);
    }

    public record LR0State(HashSet<LR0Item> state, int id){}
    public void createParisngTable() throws ShiftReduceException {
        Map<HashSet<LR0Item>, Integer> states = new HashMap<>();
        reduceStates = new HashMap<>();
        acceptingStates = new HashSet<>();
        HashSet<LR0Item> curState = new HashSet<>();
        curState.add(new LR0Item(rules.size()-1, 0));
        curState = closure(curState);
        int curStateId = 0;
        states.put(curState, curStateId);
        Queue<LR0State> toExplore = new LinkedList<>();
        toExplore.add(new LR0State(curState,0));

        while(!toExplore.isEmpty()){
            var curStateRecord = toExplore.remove();
            curState = curStateRecord.state;
            curStateId = curStateRecord.id;
            var parsingTableRow = new HashMap<String, Integer>();
            if(!checkIfReduce(curState, curStateId, parsingTableRow)){
                for(LR0Item item: curState){
                    var rule = rules.get(item.ruleNum());
                    var pos = item.pos();

                    var next = rule.rhs().get(pos);
                    if(next.isTerminal() && Objects.equals(next.toString(), "$")) {
                        acceptingStates.add(curStateId);
                        continue;
                    }
                    var newState = goTo(curState, next);
                    int newStateId = states.getOrDefault(newState, -1);
                    if(newStateId == -1){
                        newStateId = states.size();
                        toExplore.add(new LR0State(newState, newStateId));
                        states.put(newState, states.size());
                    }
                    parsingTableRow.put(next.toString(), newStateId);
                }
            }
            parsingTable.add(parsingTableRow);
        }
    }

    private boolean checkIfReduce(HashSet<LR0Item> curState, int curStateId, HashMap<String, Integer> parsingTableRow) {
        if(curState.size()!=1) return false;
        var item = curState.iterator().next();
        var pos = item.pos();
        var rule = rules.get(item.ruleNum());
        if(pos == rule.rhs().size()) {
            reduceStates.put(curStateId, item.ruleNum());
            return true;
        }
        return false;
    }

    public List<HashMap<String, Integer>> getTable(){return parsingTable;}

    private boolean tryReduce(int curState, Stack<Integer> stack) {
        while(reduceStates.containsKey(curState)){
            var ruleNum = reduceStates.get(curState);
            var rule = rules.get(ruleNum);
            for(int i = rule.rhs().size() - 1; i >= 0; i--){
                stack.pop();
            }
            String symbol = rule.lhs().toString();
            curState = parsingTable.get(stack.peek()).getOrDefault(symbol,-1);
            if(curState==-1) return true;
            stack.push(curState);
        }
        return false;
    }

    public boolean parse(Collection<Token> tokens){
        var stack = new Stack<Integer>();
        var curState = 0;
        stack.push(curState);
        for(Token token:tokens){
            if(tryReduce(curState, stack))
                return false;
            curState = stack.peek();
            curState = parsingTable.get(curState).getOrDefault(token.toString(),-1);
            if(curState == -1) return false;
            stack.push(curState);
        }

        if(tryReduce(curState, stack)) return false;
        return acceptingStates.contains(stack.peek());
    }
}
