package expression;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class VarSet implements Serializable {
    private HashMap<String, Integer> variable = new HashMap<>();

    public void putVar(String str, Integer expo) {
        this.variable.put(str, expo);
    }

    public void addVar(String str, Integer expo) {
        if (this.variable.containsKey(str)) {
            Integer preExpo = this.variable.get(str);
            int preExpoInt = preExpo.intValue();
            int expoInt = expo.intValue();
            expoInt += preExpoInt;
            Integer expoNow = Integer.valueOf(expoInt);
            this.variable.put(str, expoNow);
        } else {
            this.variable.put(str, expo);
        }
    }

    public HashMap<String, Integer> get() {
        return this.variable;
    }

    public VarSet multiple(VarSet next) {
        HashMap<String, Integer> mapNext = next.get();
        Set<String> strNext = mapNext.keySet();
        //deep copy
        VarSet result = new VarSet();
        for (String ss : this.variable.keySet()) {
            result.putVar(ss, this.variable.get(ss));
        }
        //iterate
        for (String s : strNext) {
            if (result.variable.containsKey(s)) {
                int expo = result.variable.get(s);
                int expoNext = mapNext.get(s);
                expo += expoNext;
                result.variable.replace(s, expo);
            } else {
                result.variable.put(s, mapNext.get(s));
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
                sb.append("*");
                sb.append(nextElement);
                if (expo != 1) {
                    sb.append("**");
                    sb.append(expo);
                }
            }
        }
        return sb.toString();
    }

}
