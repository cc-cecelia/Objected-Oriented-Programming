package expression;

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;
    private final int sign;

    public Number(BigInteger num, int sign) {
        this.num = num;
        this.sign = sign;
    }

    @Override
    public Expansion toExpansion() {
        Expansion expansion = new Expansion();
        VarSet varset = new VarSet();
        //set varSet
        Integer expo = Integer.valueOf(0);
        String str = "x";
        varset.putVar(str, expo);
        //set coe
        BigInteger coe = num.multiply(BigInteger.valueOf(sign));
        expansion.putEx(varset, coe);

        return expansion;
    }
}
