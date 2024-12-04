package com.nand2tetris.az.Instruction;
import com.nand2tetris.az.Exception.IntegerOverFlowException;
import com.nand2tetris.az.Exception.WrongNumberOfArgumentsException;
import com.nand2tetris.az.Exception.WrongTypeArgumentException;

import java.util.Arrays;
import java.util.Objects;

public class InstructionFactory {
    private static final String[] arithmeticCommands = {"add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"};
    public static Instruction createInstruction(String line){
        //Remove Comment
        line = line.split("//")[0];
        String []keyWords = line.split(" ");
        String commandType = keyWords[0];
        boolean pushCommand = Objects.equals(commandType, "push");
        boolean popCommand = Objects.equals(commandType, "pop");
        if(popCommand || pushCommand){
            if(keyWords.length != 3){
                throw new WrongNumberOfArgumentsException(commandType, 2, keyWords.length-1);
            }
            if(!keyWords[2].matches("\\d+")){
                throw new WrongTypeArgumentException(keyWords[2], "int");
            }
            int number = Integer.parseInt(keyWords[2]);
            if(number < 0 || number > 32767){
                throw new IntegerOverFlowException(keyWords[2]);
            }
            if(pushCommand){
                return new PushInstruction(keyWords[1], number);
            }
            else{
                return new PopInstruction(keyWords[1], number);
            }
        }
        boolean arithmeticCommand = keyWords.length == 1 && Arrays.asList(arithmeticCommands).contains(keyWords[0]);
        if(arithmeticCommand){
            return new ArithmeticInstruction(keyWords[0]);
        }
        throw new IllegalArgumentException("Invalid Command: "+line);
    }
}
