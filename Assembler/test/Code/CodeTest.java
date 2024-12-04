package Code;

import Exceptions.ParseError;
import junit.framework.TestCase;

import static org.junit.Assert.assertThrows;

public class CodeTest extends TestCase {

    public void testDest() {
        assertEquals("111", Code.dest("AMD"));
        assertThrows(ParseError.class, () -> Code.dest("C"));
    }

    public void testJump() {
        assertEquals("111", Code.jump("JMP"));
        assertThrows(ParseError.class, () -> Code.jump("C"));
    }

    public void testComp() {
        assertEquals("0101010", Code.comp("0"));
        assertThrows(ParseError.class, () -> Code.comp("C"));
    }

}