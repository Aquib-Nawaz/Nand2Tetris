package Parser.LR0;

import Parser.Rule;
import Parser.Symbol;
import Parser.Token;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LR0Test extends TestCase {
    private LR0 lr0;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
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
    public void testLR0NumNonTerminals() {
        assertEquals(3, lr0.getNumNonTerminals());
    }

    public void testLR0Closure() {
        List<LR0Item> items = List.of(new LR0Item(4 ,0));
        var closure = lr0.closure(items);
        assertEquals(3, closure.size());
        assertTrue(closure.contains(new LR0Item(4, 0)));
        assertTrue(closure.contains(new LR0Item(1, 0)));
        assertTrue(closure.contains(new LR0Item(0, 0)));
    }

    public void testGoTo(){
        HashSet<LR0Item> state = lr0.closure(List.of(new LR0Item(4 ,0)));
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

    public void testCreateParsingTable()  {
        var table = lr0.getTable();
        assertEquals(9, table.size());
    }

    public void testParse()  {
        Token x = new Token("x"), lp  = new Token("("), rp = new Token(")"),
        comma = new Token(",");
        var sentence = List.of(lp,x,comma,x,rp);
        assertTrue(lr0.parse(sentence));
        sentence = List.of(lp,lp,x,comma,x,rp);
        assertFalse(lr0.parse(sentence));
        sentence = List.of(lp,x,comma,x,comma,rp);
        assertFalse(lr0.parse(sentence));
    }

}