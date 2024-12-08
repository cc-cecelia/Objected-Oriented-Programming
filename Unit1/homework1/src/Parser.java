import expression.Number;
import expression.*;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;
    private static final String PATTERN_TERM = "^\\d+$";
    public static final Pattern RE = Pattern.compile(PATTERN_TERM);
    private static final String PATTERN_TERM_CHAR = "^[a-z]+$";
    public static final Pattern RE_CHAR = Pattern.compile(PATTERN_TERM_CHAR);

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
        Matcher matcher = RE.matcher(lexer.peek());
        Matcher matcherChar = RE_CHAR.matcher(lexer.peek());
        int sign = 1;
        while (lexer.peek().indexOf('+') != -1 || lexer.peek().indexOf('-') != -1) {
            if (lexer.peek().indexOf('-') != -1) {
                sign *= -1;
            }
            lexer.next();
            matcher = RE.matcher(lexer.peek());
            matcherChar = RE_CHAR.matcher(lexer.peek());
        }
        if (matcher.matches()) {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num, sign);
        } else if (matcherChar.matches()) {
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
        } else {
            return new Number(new BigInteger(String.valueOf(0)), 1);
        }
    }
}
