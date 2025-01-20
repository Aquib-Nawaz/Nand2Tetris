package Parser;

import java.util.List;

public class Rule {
    private final Symbol lhs;
    private final List<Symbol>rhs;
    private Integer prec;
    public Rule(Symbol lhs, List<Symbol> rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Rule(Symbol lhs, List<Symbol> rhs, int prec){
        this.lhs = lhs;
        this.rhs = rhs;
        this.prec = prec;
    }
    public Symbol lhs(){
        return lhs;
    }
    public List<Symbol> rhs(){
        return rhs;
    }

    public Integer prec(){
        return prec;
    }
}
