package com.nand2tetris.az.Memory;

import com.nand2tetris.az.Exception.StaticMemoryOverFlowException;
import com.nand2tetris.az.Exception.SymbolNotFoundException;

import java.util.*;

public class MemoryTranslator {
    private final Map<String, Integer> variableMap;
    private final Map<String, String> vmToHackMap;
    private String functionName;
    private int staticAddr;
    private int loopAddr;
    private final String loopPref;
    private int calleeCount = 0;
    private String fileName;

    public static MemoryTranslator memoryTranslatorInstance;
    public final List<String> MemoryType1=List.of(
            "local", "argument", "this", "that"
    );

    public final List<String> MemoryType2=List.of(
            "temp", "pointer"
    );
    private MemoryTranslator(){
        variableMap  = new HashMap<>();
        vmToHackMap = initializeHackMap();
        staticAddr = 16;
        loopAddr = 0;
        loopPref = "$VM_TO_HACK$";
    }

    public synchronized static MemoryTranslator getInstance(){
        return Objects.requireNonNullElseGet(memoryTranslatorInstance,
                () -> memoryTranslatorInstance = new MemoryTranslator());
    }

    private static Map<String, String>initializeHackMap(){
        Map<String, String>map = new HashMap<>();
        map.put("local", "LCL");
        map.put("argument", "ARG");
        map.put("this", "THIS");
        map.put("that", "THAT");
        for (int i=13;i<16;i++){
            map.put("R"+i, "R"+i);
        }
        return map;
    }

    public String translateVMSymbolToHackSymbol(String arg1){
        if(!vmToHackMap.containsKey(arg1)){
            throw new IllegalArgumentException("Invalid first argument: "+arg1);
        }
        return vmToHackMap.get(arg1);
    }

    public String translateSymbolToAddress(String arg1, int arg2){
        int variable = switch (arg1){
            case "temp" -> 5;
            case "pointer" -> 3;
            default -> throw new IllegalArgumentException("Invalid first argument: "+arg1);
        };
        return Integer.valueOf(variable+arg2).toString();
    }

    public String translateLabelSymbol(String label){
        return getFunctionName()+"$"+label;
    }
    public String getNextLoopAddr(){
        return loopPref + loopAddr++;
    }

    public int setStaticSymbol(int arg) {
        String symbolName = fileName + "." + arg;
        if(variableMap.containsKey(symbolName)){
            return variableMap.get(symbolName);
        }
        if(staticAddr > 256){
            throw new StaticMemoryOverFlowException();
        }
        variableMap.put(symbolName, staticAddr);
        return staticAddr++ ;
    }

    public int getStaticSymbolAddr(int arg){
        String symbolName = fileName + "." + arg;
        if(!variableMap.containsKey(symbolName)){
            throw new SymbolNotFoundException(Integer.toString(arg));
        }
        return variableMap.get(symbolName);
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFunctionName() {
        return functionName;
    }
    public void setFunctionName(String functionName) {
        calleeCount = 0;
        this.functionName = functionName;
    }

    public int getAndIncrementCalleeCount() {
        return calleeCount++;
    }

}
