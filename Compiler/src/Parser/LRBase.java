package Parser;

import Parser.Exceptions.ShiftReduceException;
import Parser.LR0.LR0Item;

import java.util.*;

public abstract class LRBase {
    protected int nonTerminals;
    protected List <Rule> rules;
    protected Map<Integer, List<Integer>> ruleMap;
    private List<HashMap<String, Integer>>parsingTable;
    protected HashSet<Integer> acceptingStates;
    protected List<Map<String, Integer>> reduceStates;
    public LRBase(List<Rule> rules) throws ShiftReduceException {
        if(rules == null || rules.isEmpty()) return;
        this.rules = rules;
        this.rules.add(new Rule(new Symbol("S'", false),
                List.of(rules.getFirst().lhs(), new Symbol("$", true))));
        this.rules.getLast().lhs().setId(0);
        initializeId();
        initializeRuleMap();
    }

    void setTokenToId(Symbol token){
        if(token.isTerminal() || token.getId()!=-1)return;
        token.setId(nonTerminals++);
    }
    private void initializeId() {
        nonTerminals = 1;
        for (Rule rule : rules) {
            var token = rule.lhs();
            setTokenToId(token);
            for (Symbol token1 : rule.rhs()) {
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

    public record parentItemChildId(LRItemBase parentItem, int childId){}

    public HashSet<LRItemBase> closure(Collection<LRItemBase> items){
        if(items == null || items.isEmpty()) return new HashSet<>();
        Queue<parentItemChildId> toExplore = new LinkedList<>();
        HashSet<Integer> vis = new HashSet<>();
        var ret = new ArrayList<LRItemBase>();
        for(LRItemBase item: items){
            var pos = item.pos();
            var rule = rules.get(item.ruleNum()).rhs();
            ret.add(item);

            if(pos < rule.size()){
                var next = rule.get(pos);
                var nextId = next.getId();
                if(next.isTerminal()||vis.contains(nextId)) continue;
                toExplore.add(new parentItemChildId(item, nextId));
                vis.add(nextId);
            }
        }
        while(!toExplore.isEmpty()){
            var next = toExplore.remove();
            for (int i : ruleMap.getOrDefault(next.childId, new ArrayList<>())) {
                ret.add(next.parentItem.getChildItem(i));
                var child = rules.get(i).rhs().getFirst();
                var childId = child.getId();
                if(!child.isTerminal() && !vis.contains(childId)){
                    toExplore.add(new parentItemChildId(next.parentItem(), childId));
                    vis.add(childId);
                }
            }
        }
        return new HashSet<>(ret);
    }

    public int getNumNonTerminals(){
        return nonTerminals;
    }

    public HashSet<LRItemBase> goTo(Collection<LRItemBase> items, Symbol token){

        List<LRItemBase> ret = new ArrayList<>();
        for(LRItemBase item: items){
            var rule = rules.get(item.ruleNum());
            var pos = item.pos();
            var next = rule.rhs().get(pos);
            if(Objects.equals(next.toString(), token.toString())){
                ret.add(item.getGoToItem());
            }
        }
        return closure(ret);
    }

    public record LRBaseState(HashSet<LRItemBase> state, int id){}

    public void createParisngTable(LRItemBase initialItem) throws ShiftReduceException {
        parsingTable = new ArrayList<>();
        Map<HashSet<LRItemBase>, Integer> states = new HashMap<>();
        reduceStates = new ArrayList<>();
        acceptingStates = new HashSet<>();
        HashSet<LRItemBase> curState = new HashSet<>();

        curState.add(getInitialItem());
        curState = closure(curState);

        int curStateId = 0;
        states.put(curState, curStateId);
        Queue<LRBaseState> toExplore = new LinkedList<>();

        toExplore.add(new LRBaseState(curState,0));

        while(!toExplore.isEmpty()){
            var curStateRecord = toExplore.remove();
            curState = curStateRecord.state;
            curStateId = curStateRecord.id;
            var parsingTableRow = new HashMap<String, Integer>();
            var reduceRow = new HashMap<String, Integer>();
            if(!checkIfReduce(curState, curStateId, reduceRow)){
                for(LRItemBase item: curState){
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
                        toExplore.add(new LRBaseState(newState, newStateId));
                        states.put(newState, states.size());
                    }
                    parsingTableRow.put(next.toString(), newStateId);
                }
            }
            parsingTable.add(parsingTableRow);
        }
    }

    protected abstract LRItemBase getInitialItem();

    private boolean checkIfReduce(HashSet<LRItemBase> curState, int curStateId, HashMap<String, Integer> reduceTableRow) {
        var item = curState.iterator().next();
        var pos = item.pos();
        var rule = rules.get(item.ruleNum());
        if(pos == rule.rhs().size()) {
            return item.putReduceState(reduceTableRow);
        }
        return false;
    }

    public List<HashMap<String, Integer>> getTable(){return parsingTable;}

    private boolean tryReduce(Token token, int curState, Stack<Integer> stack) {
        var reduceRow = reduceStates.get(curState);
        while(reduceRow.containsKey(token.toString()) || reduceRow.containsKey("<all>")){
            var ruleNum = reduceRow.get(token.toString());
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
            if(tryReduce(token, curState, stack))
                return false;
            curState = stack.peek();
            curState = parsingTable.get(curState).getOrDefault(token.toString(),-1);
            if(curState == -1) return false;
            stack.push(curState);
        }

        if(tryReduce(new Token("$") ,curState, stack)) return false;
        return acceptingStates.contains(stack.peek());
    }
}
