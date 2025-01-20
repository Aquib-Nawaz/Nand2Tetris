package Parser;

import Parser.Exceptions.ParsingException;
import Parser.Exceptions.ShiftReduceException;
import Parser.LR1.LR1;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrecedenceTest{

    private LR1 lr1;
    protected void setUp1() {
        Symbol E = new Symbol("E", false), plus = new Symbol("+", true),
                x = new Symbol("x", true);

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(E, List.of(E, plus, E)));
        rules.add(new Rule(E, List.of(x)));
        lr1 = new LR1(rules);
    }

    protected void setUp2() {
        Symbol E = new Symbol("E", false), plus = new Symbol("+", true),
                x = new Symbol("x", true), star = new Symbol("*", true);

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(E, List.of(E, plus, E)));
        rules.add(new Rule(E, List.of(E, star, E)));
        rules.add(new Rule(E, List.of(x)));
        lr1 = new LR1(rules);
    }
    @Test
    public void testCreateParsingTable() {
        setUp1();
        assertThrows(ShiftReduceException.class, () -> lr1.createParisngTable());
    }

    @Test
    public void testCreateParsingTable2() throws ParsingException {
        setUp1();
        var prec = new HashMap<String, Integer>();
        prec.put("+",0);
        lr1.setPrecedenceList(prec);
        lr1.createParisngTable();
        prec.put("+",1);
        lr1.setPrecedenceList(prec);
        assertThrows(ShiftReduceException.class, () -> lr1.createParisngTable());
        prec.put("+",2);
        lr1.setPrecedenceList(prec);
        lr1.createParisngTable();
    }

    @Test
    public void testCreateParsingTable3() throws ParsingException {
        setUp2();
        var prec = new HashMap<String, Integer>();
        prec.put("+",0);
        lr1.setPrecedenceList(prec);
        assertThrows(ShiftReduceException.class, () -> lr1.createParisngTable());
        prec.put("*",3);
        lr1.setPrecedenceList(prec);
        lr1.createParisngTable();
        var table = lr1.getTable();
        assertEquals(7, table.size());
    }

}
