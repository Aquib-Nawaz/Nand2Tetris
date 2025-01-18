package Parser.Exceptions;

import Parser.LRItemBase;

import java.util.HashSet;

public class ShiftReduceException extends ParsingException {

    public ShiftReduceException(HashSet<LRItemBase> curState) {
        super("Shift Reduce Exception: " + curState);
    }
}
