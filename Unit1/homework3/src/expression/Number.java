package expression;

import java.math.BigInteger;
import java.util.Objects;

public class Number implements Factor {
    private final BigInteger num;
    private final int sign;

    public Number(BigInteger num, int sign) {
        this.num = num;
        this.sign = sign;
    }

    @Override
    public Expansion toExpansion() {
        VarSet varset = new VarSet();
        //set varSet
        varset.putVar("x", 0);
        varset.putVar("y", 0);
        varset.putVar("z", 0);
        //set coe
        Expansion expansion = new Expansion();
        BigInteger coe = num.multiply(BigInteger.valueOf(sign));
        expansion.putEx(varset, coe);

        return expansion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.sign == -1) {
            sb.append("(-");
            sb.append(this.num + ")");
        } else {
            sb.append("(" + this.num + ")");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Number)) { return false; }
        Number number = (Number) o;
        return sign == number.sign && Objects.equals(num, number.num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, sign);
    }
}