package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Instruction.PushInstruction;
import com.nand2tetris.az.Memory.MemoryTranslator;
import junit.framework.TestCase;

public class PushInstructionTest extends TestCase {
    public void testLocal(){
        String code = new PushInstruction("local", 5).writeCode();
        System.out.println(code);
    }

    public void testLoca1(){
        String code = new PushInstruction("local", 1).writeCode();
        System.out.println(code);
    }

    public void testConstant(){
        String code = new PushInstruction("constant", 21).writeCode();
        System.out.println(code);
    }

    public void testTemp(){
        String code = new PushInstruction("temp", 6).writeCode();
        System.out.println(code);
    }

    public void testStatic(){
        MemoryTranslator.getInstance().setFileName("test");
        assertEquals(16, MemoryTranslator.getInstance().setStaticSymbol(6));
        String code = new PushInstruction("static", 6).writeCode();
        System.out.println(code);
    }
}