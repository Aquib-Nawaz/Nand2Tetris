package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

public class FunctionInstruction implements Instruction {
    private final String name;
    private final int nVars;
    public FunctionInstruction(String name, int nVars) {
        this.name = name;
        this.nVars = nVars;
    }

    @Override
    public String arg1() {
        return name;
    }

    @Override
    public int arg2() {
        return nVars;
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_FUNCTION;
    }

    @Override
    public String writeCode() {
        //Initialize local variables

        StringBuilder line = new StringBuilder();
        line.append("(").append(name).append(")\n");
        if(nVars>0){
            pushArguments(line);
        }
        MemoryTranslator.getInstance().setFunctionName(name);
        return line.toString();
    }

    private void pushArguments(StringBuilder line) {
        line.append("@").append(nVars).append("\n");
        line.append("D=A\n");
        line.append("@SP\n");
        line.append("A=M\n");
        for (int i=0;;i++){
            line.append("M=0\n");
            if(i==nVars-1){
                break;
            }
            line.append("A=A+1\n");
        }
        line.append("@SP\n");
        line.append("M=D+M\n");
    }
}
