package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

import java.util.List;
import java.util.Objects;

public class PushInstruction implements Instruction {
    private final MemoryTranslator memoryTranslator;
    private final String arg1;
    private final int arg2;
    PushInstruction(String arg1, int arg2){
        this.arg1 = arg1;
        this.arg2 = arg2;
        memoryTranslator = MemoryTranslator.getInstance();
    }

    @Override
    public String arg1() {
        return arg1;
    }

    @Override
    public int arg2() {
        return arg2;
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_PUSH;
    }

    @Override
    public String writeCode(){
        //
        String line = getValueInDReg();
        line+="@SP\n";
        line+="M=M+1\n";
        line+="A=M-1\n";
        line+="M=D\n";
        return line;
    }

    private String getValueInDReg() {
        String line= "";
        if (memoryTranslator.MemoryType1.contains(arg1)) {
            line += loadIndexAddressOfSegment();
        }
        else if(memoryTranslator.MemoryType2.contains(arg1)){
            line+="@"+memoryTranslator.translateSymbolToAddress(arg1, arg2)+"\n";
            line+="D=M\n";
        }
        else if(Objects.equals(arg1, "constant")){
            line+="@"+arg2+"\n";
            line+="D=A\n";
        }
        else if(Objects.equals(arg1, "static")){
            line+="@"+memoryTranslator.getStaticSymbolAddr(arg2)+"\n";
            line+="D=M\n";
        }
        else {
            throw new IllegalArgumentException(arg1);
        }
        return line;
    }

    private String loadIndexAddressOfSegment() {
        String line="";
        if(arg2<=2){
            line+="@"+memoryTranslator.translateVMSymbolToHackSymbol(arg1)+"\n";
            line+="A=M"+(arg2==0?"":"+1")+"\n";
            if(arg2==2){
                line+="A=A+1\n";
            }
            line+="D=M\n";
            return line;
        }
        else{
            line +="@"+arg2+"\n";
            line +="D=A\n";
            line +="@"+memoryTranslator.translateVMSymbolToHackSymbol(arg1)+"\n";
            line +="A=D+M\n";
            line +="D=M\n";
        }
        return line;
    }
}
