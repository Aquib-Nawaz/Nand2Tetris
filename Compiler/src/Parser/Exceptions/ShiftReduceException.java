package Parser.Exceptions;

import Parser.LR0.LR0Item;

import java.util.HashSet;

public class ShiftReduceException extends Exception {

    public ShiftReduceException(HashSet<LR0Item> state) {
        super("Shift Reduce Exception: " + state);
    }
}
