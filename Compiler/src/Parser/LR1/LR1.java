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
    }

    @Override
    protected void checkException(HashMap<String, Integer> parsingTableRow, HashMap<String, Integer> reduceRow,
                                  HashSet<LRItemBase> curState) throws ParsingException {

    }

    @Override
    protected LRItemBase getInitialItem() {
        return null;
    }

    @Override
    public LRItemBase getItemForClosure(LRItemBase item, int ruleNum) {
        return null;
    }
}
