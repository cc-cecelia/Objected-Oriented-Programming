package expression;

public class Derivation implements Factor {
    private int sign;
    private char para;
    private Expr exprToDe;

    public Derivation() {
        this.sign = 1;
        this.para = 'x';
        this.exprToDe = new Expr();
    }

    public void addSign(int sign) { this.sign = sign; }

    public void addPara(char para) { this.para = para; }

    public void addExpr(Expr expr) { this.exprToDe = expr; }

    public Expansion derive() {
        Expansion result = this.exprToDe.toExpansion().derive(this.para);
        return result;
    }

    @Override
    public Expansion toExpansion() {
        return derive();
    }
}
