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
        line = line.split("//")[0].trim();
        String []keyWords = line.split("\\s+");
        String commandType = keyWords[0];
        boolean pushCommand = Objects.equals(commandType, "push");
        boolean popCommand = Objects.equals(commandType, "pop");
        if(popCommand || pushCommand){
            checkNumberOfArguments(keyWords, commandType, 2);
            checkArgumentType(keyWords, 2, "\\d+", "int");
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
        boolean arithmeticCommand = Arrays.asList(arithmeticCommands).contains(keyWords[0]);
        if(arithmeticCommand){
            checkNumberOfArguments(keyWords, commandType, 0);
            return new ArithmeticInstruction(keyWords[0]);
        }
        if(commandType.equals("label") || commandType.equals("goto") || commandType.equals("if-goto")){
            checkNumberOfArguments(keyWords, commandType, 1);
            if(commandType.equals("label")){
                return new LabelInstruction(keyWords[1]);
            }
            if(commandType.equals("goto")){
                return new GotoInstruction(keyWords[1]);
            }
            return new IfGotoInstruction(keyWords[1]);
        }
        if(commandType.equals("function") || commandType.equals("call")){
            checkNumberOfArguments(keyWords, commandType, 2);
            checkArgumentType(keyWords, 2, "\\d+", "int");
            if(commandType.equals("function")) {
                return new FunctionInstruction(keyWords[1], Integer.parseInt(keyWords[2]));
            }
            return new CallInstruction(keyWords[1], Integer.parseInt(keyWords[2]));
        }
        if(commandType.equals("return")){
            checkNumberOfArguments(keyWords, commandType, 0);
            return new ReturnInstruction();
        }
        throw new IllegalArgumentException("Invalid Command: "+line);
    }

    private static void checkArgumentType(String[] keyWords, int idx, String expr, String expected) {
        if(!keyWords[idx].matches(expr)){
            throw new WrongTypeArgumentException(expected, keyWords[idx]);
        }
    }

    private static void checkNumberOfArguments(String[] keyWords, String commandType, int expected) {
        if (keyWords.length-1 != expected) {
            throw new WrongNumberOfArgumentsException(commandType, expected, keyWords.length - 1);
        }
    }
}
