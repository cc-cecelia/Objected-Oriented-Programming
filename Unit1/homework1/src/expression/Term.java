package expression;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

public class Term implements Factor {
    private final HashSet<Factor> factors;

    public Term() {
        this.factors = new HashSet<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    @Override
    public Expansion toExpansion() {
        Iterator<Factor> iter = factors.iterator();
        VarSet varset = new VarSet();
        varset.putVar("x", 0);
        Expansion expansion = new Expansion();
        expansion.putEx(varset, BigInteger.valueOf(1));
        while (iter.hasNext()) {
            expansion.multiple(iter.next().toExpansion());
        }

        return expansion;
    }
}
