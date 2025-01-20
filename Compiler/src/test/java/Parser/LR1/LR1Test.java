package Parser.LR1;

import Parser.Exceptions.ParsingException;
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

public class LR1Test {

    private LR1 lr1;

    @BeforeEach
    protected void setUp()  {
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


    @Test
    public void testLR1Closure() {
        List<LRItemBase> items = List.of(lr1.getInitialItem());
        var closure = lr1.closure(items);
        assertEquals(6, closure.size());

        assertTrue(closure.contains(new LR1Item(0, 0, new HashSet<>(List.of("$")))));
        assertTrue(closure.contains(new LR1Item(1, 0, new HashSet<>(List.of("$")))));
        assertTrue(closure.contains(new LR1Item(2, 0, new HashSet<>(List.of("$")))));
        assertTrue(closure.contains(new LR1Item(5, 0, new HashSet<>())));
        assertTrue(closure.contains(new LR1Item(3, 0, new HashSet<>(List.of("$", "=")))));
        assertTrue(closure.contains(new LR1Item(4, 0, new HashSet<>(List.of("$", "=")))));
    }


    @Test
    public void testMergeClosureListIntoHashSet() {
        List<LRItemBase> items = new ArrayList<>();
        items.add(new LR1Item(3, 0, new HashSet<>(List.of("$"))));
        items.add(new LR1Item(3, 0, new HashSet<>(List.of("="))));
        items.add(new LR1Item(4, 0, new HashSet<>(List.of("x"))));
        items.add(new LR1Item(4, 0, new HashSet<>(List.of("y"))));

        var merged = lr1.mergeClosureListIntoHashSet(items);

        assertEquals(2, merged.size());
        assertTrue(merged.contains(new LR1Item(3, 0, new HashSet<>(List.of("$", "=")))));
        assertTrue(merged.contains(new LR1Item(4, 0, new HashSet<>(List.of("x", "y")))));
        assertFalse(merged.contains(items.get(1)));
    }


    @Test
    public void testCreateParseTable() throws ParsingException {
        lr1.createParisngTable();
        var table = lr1.getTable();
        assertEquals(14, table.size());
    }


    @Test
    public void testParse() throws ParsingException {
        lr1.createParisngTable();
        Token x = new Token("x"), eq = new Token("="), star = new Token("*");
        assertTrue(lr1.parse(List.of(x)));
        assertTrue(lr1.parse(List.of(star, x, eq, x)));
        assertTrue(lr1.parse(List.of(star, x, eq, star, star, x)));
        assertTrue(lr1.parse(List.of(x, eq, star, x)));

        assertFalse(lr1.parse(List.of(x, eq, star)));
        assertFalse(lr1.parse(List.of(x, eq, x, star)));
    }
}