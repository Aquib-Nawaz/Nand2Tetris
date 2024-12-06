package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

public class LabelInstruction implements Instruction {
    private final String keyWord;
    private final MemoryTranslator memoryTranslator;;
    public LabelInstruction(String keyWord) {
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
        return InstructionType.C_LABEL;
    }

    @Override
    public String writeCode() {
        return "("+memoryTranslator.translateLabelSymbol(keyWord)+")\n";
    }
}
