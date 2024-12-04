package Code;

import junit.framework.TestCase;

import static org.junit.Assert.assertThrows;

public class AInstructionTest extends TestCase {
    public void testGetCodeForNumber(){
        AInstruction aInstruction = new AInstruction();
        assertEquals("0000000000001110", aInstruction.getCode("14"));
    }

    public void testGetCodeForSymbol(){
        AInstruction aInstruction = new AInstruction();
        assertEquals("0000000000010000", aInstruction.getCode("i"));
    }

    public void testPreDefinedSymbol(){
        AInstruction aInstruction = new AInstruction();
        assertEquals("0000000000001101", aInstruction.getCode("R13"));
    }

    public void testInvalidSymbol(){
        AInstruction aInstruction = new AInstruction();
        assertThrows(RuntimeException.class, ()->aInstruction.getCode("1aqwda"));
    }
}