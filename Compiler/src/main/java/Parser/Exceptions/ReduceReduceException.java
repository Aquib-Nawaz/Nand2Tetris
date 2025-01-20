package Parser.Exceptions;

import Parser.LRItemBase;

import java.util.HashSet;

public class ReduceReduceException extends ParsingException {

    public ReduceReduceException(HashSet<LRItemBase> curState) {
        super("Reduce Reduce Exception: " + curState);
    }

}
