package com.nand2tetris.az.Instruction;

import junit.framework.TestCase;

public class PopInstructionTest extends TestCase {

    public void testLocal1(){
        String code = new PopInstruction("local", 1).writeCode();
        System.out.println(code);
    }
    public void testLocal0(){
        String code = new PopInstruction("local", 0).writeCode();
        System.out.println(code);
    }

    public void testLocal8(){
        String code = new PopInstruction("local", 8).writeCode();
        System.out.println(code);
    }
}