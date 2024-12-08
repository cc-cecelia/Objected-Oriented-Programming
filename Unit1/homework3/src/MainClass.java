import expression.*;

import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        //define function
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        int funcNum = Integer.parseInt(input);
        for (int i = 0; i < funcNum; i++) {
            String func = sc.nextLine();
            FormalFunc.myReplace(func);
        }
        //initialize
        String expression = sc.nextLine();
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);
        //parse
        Expr expr = parser.parseExpr();
        Expansion result = expr.toExpansion();
        //print
        System.out.println(result);
    }
}
