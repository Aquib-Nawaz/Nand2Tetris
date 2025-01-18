package Parser.LR0;

import Parser.Exceptions.ShiftReduceException;
import Parser.LRBase;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LR0 extends LRBase {

    public LR0(List<Rule> rules) {
        super(rules);
    }

    @Override
    protected void checkException(HashMap<String, Integer> parsingTableRow,
                                  HashMap<String, Integer> reduceRow, HashSet<LRItemBase> curState)
            throws ShiftReduceException {

        if (!parsingTableRow.isEmpty() && !reduceRow.isEmpty()) {
            throw new ShiftReduceException(curState);
        }
    }

    @Override
    protected LRItemBase getInitialItem() {
        return new LR0Item(rules.size() - 1, 0);
    }


}
