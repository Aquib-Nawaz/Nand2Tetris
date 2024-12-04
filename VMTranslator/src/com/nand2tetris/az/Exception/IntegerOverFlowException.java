package com.nand2tetris.az.Exception;

public class IntegerOverFlowException extends RuntimeException{

    public IntegerOverFlowException(String value){
        super("Integer value should lie between 0 and 32767 "+value);
    }
}
