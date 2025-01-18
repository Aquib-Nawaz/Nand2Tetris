package Parser.LR0;

import Parser.Exceptions.ShiftReduceException;
import Parser.LRBase;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.List;

public class LR0 extends LRBase {

    public LR0(List<Rule> rules) throws ShiftReduceException {
        super(rules);
    }

    @Override
    protected LRItemBase getInitialItem() {
        return new LR0Item(rules.size() - 1, 0);
    }
}
