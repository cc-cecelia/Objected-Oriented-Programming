package expression;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class SinTrigono implements Factor {
    private int expo;
    private Factor arg;
    private int sign;

    public SinTrigono() {
        this.expo = 1;
        this.arg = new Factor() {
            @Override
            public Expansion toExpansion() {
                return null;
            }
        };
        this.sign = 1;
    }

    public void addExpo(int expo) { this.expo = expo; }

    public void addSign(int sign) { this.sign = sign; }

    public void addFactor(Factor factor) { this.arg = factor; }

    @Override
    public Expansion toExpansion() {
        VarSet varset = new VarSet();
        //set varSet
        varset.putVar("x", 0);
        varset.putVar("y", 0);
        varset.putVar("z", 0);
        //check arg
        boolean zeroArg = true;
        Expansion expansion = arg.toExpansion();
        HashMap<VarSet, BigInteger> exTerms = expansion.get();
        Set<VarSet> varSet = exTerms.keySet();
        for (VarSet v : varSet) {
            if (exTerms.get(v).compareTo(BigInteger.valueOf(0)) != 0) {
                zeroArg = false;
                break;
            }
        }
        Expansion expansionResult = new Expansion();
        if (zeroArg) {
            if (this.expo == 0) { expansionResult.putEx(varset, BigInteger.valueOf(1)); }
            else {
                BigInteger coe = BigInteger.valueOf(0);
                expansionResult.putEx(varset, coe); }
        } else {
            varset.putSin(this.arg.toExpansion(), this.expo);
            //set coe
            BigInteger coe = BigInteger.valueOf(this.sign);
            expansionResult.putEx(varset, coe);
        }
        return expansionResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (this.sign == -1) {
            sb.append('-');
        }
        if (this.expo == 0) {
            sb.append("1)");
        } else if (this.expo == 1) {
            sb.append("sin(" + this.arg.toString() + "))");
        } else {
            sb.append("sin(" + this.arg.toString() + ")" + "**" + this.expo);
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof SinTrigono)) { return false; }
        SinTrigono that = (SinTrigono) o;
        return expo == that.expo && sign == that.sign && Objects.equals(arg, that.arg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expo, arg, sign);
    }
}
