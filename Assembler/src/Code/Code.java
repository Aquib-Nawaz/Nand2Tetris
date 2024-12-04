package Code;

import java.util.HashMap;
import Exceptions.ParseError;

public class Code {
    private static final HashMap<String, String> destHash = createDestMap();
    private static final HashMap<String, String> jumpHash = createJumpMap();

    private static final HashMap<String, String> compHash = createCompMap();

    private static HashMap<String, String> createDestMap(){
        HashMap<String, String> destMap = new HashMap<>();
        destMap.put("M", "001");
        destMap.put("D", "010");
        destMap.put("A", "100");
        destMap.put("MD", "011");
        destMap.put("AM", "101");
        destMap.put("AD", "110");
        destMap.put("AMD", "111");
        destMap.put("", "000");
        return destMap;
    }

    private static HashMap<String, String> createJumpMap(){
        HashMap<String, String> jumpMap = new HashMap<>();
        jumpMap.put("JGT", "001");
        jumpMap.put("JEQ", "010");
        jumpMap.put("JGE", "011");
        jumpMap.put("JLT", "100");
        jumpMap.put("JNE", "101");
        jumpMap.put("JLE", "110");
        jumpMap.put("JMP", "111");
        jumpMap.put("", "000");
        return jumpMap;
    }

    private static HashMap<String, String> createCompMap(){
        HashMap<String, String> compMap = new HashMap<>();
        compMap.put("0"  ,"0101010");
        compMap.put("1"  ,"0111111");
        compMap.put("-1" ,"0111010");
        compMap.put("D"  ,"0001100");
        compMap.put("A"  ,"0110000");
        compMap.put("!D" ,"0001101");
        compMap.put("!A" ,"0110001");
        compMap.put("-D" ,"0001111");
        compMap.put("-A" ,"0110011");
        compMap.put("D+1","0011111");
        compMap.put("A+1","0110111");
        compMap.put("D-1","0001110");
        compMap.put("A-1","0110010");
        compMap.put("D+A","0000010");
        compMap.put("D-A","0010011");
        compMap.put("A-D","0000111");
        compMap.put("D&A","0000000");
        compMap.put("D|A","0010101");
        compMap.put("M"  ,"1110000");
        compMap.put("!M" ,"1110001");
        compMap.put("-M" ,"1110011");
        compMap.put("M+1","1110111");
        compMap.put("M-1","1110010");
        compMap.put("D+M","1000010");
        compMap.put("D-M","1010011");
        compMap.put("M-D","1000111");
        compMap.put("D&M","1000000");
        compMap.put("D|M","1010101");
        return compMap;
    }

    static public String dest(String dest){
        if(!destHash.containsKey(dest))
            throw new ParseError("Invalid dest: " + dest);

        return destHash.get(dest);
    }

    static public String comp(String comp){
        if(!compHash.containsKey(comp))
            throw new ParseError("Invalid comp: " + comp);
        return compHash.get(comp);
    }

    static public String jump(String jump){
        if(!jumpHash.containsKey(jump))
            throw new ParseError("Invalid jump: " + jump);
        return jumpHash.get(jump);
    }
}