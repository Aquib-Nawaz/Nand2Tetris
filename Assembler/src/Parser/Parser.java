package Parser;

import Exceptions.ParseError;

import java.io.*;

public class Parser {
    private final BufferedReader fileReader;
    private String curInstr, nextInstruction;
    private int lineNum;
    public Parser(String fileName) {
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            curInstr = nextInstruction = null;
        }
        catch (FileNotFoundException f){
            throw new RuntimeException("File Not Found");
        }
        lineNum = 0;
    }

    public int getLineNum(){
        return lineNum;
    }

    public boolean hasMoreCommands(){
        if(nextInstruction != null)
            return true;
        step();
        return nextInstruction != null;
    }

    private void step(){
        try{
            for(String line = fileReader.readLine(); line != null; line = fileReader.readLine()){
                lineNum++;
                line = line.replaceAll("\\s+", "");
                if(line.isEmpty() || line.startsWith("//")){
                    continue;
                }
                int commentStartIdx = line.indexOf("//");
                line = commentStartIdx >= 0 ? line.substring(0, commentStartIdx) : line;
                nextInstruction = line;
                break;
            }
        }
        catch (IOException e){
            throw new RuntimeException("Unable To Read File");
        }
    }

    public void advance(){
        if(nextInstruction == null){
            step();
            if(nextInstruction == null){
                throw new InstructionNotFoundException();
            }
        }
        curInstr = nextInstruction;
        nextInstruction = null;
    }


    public InstructionType commandType(){
        if(curInstr == null){
            throw new InstructionNotFoundException();
        }
        if(curInstr.startsWith("@")){
            return InstructionType.A_COMMAND;
        }
        if(curInstr.startsWith("(")){
            return InstructionType.L_COMMAND;
        }
        return InstructionType.C_COMMAND;
    }

    public String symbol(){
        if(commandType() == InstructionType.A_COMMAND){
            return curInstr.substring(1);
        }
        if(commandType() == InstructionType.L_COMMAND){
            if(!curInstr.endsWith(")")){
                throw new ParseError("L_Command syntax error");
            }
            return curInstr.substring(1, curInstr.length() - 1);
        }
        throw new MethodNotSupportedException("symbol", InstructionType.C_COMMAND);
    }

    public String dest(){
        InstructionType commandType = commandType();
        if(commandType != InstructionType.C_COMMAND){
            throw new MethodNotSupportedException("dest", commandType);
        }
        if(curInstr.contains("=")){
            return curInstr.split("=")[0];
        }
        return "";
    }

    public String comp(){
        InstructionType commandType = commandType();
        if(commandType != InstructionType.C_COMMAND){
            throw new MethodNotSupportedException("comp", commandType);
        }
        String [] parts = curInstr.split(";")[0].split("=");

        return parts[parts.length-1];
    }

    public String jump(){
        InstructionType commandType = commandType();
        if(commandType != InstructionType.C_COMMAND){
            throw new MethodNotSupportedException("jump", commandType);
        }
        if(curInstr.contains(";")){
            return curInstr.split(";")[1];
        }
        return "";
    }


}
