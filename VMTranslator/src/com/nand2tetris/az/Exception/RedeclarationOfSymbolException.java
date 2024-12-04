package com.nand2tetris.az.Exception;

public class RedeclarationOfSymbolException extends RuntimeException{

    public RedeclarationOfSymbolException(String value){
        super("Redeclaration Of Symbol "+value);
    }
}
