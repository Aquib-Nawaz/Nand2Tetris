package com.nand2tetris.az.Instruction;

public interface Instruction {
    String arg1();
    int arg2();
    InstructionType type();

    String writeCode();
}
