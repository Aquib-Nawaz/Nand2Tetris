package Parser.SLR;

import Parser.Exceptions.ParsingException;
import Parser.Exceptions.ShiftReduceException;
import Parser.LRBase;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SLR extends LRBase {

    public SLR(List<Rule> rules)  {
        super(rules);
        computeFollowSet();
    }

    @Override
    protected void checkException(HashMap<String, Integer> parsingTableRow, HashMap<String, Integer> reduceRow, HashSet<LRItemBase> curState)
            throws ParsingException {
        for(String s: parsingTableRow.keySet()){
            if(reduceRow.containsKey(s))
                throw new ShiftReduceException(curState);
        }
    }

    @Override
    protected LRItemBase getInitialItem() {
        return new SLRItem(rules.size()-1, 0);
    }

    @Override
    public LRItemBase getItemForClosure(LRItemBase item, int ruleNum) {
        return new SLRItem(ruleNum, 0);
    }
}
