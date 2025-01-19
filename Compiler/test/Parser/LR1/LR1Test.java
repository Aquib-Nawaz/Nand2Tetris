package Parser.LR1;

import Parser.LR0.LR0;
import Parser.LR0.LR0Item;
import Parser.LRItemBase;
import Parser.Rule;
import Parser.Symbol;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class LR1Test extends TestCase {

    private LR1 lr1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<Rule> rules = new ArrayList<>();
        Symbol S = new Symbol("S", false), V = new Symbol("V", false),
        E = new Symbol("E", false);
        Symbol x = new Symbol("x", true), eq = new Symbol("=", true),
                star = new Symbol("*", true);
        rules.add(new Rule(S, List.of(V, eq,
                E)));
        rules.add(new Rule(S, List.of(E)));
        rules.add(new Rule(E, List.of(V)));
        rules.add(new Rule(V, List.of(x)));
        rules.add(new Rule(V, List.of(star, E)));
        lr1 = new LR1(rules);
    }

    public void testLR1Closure() {
        List<LRItemBase> items = List.of(lr1.getInitialItem());
        var closure = lr1.closure(items);
        assertEquals(6, closure.size());
    }

}