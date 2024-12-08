package expression;

import java.math.BigInteger;
import java.util.Objects;

public class Power implements Factor {
    private final String var;
    private final int expo;
    private final int sign;

    public Power(String var, int expo, int sign) {
        this.var = var;
        this.expo = expo;
        this.sign = sign;
    }

    @Override
    public Expansion toExpansion() {
        VarSet varset = new VarSet();
        varset.putVar("x", 0);
        varset.putVar("y", 0);
        varset.putVar("z", 0);
        //set varSet
        Integer expo = Integer.valueOf(this.expo);
        varset.putVar(this.var, expo);
        //set coe
        BigInteger coe = BigInteger.valueOf(sign);
        Expansion expansion = new Expansion();
        expansion.putEx(varset, coe);

        return expansion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        if (sign == -1) {
            sb.append('-');
        }
        if (this.expo == 0) {
            sb.append("1)");
        } else if (this.expo == 1) {
            sb.append(this.var + ")");
        } else {
            sb.append(this.var + "**" + this.expo + ")");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Power)) { return false; }
        Power power = (Power) o;
        return expo == power.expo && sign == power.sign && Objects.equals(var, power.var);
    }

    @Override
    public int hashCode() {
        return Objects.hash(var, expo, sign);
    }
}
