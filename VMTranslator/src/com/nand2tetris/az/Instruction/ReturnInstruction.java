package com.nand2tetris.az.Instruction;

public class ReturnInstruction implements Instruction {
    @Override
    public String arg1() {
        throw new RuntimeException("Method Not Supported");
    }

    @Override
    public int arg2() {
        throw new RuntimeException("Method Not Supported");
    }

    @Override
    public InstructionType type() {
        return InstructionType.C_RETURN;
    }

    @Override
    public String writeCode() {
        return  saveReturnAddressToR14() +
                setReturnValueAtTopOfStack() +
                loadLCLToR13()+
                loadAddressToSpecialRegister("THAT")+
                popR13()+
                loadAddressToSpecialRegister("THIS")+
                popR13()+
                loadAddressToSpecialRegister("ARG")+
                popR13()+
                loadAddressToSpecialRegister("LCL")+
                "@R14\nA=M\n0;JMP\n";
    }

    private String loadLCLToR13(){
        return """
                @LCL
                D=M
                @R13
                AM=D-1
                """;
    }

    private String loadAddressToSpecialRegister(String reg){
        return "D=M\n" +
                "@"+reg+"\nM=D\n";
    }

    private String popR13(){
        return """
                @R13
                AM=M-1
                """;
    }

    private String setReturnValueAtTopOfStack(){
        return """
                @SP
                A=M-1
                D=M
                @ARG
                A=M
                M=D
                @ARG
                D=M+1
                @SP
                M=D
                """;
                
    }

    private String saveReturnAddressToR14(){
        return """
                @5
                D=A
                @LCL
                A=M-D
                D=M
                @R14
                M=D
                """;
    }
}
