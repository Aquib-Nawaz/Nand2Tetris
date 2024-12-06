package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

public class GotoInstruction implements Instruction {
    private final String keyWord;
    private final MemoryTranslator memoryTranslator;
    public GotoInstruction(String keyWord) {
        this.keyWord = keyWord;
        memoryTranslator = MemoryTranslator.getInstance();
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
        return InstructionType.C_GOTO;
    }

    @Override
    public String writeCode() {
        return "@"+memoryTranslator.translateLabelSymbol(keyWord)+"\n0;JMP\n";
    }
}
