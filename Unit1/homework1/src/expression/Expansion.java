package expression;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
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
        //iterate
        for (VarSet u : var) {
            for (VarSet v : varNext) {
                VarSet result = u.multiple(v);
                //calculate coeCurrent
                BigInteger coeNext = mapNext.get(v);
                BigInteger coeCurrent = coeNext.multiply(this.exTerms.get(u));
                //add into copy
                if (copy.contains(result)) {
                    VarSet varInThis = copy.getKey(result);
                    BigInteger coe = copy.exTerms.get(varInThis);
                    BigInteger coeAns = coe.add(coeCurrent);
                    copy.exTerms.replace(varInThis, coeAns);
                }
                else {
                    copy.putEx(result, coeCurrent);
                }
            }
        }
        //deep copy 'copy'
        this.exTerms.clear();
        for (VarSet s : copy.exTerms.keySet()) {
            this.putEx(s, copy.exTerms.get(s));
        }
    }

    public void add(Expansion next) {
        HashMap<VarSet, BigInteger> mapNext = next.get();
        Set<VarSet> varNext = mapNext.keySet();
        for (VarSet v : varNext) {
            if (this.contains(v)) {
                VarSet varInThis = this.getKey(v);
                BigInteger coe = this.exTerms.get(varInThis);
                BigInteger coeAns = coe.add(mapNext.get(v));
                this.exTerms.replace(varInThis, coeAns);
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

    public boolean contains(VarSet obj) {
        int flag = 0;
        int flagg = 0;
        Set<VarSet> var = this.exTerms.keySet();
        HashMap<String, Integer> objVarSet = obj.get();
        Set<String> objStr = objVarSet.keySet();
        for (VarSet v : var) {
            HashMap<String, Integer> mine = v.get();
            Set<String> mineStr = mine.keySet();
            flagg = 0;
            if (mineStr.size() != objStr.size()) {
                continue;
            }
            flagg = 1;
            for (String s : mineStr) {
                for (String objS : objStr) {
                    if (s.equals(objS)) {
                        if (mine.get(s).equals(objVarSet.get(objS))) {
                            flag = 1;
                        }
                    }
                }
                if (flag == 1) {
                    flag = 0;
                } else {
                    flagg = 0;
                    break;
                }
            }
            if (flagg == 1) {
                break;
            }
        }
        return (flagg == 1);
    }

    public VarSet getKey(VarSet obj) {
        int flag = 0;
        int flagg = 0;
        VarSet result = new VarSet();
        Set<VarSet> var = this.exTerms.keySet();
        HashMap<String, Integer> objVarSet = obj.get();
        Set<String> objStr = objVarSet.keySet();
        for (VarSet v : var) {
            HashMap<String, Integer> mine = v.get();
            Set<String> mineStr = mine.keySet();
            if (mineStr.size() != objStr.size()) {
                continue;
            }
            flagg = 1;
            for (String s : mineStr) {
                for (String objS : objStr) {
                    if (s.equals(objS)) {
                        if (mine.get(s).equals(objVarSet.get(objS))) {
                            flag = 1;
                        }
                    }
                }
                if (flag == 1) {
                    flag = 0;
                } else {
                    flagg = 0;
                    break;
                }
            }
            if (flagg == 1) {
                result = v;
                break;
            }
        }
        return result;
    }

    public Expansion clone() {
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
}
