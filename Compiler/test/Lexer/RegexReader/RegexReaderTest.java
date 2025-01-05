package Lexer.RegexReader;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;


public class RegexReaderTest {
    @Test
    public void testRegexReaderSimple() {

        RegexReader reader = new RegexReader("abcd");
        NFA nfa = reader.getNFA();
        assertEquals(0, nfa.match("abcd"));
        assertEquals(-1, nfa.match("abc"));
        assertEquals(-1, nfa.match("abce"));
    }

    @Test
    public void testRegexReaderWithStar() {

        RegexReader reader = new RegexReader("abc*");
        NFA nfa = reader.getNFA();
        assertEquals(0, nfa.match("abc"));
        assertEquals(0, nfa.match("abcc"));
        assertEquals(0, nfa.match("ab"));
        assertEquals(-1, nfa.match("abcd"));
        assertEquals(-1, nfa.match("a"));
        reader = new RegexReader("a*");
        nfa = reader.getNFA();
        assertEquals(0, nfa.match("a"));
        assertEquals(0, nfa.match("aa"));
        assertEquals(-1, nfa.match("ab"));
        assertEquals(0, nfa.match(""));
    }

    @Test
    public void testRegexReaderWithOr() {
        RegexReader reader = new RegexReader("ab|cd");
        NFA nfa = reader.getNFA();
        assertEquals(0, nfa.match("ab"));
        assertEquals(0, nfa.match("cd"));
        assertEquals(-1, nfa.match("abcd"));

        reader = new RegexReader("a*df|b*th");
        nfa = reader.getNFA();
        assertEquals(0, nfa.match("adf"));
        assertEquals(0, nfa.match("bth"));
        assertEquals(0, nfa.match("df"));
        assertEquals(0, nfa.match("aaaaaaaadf"));
        assertEquals(0, nfa.match("th"));
        assertEquals(-1, nfa.match("abth"));
        assertEquals(-1, nfa.match("fth"));
    }
}