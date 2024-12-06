package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

public class CallInstruction implements Instruction {
    private final String calleeName;
    private final int nArgs;
    private static int calleeCount = 0;
    private final MemoryTranslator memoryTranslator;
    public CallInstruction(String callee, int nArgs) {
        this.calleeName = callee;
        this.nArgs = nArgs;
        memoryTranslator = MemoryTranslator.getInstance();
    }

    @Override
    public String arg1() {
        return calleeName;
    }

    @Override
    public int arg2() {
        return nArgs;
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_CALL;
    }

    @Override
    public String writeCode() {
        String returnSymbol = memoryTranslator
                .translateLabelSymbol("ret." + memoryTranslator.getAndIncrementCalleeCount());

        StringBuilder line = new StringBuilder();
        line.append("@").append(returnSymbol).append("\n");
        pushReturnAddress(line);
        pushSpecialRegister(line, "LCL");
        pushSpecialRegister(line, "ARG");
        pushSpecialRegister(line, "THIS");
        pushSpecialRegister(line, "THAT");

        updateARG(line);
        updateLCL(line);
        line.append("@").append(calleeName).append("\n");
        line.append("0;JMP\n");
        line.append("(").append(returnSymbol).append(")\n");
        return line.toString();
    }

    private static void updateLCL(StringBuilder line) {
        line.append("@SP\n");
        line.append("D=M\n");
        line.append("@LCL\n");
        line.append("M=D\n");
    }

    private void updateARG(StringBuilder line) {
        line.append("@").append(nArgs+5).append("\n");
        line.append("D=A\n");
        line.append("@SP\n");
        line.append("M=M+1\n");
        line.append("D=M-D\n");
        line.append("@ARG\n");
        line.append("M=D\n");
    }

    private static void pushSpecialRegister(StringBuilder line, String str) {
        line.append("@").append(str).append("\n");
        line.append("D=M\n");
        line.append("@SP\n");
        line.append("AM=M+1\n");
        line.append("M=D\n");
    }

    private static void pushReturnAddress(StringBuilder line) {
        line.append("D=A\n");
        line.append("@SP\n");
        line.append("A=M\n");
        line.append("M=D\n");
    }
}
