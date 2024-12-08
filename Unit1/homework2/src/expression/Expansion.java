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
        this.exTerms.put(var, coe);
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
            if (this.exTerms.containsKey(v)) {
                BigInteger coe = this.exTerms.get(v);
                BigInteger coeAns = coe.add(mapNext.get(v));
                this.exTerms.replace(v, coeAns);
            }
            else {
                this.putEx(v, mapNext.get(v));
            }
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
