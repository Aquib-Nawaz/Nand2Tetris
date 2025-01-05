package Lexer.RegexReader;

import org.junit.Test;

import static org.junit.Assert.*;


public class RegexReaderTest {
    @Test
    public void testRegexReaderSimple() {

        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("abcd",0);
        assertEquals(0, nfa.match("abcd"));
        assertEquals(-1, nfa.match("abc"));
        assertEquals(-1, nfa.match("abce"));
    }

    @Test
    public void testRegexReaderWithStar() {

        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("abc*",0);
        assertEquals(0, nfa.match("abc"));
        assertEquals(0, nfa.match("abcc"));
        assertEquals(0, nfa.match("ab"));
        assertEquals(-1, nfa.match("abcd"));
        assertEquals(-1, nfa.match("a"));
        reader = new RegexReader();
        nfa = reader.addRegex("a*",0);
        assertEquals(0, nfa.match("a"));
        assertEquals(0, nfa.match("aa"));
        assertEquals(-1, nfa.match("ab"));
        assertEquals(0, nfa.match(""));
    }

    @Test
    public void testRegexReaderWithOr() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("ab|cd",0);
        assertEquals(0, nfa.match("ab"));
        assertEquals(0, nfa.match("cd"));
        assertEquals(-1, nfa.match("abcd"));

        reader = new RegexReader();
        nfa = reader.addRegex("a*df|b*th",0);
        assertEquals(0, nfa.match("adf"));
        assertEquals(0, nfa.match("bth"));
        assertEquals(0, nfa.match("th"));
        assertEquals(0, nfa.match("bbbbth"));
        assertEquals(0, nfa.match("df"));
        assertEquals(0, nfa.match("aaaaaaaadf"));
        assertEquals(0, nfa.match("bbbbbbth"));
        assertEquals(0, nfa.match("th"));
        assertEquals(-1, nfa.match("abth"));
        assertEquals(-1, nfa.match("fth"));
    }

    @Test
    public void testRegexReaderWithBrackets() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("(ab|cd)",0);
        assertEquals(0, nfa.match("ab"));
        assertEquals(0, nfa.match("cd"));
        assertEquals(-1, nfa.match("abcd"));
        assertEquals(-1, nfa.match("a"));
        assertEquals(-1, nfa.match("bc"));
        assertEquals(-1, nfa.match("c"));

        reader = new RegexReader();
        nfa = reader.addRegex("(ab)*",0);
        assertEquals(0, nfa.match("ab"));
        assertEquals(0, nfa.match("abab"));
        reader = new RegexReader();
        nfa = reader.addRegex("(ab|cd)*",0);
        assertEquals(0, nfa.match("cd"));
        assertEquals(0, nfa.match("abcdab"));
        assertEquals(0, nfa.match("cdabcdcd"));
        assertEquals(-1, nfa.match("abc"));
        assertEquals(-1, nfa.match("abcda"));
    }

    @Test
    public void testRegexReaderWithBrackets2() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("(ab*df*|ce*g)*",0);
        assertEquals(0, nfa.match("abdf"));
        assertEquals(0, nfa.match("ad"));
        assertEquals(0, nfa.match("adffff"));
        assertEquals(0, nfa.match("ceg"));
        assertEquals(0, nfa.match("abdfceg"));
        assertEquals(0, nfa.match("abdfcegabdfceg"));
        assertEquals(-1, nfa.match("abdfcegabdfcegabdfce"));
    }

    @Test
    public void testRegexReaderWithBrackets3() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("ab(c|df)",0);
        assertEquals(0, nfa.match("abc"));
        assertEquals(0, nfa.match("abdf"));
    }

    @Test
    public void testRegexReaderWithBrackets4() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("ab(c|df)*",0);
        assertEquals(0, nfa.match("abc"));
        assertEquals(0, nfa.match("abdf"));
        assertEquals(0, nfa.match("abccccdf"));
        assertEquals(0, nfa.match("abdfcdfcc"));
        assertEquals(-1, nfa.match("abf"));
        assertEquals(-1, nfa.match("abd"));
    }

    @Test
    public void testRegexReaderWithBrackets5() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("ab(c|(d*|f))(g|h)*",0);
        assertEquals(0, nfa.match("abc"));
        assertEquals(0, nfa.match("abch"));
        assertEquals(-1, nfa.match("abccccdf"));

        assertEquals(0, nfa.match("abfggghghh"));
        assertEquals(0, nfa.match("abd"));
    }

    @Test
    public void testRegexReaderWithQuestionMark() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("ab?cd",0);
        assertEquals(0, nfa.match("acd"));
        assertEquals(0, nfa.match("abcd"));
        assertEquals(-1, nfa.match("abbcd"));
        assertEquals(-1, nfa.match("bcd"));
    }

    @Test
    public void testRegexReaderWithDot() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex(".",0);
        assertEquals(0, nfa.match("a"));
        assertEquals(0, nfa.match("b"));
        assertEquals(0, nfa.match("c"));
        assertEquals(0, nfa.match("d"));
        assertEquals(-1, nfa.match("ab"));
        reader = new RegexReader();
        nfa = reader.addRegex("a.",0);
        assertEquals(0, nfa.match("aa"));
        assertEquals(0, nfa.match("ab"));
        assertEquals(-1, nfa.match("b"));
        assertEquals(-1, nfa.match("a\n"));
    }

    @Test
    public void testRegexReaderWithDotStar() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex(".*",0);
        assertEquals(0, nfa.match(""));
        assertEquals(0, nfa.match("a"));
        assertEquals(0, nfa.match("ab"));
        assertEquals(0, nfa.match("abc"));
        assertEquals(0, nfa.match("abcd"));
        assertEquals(0, nfa.match("abcde"));
        assertEquals(0, nfa.match("b"));
        assertEquals(0, nfa.match("ababab"));
    }

    @Test
    public void testRegexReaderWithBigBracket() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("[b-z]*",0);
        assertEquals(0, nfa.match("b"));
        assertEquals(0, nfa.match("zhsdkfjsdlkbk"));
        assertEquals(-1, nfa.match("a"));
        assertEquals(-1, nfa.match("hdslhflkadhsfkhafdfs"));
    }

    @Test
    public void testRegexReaderWithBigBracket2() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("[_a-zA-Z][0-9_a-zA-Z]*",0);
        assertEquals(-1, nfa.match("1dsahldhh"));
        assertEquals(-1, nfa.match("046"));
        assertEquals(0, nfa.match("_121312Agska_dl123"));
        assertEquals(0, nfa.match("_"));
    }

    @Test
    public void testRegexReaderWithBigBracket3() {
        RegexReader reader = new RegexReader();
        NFA nfa = reader.addRegex("[^a-z]*",0);
        assertEquals(0, nfa.match("13123"));
        assertEquals(-1, nfa.match("a"));
    }
}