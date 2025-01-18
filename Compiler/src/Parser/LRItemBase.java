package Parser;

import Parser.Exceptions.ParsingException;

import java.util.HashMap;
import java.util.HashSet;

public class LRItemBase {

    protected int ruleNum;
    protected int pos;

    public LRItemBase(int ruleNum, int pos) {
        this.ruleNum = ruleNum;
        this.pos = pos;
    }

    public int ruleNum() {
        return ruleNum;
    }

    public int pos() {
        return pos;
    }

    public LRItemBase getChildItem(int i) {
        return new LRItemBase(i, 0);
    }

    public LRItemBase getGoToItem() {
        return new LRItemBase(ruleNum, pos + 1);
    }

    public void putReduceState(HashMap<String, Integer> reduceTableRow,
                               HashSet<LRItemBase> curState)
    throws ParsingException {
        reduceTableRow.put("<all>", ruleNum);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof LRItemBase other)) return false;
        return ruleNum == other.ruleNum && pos == other.pos;
    }

    @Override
    public int hashCode() {
        return ruleNum * 31 + pos;
    }
}
