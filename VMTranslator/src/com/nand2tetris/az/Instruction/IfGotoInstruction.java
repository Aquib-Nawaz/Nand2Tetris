package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

public class IfGotoInstruction implements Instruction {
    private final String keyWord;
    private final MemoryTranslator memoryTranslator;
    public IfGotoInstruction(String keyWord) {
        memoryTranslator = MemoryTranslator.getInstance();
        this.keyWord = keyWord;
    }

    @Override
    public String arg1() {
        return keyWord;
    }

    @Override
    public int arg2() {
        throw new RuntimeException("Method Not Supported");
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_IF;
    }

    @Override
    public String writeCode() {
        return "@SP\nAM=M-1\nD=M\n"+
                "@"+memoryTranslator.translateLabelSymbol(keyWord)+"\nD;JNE\n";
    }
}
