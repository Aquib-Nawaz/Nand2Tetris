package com.nand2tetris.az.Exception;

public class StaticMemoryOverFlowException extends RuntimeException {

    public StaticMemoryOverFlowException() {
        super("Static memory full can't add new variables");
    }
}
