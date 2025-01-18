package Parser.LR0;

import Parser.Exceptions.ParsingException;
import Parser.Exceptions.ReduceReduceException;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.HashMap;
import java.util.HashSet;

public class LR0Item extends LRItemBase {

    public LR0Item(int ruleNum, int pos) {
        super(ruleNum, pos);
    }

    @Override
    public void putReduceState(HashMap<String, Integer> reduceTableRow,
                               HashSet<LRItemBase> curState, HashSet<String>followSet)
        throws ParsingException {
        if(reduceTableRow.isEmpty())
            reduceTableRow.put("<all>", ruleNum);
        else
            throw new ReduceReduceException(curState);
    }

    @Override
    public LRItemBase getChildItem(int i) {
        return new LR0Item(i, 0);
    }

    @Override
    public LRItemBase getGoToItem() {
        return new LR0Item(ruleNum, pos + 1);
    }

}
