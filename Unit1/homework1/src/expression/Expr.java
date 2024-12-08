package expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Expr implements Factor {
    private final HashSet<Term> terms;
    private int sign;
    private int expo;

    public Expr() {
        this.terms = new HashSet<>();
        this.sign = 1;
        this.expo = 1;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void addExpo(int expo) { this.expo = expo; }

    public void addSign(int sign) { this.sign = sign; }

    @Override
    public Expansion toExpansion() {
        Iterator<Term> iter = terms.iterator();
        VarSet varSet = new VarSet();
        varSet.putVar("x", 0);
        Expansion expansion = new Expansion();
        expansion.putEx(varSet, BigInteger.valueOf(0));
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
                Expansion tmp = expansion.clone();
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
}
