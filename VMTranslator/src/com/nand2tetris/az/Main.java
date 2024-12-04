package com.nand2tetris.az;

import com.nand2tetris.az.Memory.MemoryTranslator;
import com.nand2tetris.az.Parser.Parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Main {
    public static void main(String[ ] args){
        if(args.length==0){
            System.out.println("Please provide a file name");
            return;
        }
        if(!args[0].endsWith(".vm")){
            System.out.println("Please provide a .vm file");
            return;
        }
        Parser parser = new Parser(args[0]);
        //get base filename
        File file = new File(args[0]);
        String fileName = file.getName();
        fileName = fileName.substring(0,fileName.length()-3);
        MemoryTranslator.getInstance().setFileName(fileName);
        try (BufferedWriter fileWriter = new BufferedWriter(
                new FileWriter(args[0].substring(0,args[0].length()-3) + ".asm"))){
            while(parser.hasMoreCommands()){
                parser.advance();
                fileWriter.write(parser.getCurrentInstruction().writeCode());
            }
        }
        catch (Exception e){
            throw new RuntimeException("Can't write to file");
        }
    }
}
