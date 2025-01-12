package Parser;

import java.util.List;

public record Rule(Token lhs, List<Token>rhs) {
}
