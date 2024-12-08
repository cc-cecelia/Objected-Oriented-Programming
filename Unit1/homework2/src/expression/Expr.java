package expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Expr implements Factor {
    private final ArrayList<Term> terms;
    private int sign;
    private int expo;

    public Expr() {
        this.terms = new ArrayList<>();
        this.sign = 1;
        this.expo = 1;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void addExpo(int expo) { this.expo = expo; }

    public void addSign(int sign) { this.sign = sign; }

    public void multSign(int sign) { this.sign *= sign; }

    @Override
    public Expansion toExpansion() {
        Iterator<Term> iter = terms.iterator();
        Expansion expansion = new Expansion();
        while (iter.hasNext()) {
            expansion.add(iter.next().toExpansion());
        }
        if (this.expo == 0) {
            Expansion expansion0 = new Expansion();
            VarSet varSet0 = new VarSet();
            varSet0.putVar("x", 0);
            expansion0.putEx(varSet0, BigInteger.valueOf(1));
            if (this.sign == -1) {
                expansion0.inverse();
            }
            return expansion0;
        }
        else if (this.expo >= 2) {
            ArrayList<Expansion> list = new ArrayList<>();
            for (int i = 0; i < this.expo - 1; i++) {
                Expansion tmp = expansion.deepClone();
                list.add(tmp);
            }
            for (Expansion value : list) {
                expansion.multiple(value);
            }
        }
        if (this.sign == -1) {
            expansion.inverse();
        }
        return expansion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (this.sign == -1) {
            sb.append('-');
        }
        if (this.expo == 0) {
            sb.append("1");
        } else {
            sb.append("(");
            Iterator<Term> iter = terms.iterator();
            Term next = iter.next();
            sb.append(next.toString());
            while (iter.hasNext()) {
                sb.append("+" + iter.next().toString());
            }
            sb.append(")");
            if (this.expo >= 2) {
                sb.append("**" + this.expo);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Expr))  { return false; }
        Expr expr = (Expr) o;
        return sign == expr.sign && expo == expr.expo && Objects.equals(terms, expr.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms, sign, expo);
    }
}
