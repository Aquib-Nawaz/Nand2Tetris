package Parser.SLR;


import Parser.LRBase;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.HashSet;
import java.util.List;

public class SLR extends LRBase {

    public SLR(List<Rule> rules)  {
        super(rules);
        computeFollowSet();
    }

    @Override
    protected LRItemBase getInitialItem() {
        return new SLRItem(rules.size()-1, 0);
    }

    @Override
    public LRItemBase getItemForClosure(LRItemBase item, int ruleNum) {
        return new SLRItem(ruleNum, 0);
    }

    @Override
    public HashSet<LRItemBase> mergeClosureListIntoHashSet(List<LRItemBase> list) {
        return new HashSet<>(list);
    }
}
