package Parser;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class ParserUtility {
    private void dfsFirst(List<Rule> rules, List<List<String>> first){}
    public static HashMap<String, List<String>> getFirstSet(List<Rule> rules) {

        List<List<String>> first = new ArrayList<>();
        return null;
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

    public static HashMap<Integer, List<Integer>> initializeRuleMap(List<Rule> rules) {
        HashMap<Integer, List<Integer>> ruleMap = new HashMap<>();
        for (int i = 0; i < rules.size(); i++) {
            int lhs = rules.get(i).lhs().getId();
            var map = ruleMap.getOrDefault(lhs, new ArrayList<>());
            map.add(i);
            ruleMap.put(lhs, map);
        }
        return ruleMap;
    }


}
