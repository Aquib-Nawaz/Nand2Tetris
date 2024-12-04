package com.nand2tetris.az.Exception;

public class WrongNumberOfArgumentsException extends RuntimeException{
    public WrongNumberOfArgumentsException(String commandType, int expected, int actual) {
        super("Wrong Number Of Arguments For Command Type: "+commandType+
                " expected: "+expected+", actual: "+actual);
    }
}
