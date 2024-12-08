import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        UnionSys.getInstance().initialize();
        Scanner scanner = new Scanner(System.in);
        UnionSys.getInstance().setSchool(scanner);
        UnionSys.getInstance().operate(scanner);

    }
}
