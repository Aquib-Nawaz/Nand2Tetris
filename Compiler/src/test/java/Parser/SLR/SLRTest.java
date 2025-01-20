package Parser.SLR;

import Parser.Exceptions.ParsingException;
import Parser.Rule;
import Parser.Symbol;
import Parser.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SLRTest {
    private SLR slr;
    @BeforeEach
    protected void setUp() {
        List<Rule> rules = new ArrayList<>();
        Symbol E = new Symbol("E", false), T = new Symbol("T", false);
        Symbol plus = new Symbol("+", true), x = new Symbol("x", true);
        rules.add(new Rule(E, List.of(T, plus,
                E)));
        rules.add(new Rule(E, List.of(T)));
        rules.add(new Rule(T, List.of(x)));
        slr = new SLR(rules);
    }

    public void testCreateParsingTable() throws ParsingException {
        slr.createParisngTable();
        var table = slr.getTable();
        assertEquals(6, table.size());
    }

    @Test
    public void testParse() throws ParsingException {
        Token x = new Token("x"), plus = new Token("+");
        var sentence = List.of(x, plus, x);
        slr.createParisngTable();
        assertTrue(slr.parse(sentence));
        assertFalse(slr.parse(List.of(x, x)));
        assertTrue(slr.parse(List.of(x)));
        assertFalse(slr.parse(List.of(x, plus)));
        assertFalse(slr.parse(List.of(x, plus, x, plus)));

    }

}