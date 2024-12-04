import Code.AInstruction;
import Code.Code;
import Parser.*;

import java.io.FileWriter;
import java.io.IOException;


public class Main {
    static String checkExtensionAndReturnOutFile(String fileName){
        if(!fileName.endsWith(".asm")){
            System.out.println("Error: filename Should End with .asm");
        }
        return fileName.substring(0,fileName.length()-4)+".hack";
    }

    static void lookForLInstruction(String fileName, AInstruction aInstruction){
        Parser parser= new Parser(fileName);
        int intructionNum=0;
        while (parser.hasMoreCommands()){
            parser.advance();
            if(parser.commandType()==InstructionType.L_COMMAND){
                aInstruction.addLoopSymbol(parser.symbol(), intructionNum);
            }
            else{
                intructionNum++;
            }
        }
    }

    public static void main(String[] args) {
        String fileName = args[0];
        String outFile = checkExtensionAndReturnOutFile(fileName);
        Parser parser = new Parser(fileName);
        AInstruction aInstruction = new AInstruction();
        lookForLInstruction(fileName, aInstruction);

        try(FileWriter writer = new FileWriter(outFile)){
            while (parser.hasMoreCommands()){
                parser.advance();
                if(parser.commandType()== InstructionType.A_COMMAND){
                    writer.write(aInstruction.getCode(parser.symbol())+"\n");
                }
                else if(parser.commandType()==InstructionType.C_COMMAND){
                    writer.write("111"+ Code.comp(parser.comp()) +
                            Code.dest(parser.dest()) + Code.jump(parser.jump())+"\n");
                }
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch (RuntimeException e){
            System.out.println(parser.getLineNum()+": "+e.getMessage());
        }
    }
}