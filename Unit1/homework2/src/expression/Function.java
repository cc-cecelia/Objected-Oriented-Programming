package expression;

import java.util.Objects;

public class Function implements Factor {
    private String actFunc;
    private Expr expr;

    public Function(String input, int sign) {
        this.actFunc = input;
        this.transfer();
        this.expr.multSign(sign);
    }

    public void transfer() {
        Lexer lexer = new Lexer(actFunc);
        Parser parser = new Parser(lexer);
        //parse
        this.expr = parser.parseExpr();
    }

    public void getActFunc(String actualFunc) {
        this.actFunc = actualFunc;
    }

    @Override
    public Expansion toExpansion() {
        return this.expr.toExpansion();
    }

    @Override
    public String toString() { return this.expr.toString(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Function)) { return false; }
        Function function = (Function) o;
        return Objects.equals(actFunc, function.actFunc) && Objects.equals(expr, function.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actFunc, expr);
    }
}
