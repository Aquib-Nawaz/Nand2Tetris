package com.nand2tetris.az.Exception;

public class WrongTypeArgumentException extends RuntimeException{
    public WrongTypeArgumentException(String expectedType,  String value){
        super("Wrong type of argument "+value+" expected type:- "+expectedType);
    }
}
