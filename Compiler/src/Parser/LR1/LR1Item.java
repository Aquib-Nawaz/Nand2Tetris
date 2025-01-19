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
        assert pos < nextSet.size();
        return new LR1Item(ruleNum, pos + 1, nextSet);
    }

    @Override
    public void putReduceState(HashMap<String, Integer> reduceTableRow, HashSet<LRItemBase> curState,
                               HashSet<String> followSet) throws ParsingException {

    }

    public HashSet<String> getNextSet() {
        return nextSet;
    }

    public void addFollowSet(LR1Item item){
        nextSet.addAll(item.getNextSet());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof LR1Item other)) return false;
        return ruleNum == other.ruleNum && pos == other.pos && nextSet.equals(other.getNextSet());
    }
    @Override
    public int hashCode() {
        return ruleNum * 31 * 31 + pos * 31 + nextSet.hashCode();
    }
}
