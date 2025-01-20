package Parser;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class ParserUtility {

    public static List<HashSet<String>> getFirstSet(List<Rule> rules, int nonTerminals) {
        List<HashSet<String>> firstSet = new ArrayList<>();
        for (int i = 0; i < nonTerminals; i++) {
            firstSet.add(new HashSet<>());
        }
        HashSet<Integer> isChanged = new HashSet<>();
        do{
            HashSet<Integer> isChangedCurr = new HashSet<>();
            for(Rule rule: rules){
                var rhs = rule.rhs().getFirst();
                var lhs = rule.lhs();
                var lhsFirst = firstSet.get(lhs.getId());
                boolean curChange=false;
                if(rhs.isTerminal()){
                    curChange = lhsFirst.add(rhs.toString());
                }
                else if(isChanged.contains(rhs.getId())){
                    var rhsFirst = firstSet.get(rhs.getId());
                    curChange = lhsFirst.addAll(rhsFirst);
                }

                if(curChange) {
                    isChanged.add(lhs.getId());
                    isChangedCurr.add(lhs.getId());
                }
            }
            isChanged = isChangedCurr;
        }while (!isChanged.isEmpty());
        return firstSet;
    }

    public static List <HashSet<String>> getFollowSet(List<Rule> rules, List <List<Integer>> ruleMap, int nonTerminals,
                                                                 List< HashSet<String>> firstSet) {
        List <HashSet<String>> followSet = new ArrayList<>(nonTerminals);
        HashSet<Integer> toFollow = new HashSet<>();
        for (int i = 0; i < nonTerminals; i++) {
            followSet.add(new HashSet<>());
            toFollow.add(i);
        }

        for(Rule rule: rules){
            var rhs = rule.rhs();
            for(int i=0;i<rhs.size()-1;i++){
                var symbol = rhs.get(i);
                if(symbol.isTerminal()) continue;
                var symbolId = symbol.getId();
                var next = rhs.get(i+1);
                var currFollow = followSet.get(symbolId);
                if(next.isTerminal()) {
                    currFollow.add(next.toString());
                }
                else{
                    currFollow.addAll(firstSet.get(next.getId()));
                }
            }
        }

        while(!toFollow.isEmpty()){
            var next = toFollow.iterator().next();
            toFollow.remove(next);

            for (int i : ruleMap.get(next)) {
                var rule = rules.get(i);
                var lastSymbol = rule.rhs().getLast();
                if(lastSymbol.isTerminal()) continue;
                var rhsId = lastSymbol.getId();
                var follow = followSet.get(next);
                var rhsFollow = followSet.get(rhsId);
                if(rhsFollow.addAll(follow))
                    toFollow.add(rhsId);
            }
        }
        return followSet;
    }

    public static int setTokenToId(Symbol token, int nonTerminals) {
        if(!token.isTerminal() && token.getId()==-1){
            token.setId(nonTerminals++);
        }
        return nonTerminals;
    }
    public static int initializeId(List<Rule> rules) {
        int nonTerminals = 1;
        for (Rule rule : rules) {
            var token = rule.lhs();
            nonTerminals = setTokenToId(token, nonTerminals);
            for (Symbol token1 : rule.rhs()) {
                nonTerminals = setTokenToId(token1, nonTerminals);
            }
        }
        return nonTerminals;
    }

    public static List<List<Integer>> initializeRuleMap(List<Rule> rules, int nonTerminals) {
        List <List<Integer>> ruleMap = new ArrayList<>(nonTerminals);

        for (int i = 0; i < nonTerminals; i++) {
            ruleMap.add(new ArrayList<>());
        }

        for (int i = 0; i < rules.size(); i++) {
            int lhs = rules.get(i).lhs().getId();
            var map = ruleMap.get(lhs);
            map.add(i);
        }
        return ruleMap;
    }


}
