package expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;
    private static final String PATTERN_TERM = "^\\d+$";
    public static final Pattern RE = Pattern.compile(PATTERN_TERM);
    private static final String PATTERN_TERM_XYZ = "^[x-z]$";
    public static final Pattern RE_XYZ = Pattern.compile(PATTERN_TERM_XYZ);
    private static final String PATTERN_TERM_FGH = "^[f-h]$";
    public static final Pattern RE_FGH = Pattern.compile(PATTERN_TERM_FGH);

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        int expo;
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addTerm(parseTerm());
        }
        if (lexer.peek().equals(")")) {
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                expo = Integer.parseInt(lexer.peek());
                expr.addExpo(expo);
                lexer.next();
                return expr;
            }
        }
        expr.addExpo(1);
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        int sign = 1;
        while (lexer.peek().indexOf('+') != -1 || lexer.peek().indexOf('-') != -1) {
            if (lexer.peek().indexOf('-') != -1) {
                sign *= -1;
            }
            lexer.next();
        }
        Matcher matcher = RE.matcher(lexer.peek());
        Matcher matcherXyz = RE_XYZ.matcher(lexer.peek());
        Matcher matcherFgh = RE_FGH.matcher(lexer.peek());
        if (matcher.matches()) {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num, sign);
        } else if (matcherXyz.matches()) {
            String var = lexer.peek();
            int expo;
            lexer.next();
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                expo = Integer.parseInt(lexer.peek());
                lexer.next();
                return new Power(var, expo, sign);
            }
            return new Power(var, 1, sign);
        } else if (lexer.peek().equals("(")) {
            lexer.next();
            Expr expr = parseExpr();
            expr.addSign(sign);
            return expr;
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            if (lexer.peek().equals("sin")) {
                return parseSin(sign);
            } else {
                return parseCos(sign);
            }
        } else if (matcherFgh.matches()) {
            return parseFunc(sign);
        } else if (lexer.peek().charAt(0) == 'd') {
            char para = lexer.peek().charAt(1);
            return parseDerivation(para, sign);
        }
        return new Number(new BigInteger(String.valueOf(0)), 1);
    }

    public Factor parseFunc(int sign) {
        String name = lexer.peek();
        int paraNum = FormalFunc.getNum(name);
        lexer.next();
        //"(" here
        lexer.next();
        ArrayList<Factor> actPara = new ArrayList<>();
        for (int i = 0; i < paraNum; i++) {
            actPara.add(parseFactor());
            //")" or "," here
            lexer.next();
        }
        return new Function(FormalFunc.actualFunc(name, actPara), sign);
    }

    public CosTrigono parseCos(int sign) {
        int expo;
        CosTrigono cos = new CosTrigono();
        lexer.next();
        //"(" here
        lexer.next();
        Factor arg = parseFactor();
        cos.addFactor(arg);
        cos.addSign(sign);
        //")" here
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            expo = Integer.parseInt(lexer.peek());
            cos.addExpo(expo);
            lexer.next();
            return cos;
        }
        cos.addExpo(1);
        return cos;
    }

    public SinTrigono parseSin(int sign) {
        int expo;
        SinTrigono sin = new SinTrigono();
        lexer.next();
        //"(" here
        lexer.next();
        Factor arg = parseFactor();
        sin.addFactor(arg);
        sin.addSign(sign);
        //")" here
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            expo = Integer.parseInt(lexer.peek());
            sin.addExpo(expo);
            lexer.next();
            return sin;
        }
        sin.addExpo(1);
        return sin;
    }

    public Derivation parseDerivation(char para, int sign) {
        lexer.next();
        //curToken is "(" here
        lexer.next();
        Derivation derivation = new Derivation();
        Expr expression = parseExpr();
        expression.addSign(sign);
        derivation.addSign(sign);
        derivation.addPara(para);
        derivation.addExpr(expression);
        return derivation;
    }
}