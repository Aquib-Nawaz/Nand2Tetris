package Parser;

import java.util.List;

public record Rule(Symbol lhs, List<Symbol>rhs) {
}
