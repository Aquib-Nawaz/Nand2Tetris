package com.nand2tetris.az.Parser;

import com.nand2tetris.az.Instruction.Instruction;
import com.nand2tetris.az.Instruction.InstructionFactory;


import java.io.*;

public class Parser {
    private String curLine, nextLine;
    private BufferedReader reader;
    private int lineNum;

    public Parser(String fileName){
        lineNum = 0;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        }
        catch (FileNotFoundException f){
            throw new RuntimeException("File not found: "+fileName);
        }
    }
    private boolean isEmptyLine(String line){
        line = line.trim();
        return line.isEmpty() || line.trim().startsWith("//");
    }

    private void step(){
        try {
            for(String line=reader.readLine(); line!=null; line=reader.readLine()){
                lineNum++;
                if(isEmptyLine(line)){
                    continue;
                }
                nextLine = line;
                break;
            }
        }
        catch (IOException e){
            throw new RuntimeException("Unable to read file");
        }
    }

    public boolean hasMoreCommands(){
        if(nextLine==null){
            step();
        }
        return nextLine != null;
    }

    public void advance(){
        if(nextLine == null){
            step();
        }
        if(nextLine == null){
            throw new RuntimeException("No more instructions!");
        }
        curLine = nextLine;
        nextLine = null;
    }

    public int getLineNum(){
        return lineNum;
    }

    public Instruction getCurrentInstruction(){
        return InstructionFactory.createInstruction(curLine);
    }
}
