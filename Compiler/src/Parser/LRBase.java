package Parser;

import Parser.Exceptions.ParsingException;
import Parser.Exceptions.ShiftReduceException;

import java.util.*;


public abstract class LRBase {
    protected int nonTerminals;
    protected List <Rule> rules;
    protected List< List<Integer>> ruleMap;
    private List<HashMap<String, Integer>>parsingTable;
    protected HashSet<Integer> acceptingStates;
    protected List<Map<String, Integer>> reduceStates;
    public LRBase(List<Rule> rules)  {
        if(rules == null || rules.isEmpty()) return;
        this.rules = rules;
        this.rules.add(new Rule(new Symbol("S'", false),
                List.of(rules.getFirst().lhs(), new Symbol("$", true))));
        this.rules.getLast().lhs().setId(0);
        nonTerminals = ParserUtility.initializeId(rules);
        ruleMap = ParserUtility.initializeRuleMap(rules, nonTerminals);
//        createParisngTable();
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
            for (int i : ruleMap.get(next.childId)) {
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

    public void createParisngTable() throws ParsingException {
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
            for(LRItemBase item: curState){
                var rule = rules.get(item.ruleNum());
                var pos = item.pos();
                if(pos == rule.rhs().size()) {
                    item.putReduceState(reduceRow, curState);
                    continue;
                }
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
            checkException(parsingTableRow, reduceRow, curState);
            parsingTable.add(parsingTableRow);
            reduceStates.add(reduceRow);
        }
    }

    protected abstract void checkException(HashMap<String, Integer> parsingTableRow,
                                           HashMap<String, Integer> reduceRow, HashSet<LRItemBase> curState) throws ParsingException;

    protected abstract LRItemBase getInitialItem();

    public List<HashMap<String, Integer>> getTable(){return parsingTable;}

    private boolean tryReduce(Token token, int curState, Stack<Integer> stack) {
        var stringToken = token.toString();
        while(true){
            var reduceRow = reduceStates.get(curState);
            var ruleNum = reduceRow.get(stringToken);
            if(ruleNum == null) ruleNum = reduceRow.get("<all>");
            if(ruleNum == null) return false;
            var rule = rules.get(ruleNum);
            for(int i = rule.rhs().size() - 1; i >= 0; i--){
                stack.pop();
            }
            String symbol = rule.lhs().toString();
            curState = parsingTable.get(stack.peek()).getOrDefault(symbol,-1);
            if(curState==-1) return true;
            stack.push(curState);
        }

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
