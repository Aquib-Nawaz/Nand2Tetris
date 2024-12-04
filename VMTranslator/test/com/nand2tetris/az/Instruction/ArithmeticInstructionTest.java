package com.nand2tetris.az.Instruction;
import com.nand2tetris.az.Instruction.ArithmeticInstruction;
import junit.framework.TestCase;

public class ArithmeticInstructionTest extends TestCase {
    public void testAdd(){
        String code = new ArithmeticInstruction("add").writeCode();
        System.out.println(code);
    }

    public void testSub(){
        String code = new ArithmeticInstruction("sub").writeCode();
        System.out.println(code);
    }

    public void testNegative(){
        String code = new ArithmeticInstruction("neg").writeCode();
        System.out.println(code);
    }

    public void testEquality(){
        String code = new ArithmeticInstruction("eq").writeCode();
        System.out.println(code);
    }

    public void testGreaterThan(){
        String code = new ArithmeticInstruction("gt").writeCode();
        System.out.println(code);
    }
}