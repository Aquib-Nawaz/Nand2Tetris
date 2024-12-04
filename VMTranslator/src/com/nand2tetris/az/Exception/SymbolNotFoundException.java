package com.nand2tetris.az.Exception;

public class SymbolNotFoundException extends RuntimeException{

    public SymbolNotFoundException(String symbol) {
        super("Symbol not found: "+symbol);
    }
}
