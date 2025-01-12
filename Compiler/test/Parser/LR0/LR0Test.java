package Parser.LR0;

import Parser.NonTerminal;
import Parser.Rule;
import Parser.Terminal;
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
        Token S = new NonTerminal("S"), L = new NonTerminal("L");

        rules.add(new Rule(S, List.of(new Terminal("("), L,
                new Terminal(")"))));
        rules.add(new Rule(S, List.of(new Terminal("x"))));
        rules.add(new Rule(L, List.of(S)));
        rules.add(new Rule(L, List.of(L, new Terminal(","), S)));
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
        var newState = lr0.goTo(state, new Terminal("x"));
        assertEquals(1, newState.size());
        assertTrue(newState.contains(new LR0Item(1, 1)));

        newState = lr0.goTo(state, new NonTerminal("S"));
        assertEquals(1, newState.size());
        assertTrue(newState.contains(new LR0Item(4, 1)));
        state = lr0.goTo(state, new Terminal("("));
        assertEquals(5, state.size());
        newState = lr0.goTo(state, new Terminal("("));
        assertEquals(state, newState);
    }

    public void testCreateParsingTable()  {
        var table = lr0.getTable();
        assertEquals(9, table.size());
    }

    public void testParse()  {
        Token x = new Terminal("x"), lp  = new Terminal("("), rp = new Terminal(")"),
        comma = new Terminal(",");
        var sentence = List.of(lp,x,comma,x,rp);
        assertTrue(lr0.parse(sentence));
        sentence = List.of(lp,lp,x,comma,x,rp);
        assertFalse(lr0.parse(sentence));
        sentence = List.of(lp,x,comma,x,comma,rp);
        assertFalse(lr0.parse(sentence));
    }

}