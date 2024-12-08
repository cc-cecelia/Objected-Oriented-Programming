package expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Term implements Factor {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    @Override
    public Expansion toExpansion() {
        VarSet varset = new VarSet();
        varset.putVar("x", 0);
        varset.putVar("y", 0);
        varset.putVar("z", 0);
        Expansion expansion = new Expansion();
        expansion.putEx(varset, BigInteger.valueOf(1));
        Iterator<Factor> iter = factors.iterator();
        while (iter.hasNext()) {
            expansion.multiple(iter.next().toExpansion());
        }

        return expansion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Factor> iter = factors.iterator();
        sb.append("(");
        Factor next = iter.next();
        sb.append(next.toString());
        while (iter.hasNext()) {
            sb.append("*" + iter.next().toString());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Term)) { return false; }
        Term term = (Term) o;
        return Objects.equals(factors, term.factors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factors);
    }
}
