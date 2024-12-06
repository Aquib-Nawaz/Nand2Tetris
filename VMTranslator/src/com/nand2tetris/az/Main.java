package com.nand2tetris.az;

import com.nand2tetris.az.Memory.MemoryTranslator;
import com.nand2tetris.az.Parser.Parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[ ] args){
        if(args.length==0){
            System.out.println("Please provide a file or folder name");
            System.exit(1);
        }
        List<File> fileList = new ArrayList<>();
        String writeFilePath="";
        if(args[0].endsWith(".vm")){
            fileList.add(new File(args[0]));
            writeFilePath = args[0].substring(0, args[0].length()-3)+".asm";
        }
        else if(new File(args[0]).isDirectory()){
            File[] files = new File(args[0]).listFiles();
            if(files==null)
                throw new RuntimeException("Unable to read directory");
            for(File file : files){
                if(file.getName().endsWith(".vm")){
                    fileList.add(file);
                }
            }
            writeFilePath = args[0]+".asm";
        }
        else {
            System.out.println("Please provide a file or folder name containing .vm file");
            System.exit(1);
        }

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(writeFilePath))){
            fileWriter.write(initializeFile()+"\n");
            for(File file : fileList){
                compileFile(file.getAbsolutePath(), file.getName(), fileWriter);
            }
            fileWriter.flush();
        }
        catch (IOException | RuntimeException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void compileFile(String path, String name, BufferedWriter fileWriter) throws IOException {
        Parser parser = new Parser(path);
        name = name.substring(0, name.length()-3);
        MemoryTranslator.getInstance().setFileName(name);

        try {
            while(parser.hasMoreCommands()){
                parser.advance();
                fileWriter.write(parser.getCurrentInstruction().writeCode()+"\n");
            }
        }
        catch (RuntimeException e){
            throw new RuntimeException("File: "+path+" Line: "+parser.getLineNum()+" "+e.getMessage());
        }
    }

    private static String initializeFile(){
        return """
                @261
                D=A
                @SP
                M=D
                @Sys.init
                0;JMP
                """;
    }
}
