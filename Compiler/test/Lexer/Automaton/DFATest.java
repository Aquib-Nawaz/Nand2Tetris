package Lexer.Automaton;

import Lexer.RegexReader.RegexReader;
import org.junit.Test;

import static org.junit.Assert.*;


public class DFATest {
    @Test
    public void testDFASimple() {

        RegexReader reader = new RegexReader();
        reader.addRegex("abcd",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("abcd"));
        assertEquals(-1, dfa.match("abc"));
        assertEquals(-1, dfa.match("abce"));
    }

    @Test
    public void testRegexReaderWithStar() {
        RegexReader reader = new RegexReader();
        reader.addRegex("abc*",0);
        reader.addRegex("a*",1);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("abc"));
        assertEquals(0, dfa.match("abcc"));
        assertEquals(0, dfa.match("ab"));
        assertEquals(-1, dfa.match("abcd"));
        assertEquals(1, dfa.match("a"));
        assertEquals(1, dfa.match("a"));
        assertEquals(1, dfa.match("aa"));
        assertEquals(0, dfa.match("ab"));
        assertEquals(1, dfa.match(""));
    }

    @Test
    public void testRegexReaderWithOr() {
        RegexReader reader = new RegexReader();
        reader.addRegex("ab|cd",0);
        reader.addRegex("a*df|b*th",1);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("ab"));
        assertEquals(0, dfa.match("cd"));
        assertEquals(-1, dfa.match("abcd"));

        assertEquals(1, dfa.match("adf"));
        assertEquals(1, dfa.match("bth"));
        assertEquals(1, dfa.match("th"));
        assertEquals(1, dfa.match("bbbbth"));
        assertEquals(1, dfa.match("df"));
        assertEquals(1, dfa.match("aaaaaaaadf"));
        assertEquals(1, dfa.match("bbbbbbth"));
        assertEquals(1, dfa.match("th"));
        assertEquals(-1, dfa.match("abth"));
        assertEquals(-1, dfa.match("fth"));
    }

    @Test
    public void testRegexReaderWithBrackets() {
        RegexReader reader = new RegexReader();
        reader.addRegex("(ab|cd)",0);
        reader.addRegex("(ab)*",1);
        reader.addRegex("(ab|cd)*",2);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("ab"));
        assertEquals(0, dfa.match("cd"));
        assertEquals(2, dfa.match("abcd"));
        assertEquals(-1, dfa.match("a"));
        assertEquals(-1, dfa.match("bc"));
        assertEquals(-1, dfa.match("c"));

        assertEquals(0, dfa.match("ab"));
        assertEquals(1, dfa.match("abab"));
        assertEquals(0, dfa.match("cd"));
        assertEquals(2, dfa.match("abcdab"));
        assertEquals(2, dfa.match("cdabcdcd"));
        assertEquals(-1, dfa.match("abc"));
        assertEquals(-1, dfa.match("abcda"));
    }

    @Test
    public void testRegexReaderWithBrackets2() {
        RegexReader reader = new RegexReader();
        reader.addRegex("(ab*df*|ce*g)*",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("abdf"));
        assertEquals(0, dfa.match("ad"));
        assertEquals(0, dfa.match("adffff"));
        assertEquals(0, dfa.match("ceg"));
        assertEquals(0, dfa.match("abdfceg"));
        assertEquals(0, dfa.match("abdfcegabdfceg"));
        assertEquals(-1, dfa.match("abdfcegabdfcegabdfce"));
    }

    @Test
    public void testRegexReaderWithBrackets3() {
        RegexReader reader = new RegexReader();
        reader.addRegex("ab(c|df)",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("abc"));
        assertEquals(0, dfa.match("abdf"));
    }

    @Test
    public void testRegexReaderWithBrackets4() {
        RegexReader reader = new RegexReader();
        reader.addRegex("ab(c|df)*",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("abc"));
        assertEquals(0, dfa.match("abdf"));
        assertEquals(0, dfa.match("abccccdf"));
        assertEquals(0, dfa.match("abdfcdfcc"));
        assertEquals(-1, dfa.match("abf"));
        assertEquals(-1, dfa.match("abd"));
    }

    @Test
    public void testRegexReaderWithBrackets5() {
        RegexReader reader = new RegexReader();
        reader.addRegex("ab(c|(d*|f))(g|h)*",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("abc"));
        assertEquals(0, dfa.match("abch"));
        assertEquals(-1, dfa.match("abccccdf"));

        assertEquals(0, dfa.match("abfggghghh"));
        assertEquals(0, dfa.match("abd"));
    }

    @Test
    public void testRegexReaderWithQuestionMark() {
        RegexReader reader = new RegexReader();
        reader.addRegex("ab?cd",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("acd"));
        assertEquals(0, dfa.match("abcd"));
        assertEquals(-1, dfa.match("abbcd"));
        assertEquals(-1, dfa.match("bcd"));
    }

    @Test
    public void testRegexReaderWithDot() {
        RegexReader reader = new RegexReader();
        reader.addRegex(".",0);
        reader.addRegex("a.",1);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("a"));
        assertEquals(0, dfa.match("b"));
        assertEquals(0, dfa.match("c"));
        assertEquals(0, dfa.match("d"));
        assertEquals(1, dfa.match("ab"));
        assertEquals(1, dfa.match("aa"));
        assertEquals(1, dfa.match("ab"));
        assertEquals(0, dfa.match("b"));
        assertEquals(-1, dfa.match("a\n"));
    }

    @Test
    public void testRegexReaderWithDotStar() {
        RegexReader reader = new RegexReader();
        reader.addRegex(".*",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match(""));
        assertEquals(0, dfa.match("a"));
        assertEquals(0, dfa.match("ab"));
        assertEquals(0, dfa.match("abc"));
        assertEquals(0, dfa.match("abcd"));
        assertEquals(0, dfa.match("abcde"));
        assertEquals(0, dfa.match("b"));
        assertEquals(0, dfa.match("ababab"));
    }

    @Test
    public void testRegexReaderWithBigBracket() {
        RegexReader reader = new RegexReader();
        reader.addRegex("[b-z]*",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("b"));
        assertEquals(0, dfa.match("zhsdkfjsdlkbk"));
        assertEquals(-1, dfa.match("a"));
        assertEquals(-1, dfa.match("hdslhflkadhsfkhafdfs"));
    }

    @Test
    public void testRegexReaderWithBigBracket2() {
        RegexReader reader = new RegexReader();
        reader.addRegex("[_a-zA-Z][0-9_a-zA-Z]*",0);
        var dfa = reader.getDFA();
        assertEquals(-1, dfa.match("1dsahldhh"));
        assertEquals(-1, dfa.match("046"));
        assertEquals(0, dfa.match("_121312Agska_dl123"));
        assertEquals(0, dfa.match("_"));
    }

    @Test
    public void testRegexReaderWithBigBracket3() {
        RegexReader reader = new RegexReader();
        reader.addRegex("[^a-z]*",0);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("13123"));
        assertEquals(-1, dfa.match("a"));
    }

    @Test
    public void testMultipleRule(){
        RegexReader reader = new RegexReader();
        reader.addRegex("if",  0);
        reader.addRegex(".*",  2);
        reader.addRegex("[_a-zA-Z][_a-zA-Z0-9]*",1);
        var dfa = reader.getDFA();
        assertEquals(0, dfa.match("if"));
        assertEquals(1, dfa.match("if_121312Agska_dl123"));
        assertEquals(1, dfa.match("if_"));
        assertEquals(2, dfa.match("1if"));
    }
}