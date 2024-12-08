package expression;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class VarSet implements Serializable {
    private HashMap<String, Integer> variable = new HashMap<>();
    private HashMap<Expansion, Integer> sin = new HashMap<>();
    private HashMap<Expansion, Integer> cos = new HashMap<>();

    public void putVar(String str, Integer expo) { this.variable.put(str, expo); }

    public HashMap<String, Integer> getVar() { return this.variable; }

    public void putSin(Expansion arg, Integer expo) { this.sin.put(arg, expo); }

    public HashMap<Expansion, Integer> getSin() { return this.sin; }

    public void putCos(Expansion arg, Integer expo) { this.cos.put(arg, expo); }

    public HashMap<Expansion, Integer> getCos() { return this.cos; }

    public VarSet multiple(VarSet next) {
        HashMap<String, Integer> varNext = next.getVar();
        Set<String> strNext = varNext.keySet();
        //deep copy
        VarSet result = this.deepClone();
        //iterate
        for (String s : strNext) {
            if (result.variable.containsKey(s)) {
                int expo = result.variable.get(s);
                int expoNext = varNext.get(s);
                expo += expoNext;
                result.variable.replace(s, expo);
            } else {
                result.putVar(s, varNext.get(s));
            }
        }
        HashMap<Expansion, Integer> sinNext = next.getSin();
        Set<Expansion> sinArgNext = sinNext.keySet();
        //iterate
        for (Expansion f : sinArgNext) {
            if (result.sin.containsKey(f)) {
                int expo = result.sin.get(f);
                int expoNext = sinNext.get(f);
                expo += expoNext;
                result.sin.replace(f, expo);
            } else {
                result.putSin(f, sinNext.get(f));
            }
        }
        HashMap<Expansion, Integer> cosNext = next.getCos();
        Set<Expansion> cosArgNext = cosNext.keySet();
        //iterate
        for (Expansion f : cosArgNext) {
            if (result.cos.containsKey(f)) {
                int expo = result.cos.get(f);
                int expoNext = cosNext.get(f);
                expo += expoNext;
                result.cos.replace(f, expo);
            } else {
                result.putCos(f, cosNext.get(f));
            }
        }
        return result;
    }

    public String toString() {
        Set<String> str = this.variable.keySet();
        Iterator<String> iter = str.iterator();
        StringBuilder sb = new StringBuilder();
        //iteration
        while (iter.hasNext()) {
            String nextElement = iter.next();
            int expo = this.variable.get(nextElement);
            if (expo != 0) {
                sb.append("*" + nextElement);
                if (expo != 1) { sb.append("**" + expo); }
            }
        }
        Set<Expansion> sin = this.sin.keySet();
        Iterator<Expansion> iterSin = sin.iterator();
        while (iterSin.hasNext()) {
            Expansion nextElement = iterSin.next();
            int expo = this.sin.get(nextElement);
            if (expo != 0) {
                sb.append("*sin(");
                if (bracketOrNot(nextElement)) { sb.append("(" + nextElement.toString() + "))"); }
                else { sb.append(nextElement.toString() + ")"); }
                if (expo != 1) { sb.append("**" + expo); }
            }
        }
        Set<Expansion> cos = this.cos.keySet();
        Iterator<Expansion> iterCos = cos.iterator();
        while (iterCos.hasNext()) {
            Expansion nextElement = iterCos.next();
            int expo = this.cos.get(nextElement);
            if (expo != 0) {
                sb.append("*cos(");
                if (bracketOrNot(nextElement)) { sb.append("(" + nextElement.toString() + "))"); }
                else { sb.append(nextElement.toString() + ")"); }
                if (expo != 1) { sb.append("**" + expo); }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof VarSet)) { return false; }
        VarSet varSet = (VarSet) o;
        boolean var = Objects.equals(variable, varSet.variable);
        boolean sin = Objects.equals(getSin(), varSet.getSin());
        boolean cos = Objects.equals(getCos(), varSet.getCos());
        return var && sin && cos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, getSin(), getCos());
    }

    public VarSet deepClone() {
        VarSet copy = new VarSet();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (VarSet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }

    public boolean bracketOrNot(Expansion arg) {
        HashMap<VarSet, BigInteger> exTerms = arg.get();
        Set<VarSet> varSet = exTerms.keySet();
        int coeIsOne = 0;
        int coeBiggerTwo = 0;
        for (VarSet v : varSet) {
            if (exTerms.get(v).compareTo(BigInteger.valueOf(1)) == 0) {
                if (varBracket(v) == 1) {
                    coeIsOne++;
                } else if (varBracket(v) > 1) {
                    return true;
                } else {
                    coeBiggerTwo++;
                }
            } else if (exTerms.get(v).compareTo(BigInteger.valueOf(1)) > 0) {
                if (varBracket(v) > 0) {
                    return true;
                }
                coeBiggerTwo++;
            } else if (exTerms.get(v).compareTo(BigInteger.valueOf(0)) < 0) {
                if (varBracket(v) > 0) {
                    return true;
                } else { coeBiggerTwo++; }
            }
        }
        if (coeIsOne == 1 && coeBiggerTwo == 0) {
            return false;
        } else if (coeIsOne == 0 && coeBiggerTwo == 1) {
            return false;
        }
        return true;
    }

    public int varBracket(VarSet varset) {
        HashMap<String, Integer> power = varset.getVar();
        Set<String> varStr = power.keySet();
        int expoNotZero = 0;
        for (String s : varStr) {
            if (power.get(s).compareTo(0) != 0) {
                expoNotZero++;
            }
        }
        HashMap<Expansion, Integer> sinHere = varset.getSin();
        Set<Expansion> sinSet = sinHere.keySet();
        for (Expansion f : sinSet) {
            if (sinHere.get(f).compareTo(0) != 0) {
                expoNotZero++;
            }
        }
        HashMap<Expansion, Integer> cosHere = varset.getCos();
        Set<Expansion> cosSet = cosHere.keySet();
        for (Expansion f : cosSet) {
            if (cosHere.get(f).compareTo(0) != 0) {
                expoNotZero++;
            }
        }
        return expoNotZero;
    }
}
