package Code;

import java.util.HashMap;
import java.util.Map;

public class AInstruction {
    private int addr;
    private final Map<String, Integer> symbolTable;

    public AInstruction(){
        addr=16;
        symbolTable = new HashMap<>();
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
        for(int i=0;i<=15;i++){
            symbolTable.put("R"+i,i);
        }
    }
    boolean checkForNumber(String symbol){
        return symbol.matches("^\\d+$");
    }
    boolean checkForSymbol(String symbol){
        return symbol.matches("^[$._a-zA-Z:][$._\\w:]*$");
    }
    public String getCode(String symbol) {
        int number;
        if(checkForNumber(symbol)){
            number = Integer.parseInt(symbol);
            if(number>=1<<15){
                throw new RuntimeException("Number out of bound: "+ symbol);
            }
        }
        else{
            if (!checkForSymbol(symbol)){
                throw new RuntimeException("symbol contains invalid character: "+ symbol);
            }
            if(symbolTable.containsKey(symbol)){
                number = symbolTable.get(symbol);
            }
            else {
                number=addr++;
                symbolTable.put(symbol, number);
            }
        }
        return String.format ("%16s", Integer.toBinaryString(number)).replace(' ' ,'0');

    }

    public void addLoopSymbol(String symbol, int i) {
        symbolTable.put(symbol, i);
    }
}
