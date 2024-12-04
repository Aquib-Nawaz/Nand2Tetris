package com.nand2tetris.az.Instruction;


import com.nand2tetris.az.Memory.MemoryTranslator;

import java.util.Objects;

public class ArithmeticInstruction implements Instruction {

    private final String arg;
    private final boolean unaryInstruction;
    private final boolean relationalInstruction;

    private final MemoryTranslator memoryTranslator;

    ArithmeticInstruction(String command){
        this.arg = command;
        unaryInstruction = arg.equals("not") || arg.equals("neg");
        relationalInstruction = arg.equals("eq") || arg.equals("gt") || arg.equals("lt");
        memoryTranslator = MemoryTranslator.getInstance();
    }

    @Override
    public String arg1() {
        return arg;
    }

    @Override
    public int arg2() {
        throw new RuntimeException("Method Not Supported");
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_ARITHMETIC;
    }
    private char arithmeticSymbol(){
        return switch (arg) {
            case "add" -> '+';
            case "sub", "neg" -> '-';
            case "and" -> '&';
            case "or" -> '|';
            case "not" -> '!';
            default -> throw new IllegalArgumentException(arg);
        };
    }
    @Override
    public String writeCode() {
        //M[M[SP]-2] = M[M[SP]-1]+M[M[SP]-2]; M[SP]=M[SP]-1
        String line="@SP\n";

        if(unaryInstruction){
            line+="A=M-1\n";            //arg2 addr
            line+="M="+arithmeticSymbol()+"M\n";
        }
        else{
            line+="AM=M-1\n";

            line+="D=M\n";
            line+="A=A-1\n";

            if(Objects.equals(arg, "sub")){
                line+="M=M"+arithmeticSymbol()+"D\n";
            }
            else if(!relationalInstruction){
                line+="M=D"+arithmeticSymbol()+"M\n";
            }
            else{
                line+="D=M-D\n";
                String loop = memoryTranslator.getNextLoopAddr();
                line+="M=-1\n";
                line+="@"+loop+"\n";
                line+="D;J"+arg.toUpperCase()+"\n";
                line+="@SP\n";
                line+="A=M-1\n";
                line+="M=0\n";
                line+="("+loop+")\n";
            }

        }
        return line;
    }
}
