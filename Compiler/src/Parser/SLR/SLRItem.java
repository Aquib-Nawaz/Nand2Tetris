package Parser.SLR;

import Parser.Exceptions.ReduceReduceException;
import Parser.LRItemBase;

import java.util.HashMap;
import java.util.HashSet;

public class SLRItem  extends LRItemBase {
    public SLRItem(int ruleNum, int pos) {
        super(ruleNum, pos);
    }

    @Override
    public void putReduceState(HashMap<String, Integer> reduceTableRow,
                               HashSet<LRItemBase> curState, HashSet<String>followSet)
        throws ReduceReduceException {
        for(String s: followSet){
            if(reduceTableRow.containsKey(s))
                throw new ReduceReduceException(curState);
            reduceTableRow.put(s, ruleNum);
        }
    }

    @Override
    public LRItemBase getGoToItem() {
        return new SLRItem(ruleNum, pos + 1);
    }

}
