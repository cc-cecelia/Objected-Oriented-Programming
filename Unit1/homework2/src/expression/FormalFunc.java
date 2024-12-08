package expression;

import java.util.ArrayList;
import java.util.HashMap;

public class FormalFunc {
    private static HashMap<String, String> formalFunc = new HashMap<>();
    private static HashMap<String, Integer> varNum = new HashMap<>();
    private static int pos = 1;

    public static Integer getNum(String name) {
        return varNum.get(name);
    }

    public static void addFunc(String name, String replacedFunc, Integer num) {
        formalFunc.put(name, replacedFunc);
        varNum.put(name, num);
    }

    public static void myReplace(String input) {
        pos = 1;
        char name = input.charAt(0);
        skipBlank(input);
        //"(" here
        pos++;
        skipBlank(input);
        char a = input.charAt(pos);
        pos++;
        skipBlank(input);
        if (input.charAt(pos) == ')') {
            pos++;
            skipBlank(input);
            //"=" here
            pos++;
            skipBlank(input);
            String func = input.substring(pos);
            String reFunc = func.replace(a,'u');
            addFunc(String.valueOf(name), reFunc, 1);
        } else {
            //"," here
            pos++;
            skipBlank(input);
            char b = input.charAt(pos);
            pos++;
            skipBlank(input);
            if (input.charAt(pos) == ')') {
                pos++;
                skipBlank(input);
                //"=" here
                pos++;
                skipBlank(input);
                String func = input.substring(pos);
                String reFunc = func.replace(b,'v');
                String rereFunc = reFunc.replace(a, 'u');
                addFunc(String.valueOf(name), rereFunc, 2);
            } else {
                //"," here
                pos++;
                skipBlank(input);
                final char c = input.charAt(pos);
                pos++;
                skipBlank(input);
                //")" here
                pos++;
                skipBlank(input);
                //"=" here
                pos++;
                skipBlank(input);
                String func = input.substring(pos);
                String reFunc = func.replace(c,'w');
                String rereFunc = reFunc.replace(b, 'v');
                String rerereFunc = rereFunc.replace(a, 'u');
                addFunc(String.valueOf(name), rerereFunc, 3);
            }
        }
    }

    public static String actualFunc(String name, ArrayList<Factor> factors) {
        int num = varNum.get(name);
        String func = formalFunc.get(name);
        String reFunc = func.replaceAll("u", "(" + factors.get(0).toExpansion().toString() + ")");
        if (num == 1) {
            return reFunc;
        } else {
            String rereFunc =
                    reFunc.replaceAll("v", "(" + factors.get(1).toExpansion().toString() + ")");
            if (num == 2) {
                return rereFunc;
            } else {
                String tmp = "(" + factors.get(2).toExpansion().toString() + ")";
                return rereFunc.replaceAll("w", tmp);
            }
        }
    }

    public static void skipBlank(String input) {
        if (pos < input.length()) {
            char current = input.charAt(pos);
            while (" \t".indexOf(current) != -1) {
                pos++;
                if (pos < input.length()) {
                    current = input.charAt(pos);
                } else {
                    return;
                }
            }
        }
    }
}
