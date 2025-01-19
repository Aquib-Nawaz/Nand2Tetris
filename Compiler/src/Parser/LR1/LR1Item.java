package Parser.LR1;

import Parser.Exceptions.ParsingException;
import Parser.LRItemBase;

import java.util.HashMap;
import java.util.HashSet;

public class LR1Item extends LRItemBase {
    HashSet<String> nextSet;
    public LR1Item(int ruleNum, int pos, HashSet<String> nextSet) {
        super(ruleNum, pos);
        this.nextSet = nextSet;
    }


    @Override
    public LRItemBase getGoToItem() {
        return null;
    }

    @Override
    public void putReduceState(HashMap<String, Integer> reduceTableRow, HashSet<LRItemBase> curState,
                               HashSet<String> followSet) throws ParsingException {

    }
}
