package Parser.LR0;

import Parser.Exceptions.ParsingException;
import Parser.Exceptions.ShiftReduceException;
import Parser.LRItemBase;
import Parser.Rule;
import Parser.Symbol;
import Parser.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LR0Test {
    private LR0 lr0;

    @BeforeEach
    protected void setUp() {
        List<Rule> rules = new ArrayList<>();
        Symbol S = new Symbol("S", false), L = new Symbol("L", false);
        Symbol x = new Symbol("x", true), lp = new Symbol("(", true),
                rp = new Symbol(")", true), comma = new Symbol(",", true);
        rules.add(new Rule(S, List.of(lp, L,
                rp)));
        rules.add(new Rule(S, List.of(x)));
        rules.add(new Rule(L, List.of(S)));
        rules.add(new Rule(L, List.of(L, comma, S)));
        lr0 = new LR0(rules);
    }

    @Test
    public void testLR0NumNonTerminals() {
        assertEquals(3, lr0.getNumNonTerminals());
    }

    @Test
    public void testLR0Closure() {
        List<LRItemBase> items = List.of(new LR0Item(4 ,0));
        var closure = lr0.closure(items);
        assertEquals(3, closure.size());
        assertTrue(closure.contains(new LR0Item(4, 0)));
        assertTrue(closure.contains(new LR0Item(1, 0)));
        assertTrue(closure.contains(new LR0Item(0, 0)));
    }

    @Test

    public void testGoTo(){
        HashSet<LRItemBase> state = lr0.closure(List.of(new LR0Item(4 ,0)));
        var newState = lr0.goTo(state, new Symbol("x", true));
        assertEquals(1, newState.size());
        assertTrue(newState.contains(new LR0Item(1, 1)));

        newState = lr0.goTo(state, new Symbol("S", false));
        assertEquals(1, newState.size());
        assertTrue(newState.contains(new LR0Item(4, 1)));
        state = lr0.goTo(state, new Symbol("(", true));
        assertEquals(5, state.size());
        newState = lr0.goTo(state, new Symbol("(", true));
        assertEquals(state, newState);
    }

    @Test
    public void testCreateParsingTable() throws ParsingException {
        lr0.createParisngTable();
        var table = lr0.getTable();
        assertEquals(9, table.size());
    }

    @Test
    public void testParse() throws ParsingException {
        Token x = new Token("x"), lp  = new Token("("), rp = new Token(")"),
        comma = new Token(",");
        var sentence = List.of(lp,x,comma,x,rp );
        lr0.createParisngTable();
        assertTrue(lr0.parse(sentence));
        sentence = List.of(lp,lp,x,comma,x,rp);
        assertFalse(lr0.parse(sentence));
        sentence = List.of(lp,x,comma,x,comma,rp);
        assertFalse(lr0.parse(sentence));
    }

}