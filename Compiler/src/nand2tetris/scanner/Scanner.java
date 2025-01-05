package nand2tetris.scanner;

import java.io.FileReader;
import java.io.InputStream;

public class Scanner {
    private final InputStream inputStream;
    private Token currToken;
    public Scanner(InputStream stream){
        inputStream = stream;
    }

    public Token peek(){
        if(currToken == null){
            step();
        }
        if(currToken == null){
            throw new IllegalStateException("No more tokens");
        }
        return currToken;
    }

    public void advance(){
        currToken = null;
    }

    private void step(){

    }


}
