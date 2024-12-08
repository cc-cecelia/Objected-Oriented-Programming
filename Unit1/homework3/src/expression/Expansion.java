package expression;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Expansion implements Serializable {
    private HashMap<VarSet, BigInteger> exTerms = new HashMap<>();

    public void putEx(VarSet var, BigInteger coe) {
        if (this.exTerms.containsKey(var)) {
            this.exTerms.put(var, coe.add(this.exTerms.get(var)));
        } else { this.exTerms.put(var, coe); }
    }

    public HashMap<VarSet, BigInteger> get() {
        return this.exTerms;
    }

    public void multiple(Expansion next) {
        HashMap<VarSet, BigInteger> mapNext = next.get();
        Set<VarSet> varNext = mapNext.keySet();
        Set<VarSet> var = this.exTerms.keySet();
        //deep copy
        Expansion copy = new Expansion();
        HashMap<VarSet, BigInteger> exCopy = copy.get();
        //iterate
        for (VarSet u : var) {
            for (VarSet v : varNext) {
                VarSet result = u.multiple(v);
                //calculate coeCurrent
                BigInteger coeNext = mapNext.get(v);
                BigInteger coeCurrent = coeNext.multiply(this.exTerms.get(u));
                //add into copy
                if (exCopy.containsKey(result)) {
                    BigInteger coe = exCopy.get(result);
                    BigInteger coeAns = coe.add(coeCurrent);
                    exCopy.replace(result, coeAns);
                }
                else {
                    copy.putEx(result, coeCurrent);
                }
            }
        }
        //deep copy 'copy'
        this.exTerms.clear();
        for (VarSet s : exCopy.keySet()) {
            this.putEx(s, exCopy.get(s));
        }
    }

    public void add(Expansion next) {
        HashMap<VarSet, BigInteger> mapNext = next.get();
        Set<VarSet> varNext = mapNext.keySet();
        for (VarSet v : varNext) {
            this.putEx(v, mapNext.get(v));
        }
    }

    public String toString() {
        Set<VarSet> var = this.exTerms.keySet();
        Iterator<VarSet> iter = var.iterator();
        StringBuilder sb = new StringBuilder();
        //first term
        VarSet nextElement = iter.next();
        BigInteger coe = this.exTerms.get(nextElement);
        sb.append(coe.toString());
        if (coe.compareTo(BigInteger.valueOf(0)) != 0) {
            sb.append(nextElement.toString());
        }
        //iteration
        while (iter.hasNext()) {
            VarSet nextElement0 = iter.next();
            BigInteger coeNext = this.exTerms.get(nextElement0);
            if (coeNext.compareTo(BigInteger.valueOf(0)) > 0) {
                sb.append("+");
            }
            else if (coeNext.compareTo(BigInteger.valueOf(0)) == 0) {
                continue;
            }
            sb.append(coeNext.toString());
            sb.append(nextElement0.toString());
        }
        // deal with 0+/0-
        if (sb.length() > 1) {
            if (sb.charAt(0) == '0' && sb.charAt(1) == '+') {
                sb.delete(0,2);
            } else if (sb.charAt(0) == '0' && sb.charAt(1) == '-') {
                sb.deleteCharAt(0);
            }
        }
        int pos = 0;
        while (sb.indexOf("1*", pos) != -1) {
            pos = sb.indexOf("1*", pos);
            if (pos == 0) {
                sb.delete(pos, pos + 2);
            } else if (!Character.isDigit(sb.charAt(pos - 1))) {
                sb.delete(pos, pos + 2);
            } else {
                pos++;
            }
        }
        return sb.toString();
    }

    public void inverse() {
        Set<VarSet> var = this.exTerms.keySet();
        for (VarSet v : var) {
            BigInteger coe = this.exTerms.get(v);
            coe = coe.multiply(BigInteger.valueOf(-1));
            this.exTerms.put(v, coe);
        }
    }

    public Expansion derive(char para) {
        Expansion result = new Expansion();
        Set<VarSet> var = this.exTerms.keySet();
        for (VarSet v : var) {
            result.add(v.derive(para, this.exTerms.get(v)));
        }
        HashMap<VarSet, BigInteger> exTermsHere = result.get();
        if (exTermsHere.isEmpty()) {
            VarSet varset = new VarSet();
            //set varSet
            varset.putVar("x", 0);
            varset.putVar("y", 0);
            varset.putVar("z", 0);
            exTermsHere.put(varset, new BigInteger("0"));
        }
        return result;
    }

    public Expansion deepClone() {
        Expansion copy = new Expansion();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (Expansion) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Expansion)) { return false; }
        Expansion expansion = (Expansion) o;
        return Objects.equals(exTerms, expansion.exTerms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exTerms);
    }
}
