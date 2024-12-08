import expression.Expansion;
import expression.Expr;

import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        //initialize
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        //parse
        Expr expr = parser.parseExpr();
        Expansion result = expr.toExpansion();
        //print
        System.out.println(result);
    }
}
