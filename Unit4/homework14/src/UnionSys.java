import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class UnionSys {
    private static final UnionSys INSTANCE = new UnionSys();
    private ArrayList<Library> libraries;
    private MyCalendar calendar;
    private ArrayList<String[]> orderList;
    private ArrayList<InterShip> interShips;

    public void initialize() {
        this.libraries = new ArrayList<>();
        this.calendar = new MyCalendar();
        this.orderList = new ArrayList<>();
        this.interShips = new ArrayList<>();
    }

    public static UnionSys getInstance() {
        return INSTANCE;
    }

    public void setSchool(Scanner scanner) {
        int t = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < t; i++) {
            String tmp = scanner.nextLine();
            String[] info = tmp.split(" ");
            Library newLib = new Library(info[0]);
            libraries.add(newLib);
            int n = Integer.parseInt(info[1]);
            for (int j = 0; j < n; j++) {
                String books = scanner.nextLine();
                String[] bookInfo = books.split(" ");
                newLib.store(bookInfo);
            }
        }
    }

    public void operate(Scanner scanner) {
        int m = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < m; i++) {
            String tmp = scanner.nextLine();
            if (i == 0) {
                System.out.println("[2023-01-01] arranging librarian arranged all the books");
            }
            String[] info = tmp.split(" ");
            String[] stuInfo = info[1].split("-");
            int j;
            for (j = 0; j < libraries.size(); j++) {
                if (libraries.get(j).checkName(stuInfo[0])) {
                    break;
                }
            }
            String opDate = info[0].substring(1, 11);
            calendar.update(LocalDate.parse(opDate));
            //cases
            switch (info[2]) {
                case "borrowed":
                    if (!libraries.get(j).borrow(info)) {
                        orderList.add(info);
                    }
                    break;
                case "returned":
                    libraries.get(j).returned(info);
                    break;
                case "smeared":
                    libraries.get(j).smear(info);
                    break;
                case "lost":
                    libraries.get(j).lose(info);
                    break;
                default:
            }
        }
        calendar.endLastDay();
        // calendar.end();
    }

    public void reserve() {
        for (int i = 0; i < orderList.size(); i++) {
            String[] info = orderList.get(i);
            // deal with the same request
            boolean flag = false;
            for (int j = 0; j < i; j++) {
                if (orderList.get(j)[1].equals(info[1]) && orderList.get(j)[3].equals(info[3])) {
                    flag = true;
                    break;
                }
            }
            if (flag) { continue; }
            // check if already have this kind of book
            String[] stuInfo = info[1].split("-");
            int j;
            for (j = 0; j < libraries.size(); j++) {
                if (libraries.get(j).checkName(stuInfo[0])) {
                    break;
                }
            }
            if (!libraries.get(j).checkIfHad(info[1], info[3])) {
                continue;
            }
            // inside order
            if (!reserveFromOthers(info, j)) {
                libraries.get(j).reserve(info);
                libraries.get(j).addPurchase(info[3]);
            }
        }
        orderList.clear();
    }

    public boolean reserveFromOthers(String[] info, int j) {
        for (int i = 0; i < libraries.size(); i++) {
            if (i == j) {
                continue;
            }
            if (libraries.get(i).ifHaveBook(info[3])) {
                // check interships before
                boolean flag = true;
                if (info[3].charAt(0) == 'B') {
                    for (InterShip in : interShips) {
                        if (in.getStuId().equals(info[1]) && in.getBook().charAt(0) == 'B'
                                && in.getType().equals("lend")) {
                            flag = false;
                            break;
                        }
                    }
                } else if (info[3].charAt(0) == 'C') {
                    for (InterShip in : interShips) {
                        if (in.getStuId().equals(info[1]) && in.getBook().equals(info[3])
                                && in.getType().equals("lend")) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    InterShip tmp = new InterShip(i, j, info[3], info[1], "lend");
                    interShips.add(tmp);
                }
                return true;
            }
        }
        return false;
    }

    public void returned(String src, String dst, String book, String stuId) {
        int srcNum = -1;
        int dstNum = -1;
        for (int i = 0; i < libraries.size(); i++) {
            if (libraries.get(i).getName().equals(src)) {
                srcNum = i;
            } else if (libraries.get(i).getName().equals(dst)) {
                dstNum = i;
            }
        }
        InterShip in = new InterShip(srcNum, dstNum, book, stuId, "return");
        interShips.add(in);
    }

    public void printForToday(String date) {
        for (int i = 0; i < libraries.size(); i++) {
            libraries.get(i).getReserveLib().printOrder();
        }
        for (int i = 0; i < interShips.size(); i++) {
            int src = interShips.get(i).getSrc();
            int dst = interShips.get(i).getDst();
            String book = interShips.get(i).getBook();
            if (interShips.get(i).getType().equals("return")) {
                System.out.println("[" + date + "] " + libraries.get(dst).getName() + "-" + book
                        + " got transported by purchasing department in "
                        + libraries.get(src).getName());
                BookState.getInstance().transported(date, book);
            } else {
                if (libraries.get(src).ifHaveBook(book)) {
                    if (book.charAt(0) == 'B') {
                        libraries.get(src).getBookSys().bnumMinus(book);
                    } else if (book.charAt(0) == 'C') {
                        libraries.get(src).getBookSys().cnumMinus(book);
                    }
                    System.out.println("[" + date + "] " + libraries.get(src).getName() + "-"
                            + book + " got transported by purchasing department in "
                            + libraries.get(src).getName());
                    BookState.getInstance().transported(date, book);
                } else {
                    interShips.remove(i);
                    i--;
                }
            }
        }
    }

    public void printForNextDay(String date) {
        ArrayList<String> borrow = new ArrayList<>();
        for (int i = 0; i < interShips.size(); i++) {
            String src = libraries.get(interShips.get(i).getSrc()).getName();
            int dst = interShips.get(i).getDst();
            String book = interShips.get(i).getBook();
            String stuId = interShips.get(i).getStuId();
            if (interShips.get(i).getType().equals("return")) {
                System.out.println("[" + date + "] " + libraries.get(dst).getName() + "-" + book
                        + " got received by purchasing department in "
                        + libraries.get(dst).getName());
                BookState.getInstance().received(date, book);
                libraries.get(dst).receive(book);
            } else {
                System.out.println("[" + date + "] " + src + "-" + book +
                        " got received by purchasing department in "
                        + libraries.get(dst).getName());
                BookState.getInstance().received(date, book);
                libraries.get(dst).getBookSys().addOutBooks(book, stuId, src);
                String str = ("[" + date + "] purchasing department lent " + src
                        + "-" + book + " to " + stuId);
                String strrr = ("(State) [" + date + "] " + book
                        +  " transfers from Available to Lent");
                String strr = ("[" + date + "] " + stuId + " borrowed " + src
                        + "-" + book + " from purchasing department");
                borrow.add(str);
                borrow.add(strrr);
                borrow.add(strr);
            }
        }
        for (int i = 0; i < borrow.size(); i++) {
            System.out.println(borrow.get(i));
        }
    }

    public void cleanOrderTimes() {
        for (Library library : libraries) {
            library.getBookSys().cleanOrderTimes();
        }
    }

    public void sort(String date) {
        for (int i = 0; i < libraries.size(); i++) {
            libraries.get(i).sort(date);
        }
    }

    public void purchase(String date) {
        for (int i = 0; i < libraries.size(); i++) {
            libraries.get(i).purchase(date);
        }
    }

    public void clearInterships() {
        interShips.clear();
    }
}
