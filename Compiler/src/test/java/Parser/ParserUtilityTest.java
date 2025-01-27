package Parser;


import javolution.io.Union;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserUtilityTest {
    List<Rule>rules;
    @BeforeEach
    protected void setUp()  {
        rules = new ArrayList<>();
        Symbol S = new Symbol("S", false), L = new Symbol("L", false), L1 =
                new Symbol("L'", false), S1 = new Symbol("S'", false);
        Symbol x = new Symbol("x", true), lp = new Symbol("(", true),
                rp = new Symbol(")", true), comma = new Symbol(",", true);

        S1.setId(0); S.setId(1); L.setId(2); L1.setId(3);

        rules.add(new Rule(S1, List.of(S, new Symbol("$", true))));
        rules.add(new Rule(S, List.of(lp, L, rp)));
        rules.add(new Rule(S, List.of(x)));
        rules.add(new Rule(L, List.of(S, L1)));
        rules.add(new Rule(L, List.of(S)));
        rules.add(new Rule(L1, List.of( comma, S, L1)));
        rules.add(new Rule(L1, List.of( comma, S)));
    }
    @Test

    public void testGetFirstSet() {
        int nonTerminals = 4;
        var firstSet = ParserUtility.getFirstSet(rules, nonTerminals);
        assertEquals(firstSet.get(0), firstSet.get(1));
        assertEquals(firstSet.get(0), firstSet.get(2));
        assertEquals(firstSet.getFirst().size(), 2);
        assertTrue(firstSet.getFirst().contains("x"));
        assertTrue(firstSet.getFirst().contains("("));
        assertTrue(firstSet.get(3).contains(","));
        assertEquals(firstSet.get(3).size(), 1);
    }

    @Test
    public void testGetFollowSet() {
        int nonTerminals = 4;
        var ruleMap = ParserUtility.initializeRuleMap(rules, nonTerminals);
        var firstSet = ParserUtility.getFirstSet(rules, nonTerminals);
        var followSet = ParserUtility.getFollowSet(rules, ruleMap, nonTerminals, firstSet);
        assertTrue(followSet.getFirst().isEmpty());
        assertEquals(followSet.get(1).size(), 3);
        assertTrue(followSet.get(1).contains("$"));
        assertTrue(followSet.get(1).contains(","));
        assertTrue(followSet.get(1).contains(")"));
        assertTrue(followSet.get(3).contains(")"));
        assertEquals(followSet.get(3).size(), 1);
        assertEquals(followSet.get(2), followSet.get(3));
    }
    class Value extends Union{
        Signed32 asInt = new Signed32();
        String asString = "";
    }

    @Test
    public void testUnion() {
        Value v = new Value();
        v.asInt.set(10);
        assertEquals(10, v.asInt.get());

        v.asString = "hello";
        assertEquals("hello", v.asString);
    }
}