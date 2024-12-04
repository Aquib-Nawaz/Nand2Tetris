package com.nand2tetris.az.Instruction;

import com.nand2tetris.az.Memory.MemoryTranslator;

public class PopInstruction implements Instruction {
    private final String arg1;
    private final int arg2;
    private final MemoryTranslator memoryTranslator;

    public PopInstruction(String segment, int index) {
        arg1 = segment;
        arg2 = index;
        memoryTranslator = MemoryTranslator.getInstance();
    }

    @Override
    public String arg1() {
        return null;
    }

    @Override
    public int arg2() {
        return 0;
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_POP;
    }

    private String getValueInDReg() {
        return """
                @SP
                AM=M-1
                D=M
                """;
    }

    private String loadDtoR13(){
        return """
                @R13
                M=D
                """;
    }


    private String segmentAndIndexTranslation(){
        StringBuilder line= new StringBuilder();
        if(arg2<=7){
            line.append(getValueInDReg());
            line.append("@").append(memoryTranslator.translateVMSymbolToHackSymbol(arg1)).append("\n");
            line.append("A=M").append(arg2 == 0 ? "" : "+1").append("\n");
            line.append("A=A+1\n".repeat(Math.max(0, arg2 - 1)));
            line.append("M=D\n");
        }
        else{
            line.append("@").append(arg2).append("\n");
            line.append("D=A\n");
            line.append("@").append(memoryTranslator.translateVMSymbolToHackSymbol(arg1)).append("\n");
            line.append("D=D+M\n");
            line.append(loadDtoR13());
            line.append(getValueInDReg());
            line.append("@R13\n");
            line.append("A=M\n");
            line.append("M=D\n");
        }
        return line.toString();
    }

    private String constantAddressTranslation() {
        return getValueInDReg()+"@"+memoryTranslator.translateSymbolToAddress(arg1, arg2)+
                "\nM=D\n";
    }

    private String staticAddressTranslation() {
        return getValueInDReg()+"@"+memoryTranslator.setStaticSymbol(arg2)+
                "\nM=D\n";
    }

    @Override
    public String writeCode() {
        String line = "";
        if(memoryTranslator.MemoryType1.contains(arg1)){
            line =  segmentAndIndexTranslation();
        }
        else if(memoryTranslator.MemoryType2.contains(arg1)){
            line = constantAddressTranslation();
        }
        else if(arg1.equals("static")){
            return staticAddressTranslation();
        }
        else {
            throw new IllegalArgumentException("Wrong Argument: " + arg1);
        }
        return line;
    }

}
