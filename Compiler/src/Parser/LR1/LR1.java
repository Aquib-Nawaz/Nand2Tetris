package Parser.LR1;

import Parser.Exceptions.ParsingException;
import Parser.LRBase;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LR1 extends LRBase {
    public LR1(List<Rule> rules) {
        super(rules);
        computeFirstSet();
    }

    @Override
    protected void checkException(HashMap<String, Integer> parsingTableRow, HashMap<String, Integer> reduceRow,
                                  HashSet<LRItemBase> curState) throws ParsingException {

    }

    @Override
    protected LRItemBase getInitialItem() {
        return new LR1Item(rules.size() - 1, 0, new HashSet<>());
    }

    @Override
    public LRItemBase getItemForClosure(LRItemBase parentItem, int ruleNum) {
        var parentRule = rules.get(parentItem.ruleNum());
        var pos = parentItem.pos()+1;
        HashSet<String> childNextSet;
        if(pos != parentRule.rhs().size()){
            var zSymbol = parentRule.rhs().get(pos);
            childNextSet = getFirstSet(zSymbol);
        }
        else{
            assert parentItem instanceof LR1Item;
            childNextSet = ((LR1Item)parentItem).getNextSet();
        }
        return new LR1Item(ruleNum, 0, childNextSet);
    }

    @Override
    public HashSet<LRItemBase> mergeClosureListIntoHashSet(List<LRItemBase> list){
        record RulePos(int ruleNum, int pos){}
        HashMap<RulePos, LR1Item> map = new HashMap<>();
        for(LRItemBase item : list){
            assert item instanceof LR1Item;
            var oldItem = map.get(new RulePos(item.ruleNum(), item.pos()));
            if(oldItem == null){
                map.put( new RulePos(item.ruleNum(), item.pos()), (LR1Item)item);
            }
            else{
                oldItem.addFollowSet((LR1Item) item);
            }
        }
        return new HashSet<>(map.values());
    }
}
