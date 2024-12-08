package expression;

import java.math.BigInteger;

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

}
