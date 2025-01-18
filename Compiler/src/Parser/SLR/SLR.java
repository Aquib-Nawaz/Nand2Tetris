package Parser.SLR;

import Parser.Exceptions.ParsingException;
import Parser.Exceptions.ShiftReduceException;
import Parser.LRBase;
import Parser.LRItemBase;
import Parser.Rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SLR extends LRBase {

    public SLR(List<Rule> rules) throws  ShiftReduceException {
        super(rules);
    }

    @Override
    protected void checkException(HashMap<String, Integer> parsingTableRow, HashMap<String, Integer> reduceRow, HashSet<LRItemBase> curState)
            throws ParsingException {

    }

    @Override
    protected LRItemBase getInitialItem() {
        return new LRItemBase(rules.size()-1, 0);
    }
}
