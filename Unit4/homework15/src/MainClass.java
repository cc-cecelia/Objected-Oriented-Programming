import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        UnionSys.getInstance().initialize();
        Scanner scanner = new Scanner(System.in);
        UnionSys.getInstance().setSchool(scanner);
        UnionSys.getInstance().operate(scanner);
        /* Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        MyCalendar calendar = new MyCalendar();
        for (int i = 0; i < n; i++) {
            String tmp = scanner.nextLine();
            String book = tmp.substring(0, 6);
            int num;
            num = Integer.parseInt(tmp.substring(7));
            Library.getInstance().store(book, num);
        }
        int m = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < m; i++) {
            String tmp = scanner.nextLine();
            String[] info = tmp.split(" ");
            String opDate = info[0].substring(1, 11);
            calendar.update(LocalDate.parse(opDate));
            //cases
            if (info[2].equals("borrowed")) {
                Library.getInstance().borrow(info);
            } else if (info[2].equals("returned")) {
                Library.getInstance().returned(info);
            } else if (info[2].equals("smeared")) {
                Library.getInstance().smear(info);
            } else if (info[2].equals("lost")) {
                Library.getInstance().lose(info);
            }
        }*/
        // calendar.end();
    }
}
