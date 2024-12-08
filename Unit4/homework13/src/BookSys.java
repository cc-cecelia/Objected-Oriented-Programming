import javafx.util.Pair;

import java.util.*;

public class BookSys {
    private static final BookSys INSTANCE = new BookSys();
    // private HashMap<String, Integer> abooks;
    private HashMap<String, Integer> bbooks;
    private HashMap<String, Integer> cbooks;
    private HashMap<String, String> stuBbook;
    private HashMap<String, HashSet<String>> stuCbook;
    private HashMap<Pair<String, String>, Boolean> smear;
    private HashMap<String, Integer> lendLib;
    private HashMap<String, Integer> machine;
    private HashMap<String, Integer> maintain;
    private ArrayList<Pair<String, String>> orderList;
    private HashMap<String, Integer> orderTimes;

    public void initialize() {
        // this.abooks = new HashMap<>();
        this.bbooks = new HashMap<>();
        this.cbooks = new HashMap<>();
        this.stuBbook = new HashMap<>();
        this.stuCbook = new HashMap<>();
        this.smear = new HashMap<>();
        this.lendLib = new HashMap<>();
        this.machine = new HashMap<>();
        this.maintain = new HashMap<>();
        this.orderList = new ArrayList<>();
        this.orderTimes = new HashMap<>();
    }

    public static BookSys getIns() {
        return INSTANCE;
    }

    public void store(String book, int num) {
        if (book.charAt(0) == 'A') {
            // abooks.put(book, num);
        } else if (book.charAt(0) == 'B') {
            bbooks.put(book, num);
        } else if (book.charAt(0) == 'C') {
            cbooks.put(book, num);
        }
    }

    public int getBnum(String book) {
        return bbooks.get(book);
    }

    public void bnumMinus(String book) {
        int tmp = bbooks.get(book);
        bbooks.replace(book, tmp - 1);
    }

    public int getCnum(String book) {
        return cbooks.get(book);
    }

    public void cnumMinus(String book) {
        int tmp = cbooks.get(book);
        cbooks.replace(book, tmp - 1);
    }

    public boolean checkStuBbook(String stuId, String book) {
        if (stuBbook.containsKey(stuId)) {
            if (lendLib.containsKey(book)) {
                int tmp = lendLib.get(book);
                lendLib.replace(book, tmp + 1);
            } else {
                lendLib.put(book, 1);
            }
            return false;
        } else {
            stuBbook.put(stuId, book);
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getKey().equals(stuId)
                        && orderList.get(i).getValue().charAt(0) == 'B') {
                    orderList.remove(i);
                    i--;
                }
            }
            return true;
        }
    }

    public boolean checkStuCbook(String stuId, String book) {
        if (stuCbook.containsKey(stuId) && stuCbook.get(stuId).contains(book)) {
            if (machine.containsKey(book)) {
                int tmp = machine.get(book);
                machine.replace(book, tmp + 1);
            } else {
                machine.put(book, 1);
            }
            return false;
        } else {
            if (stuCbook.containsKey(stuId)) {
                stuCbook.get(stuId).add(book);
            } else {
                HashSet<String> books = new HashSet<>();
                books.add(book);
                stuCbook.put(stuId, books);
            }
            return true;
        }
    }

    public boolean orderBook(String stuId, String book) {
        for (Pair<String, String> p : orderList) {
            if (p.getKey().equals(stuId) && p.getValue().equals(book)) {
                return false;
            }
        }
        if (orderTimes.containsKey(stuId)) {
            if (orderTimes.get(stuId) >= 3) {
                return false;
            }
        }
        if (book.charAt(0) == 'B' && stuBbook.containsKey(stuId)) {
            return false;
        } else if (book.charAt(0) == 'C') {
            if (stuCbook.containsKey(stuId)) {
                if (stuCbook.get(stuId).contains(book)) {
                    return false;
                }
            }
        }
        orderList.add(new Pair<>(stuId, book));
        if (orderTimes.containsKey(stuId)) {
            int tmp = orderTimes.get(stuId);
            orderTimes.put(stuId, tmp + 1);
        } else {
            orderTimes.put(stuId, 1);
        }
        return true;
    }

    public void cleanOrderTimes() {
        orderTimes.clear();
    }

    public void lose(String stuId, String book) {
        if (book.charAt(0) == 'B') {
            stuBbook.remove(stuId);
        } else if (book.charAt(0) == 'C') {
            stuCbook.get(stuId).remove(book);
            if (stuCbook.get(stuId).isEmpty()) {
                stuCbook.remove(stuId);
            }
        }
    }

    public void smeared(String stuId, String book) {
        smear.put(new Pair<>(stuId, book), true);
    }

    public boolean checkSmeared(String stuId, String book) {
        return smear.containsKey(new Pair<>(stuId, book));
    }

    public void returnBbook(String stuId, String book) {
        stuBbook.remove(stuId);
        if (checkSmeared(stuId, book)) {
            smear.remove(new Pair<>(stuId, book));
        } else {
            if (lendLib.containsKey(book)) {
                int tmp = lendLib.get(book);
                lendLib.replace(book, tmp + 1);
            } else {
                lendLib.put(book, 1);
            }
        }
    }

    public void returnCbook(String stuId, String book) {
        stuCbook.get(stuId).remove(book);
        if (stuCbook.get(stuId).isEmpty()) {
            stuCbook.remove(stuId);
        }
        if (checkSmeared(stuId, book)) {
            smear.remove(new Pair<>(stuId, book));
        } else {
            if (machine.containsKey(book)) {
                int tmp = machine.get(book);
                machine.replace(book, tmp + 1);
            } else {
                machine.put(book, 1);
            }
        }
    }

    public void maintain(String book) {
        if (maintain.containsKey(book)) {
            int tmp = maintain.get(book);
            maintain.replace(book, tmp + 1);
        } else {
            maintain.put(book, 1);
        }
    }

    public ArrayList<Pair<String, String>> satisfyOrder() {
        ArrayList<Pair<String, String>> ans = new ArrayList<>();
        // collect all the returned books
        HashMap<String, Integer> returnedBook = new HashMap<>(lendLib);
        merge(returnedBook);
        // check if any order in the orderList can be satisfied
        for (int i = 0; i < orderList.size(); i++) {
            String stuId = orderList.get(i).getKey();
            Iterator<Map.Entry<String, Integer>> iter = returnedBook.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> tmp = iter.next();
                String book = tmp.getKey();
                if (orderList.get(i).getValue().equals(book)) {
                    // add into ans
                    ans.add(orderList.get(i));
                    // change the number in returnedBook
                    if (tmp.getValue() == 1) {
                        iter.remove();
                    } else {
                        int tmpp = tmp.getValue();
                        returnedBook.replace(book, tmpp - 1);
                    }
                    // change in the orderList
                    if (book.charAt(0) == 'B') {
                        for (int j = 0; j < orderList.size(); j++) {
                            if (orderList.get(j).getKey().equals(stuId)
                                    && orderList.get(j).getValue().charAt(0) == 'B') {
                                orderList.remove(j);
                                if (j <= i) { i--; }
                                j--;
                            }
                        }
                    } else if (book.charAt(0) == 'C') {
                        orderList.remove(i);
                        i--;
                    }
                    break;
                }
            }
        }
        // return the books into the bookShelf
        for (String book : returnedBook.keySet()) {
            if (book.charAt(0) == 'B') {
                int tmpp = bbooks.get(book);
                bbooks.put(book, tmpp + returnedBook.get(book));
            } else if (book.charAt(0) == 'C') {
                int tmpp = cbooks.get(book);
                cbooks.put(book, tmpp + returnedBook.get(book));
            }
        }
        // change in the stu
        for (Pair<String, String> p : ans) {
            if (p.getValue().charAt(0) == 'B') {
                stuBbook.put(p.getKey(), p.getValue());
            } else if (p.getValue().charAt(0) == 'C') {
                if (stuCbook.containsKey(p.getKey())) {
                    stuCbook.get(p.getKey()).add(p.getValue());
                } else {
                    HashSet<String> tmp = new HashSet<>();
                    tmp.add(p.getValue());
                    stuCbook.put(p.getKey(), tmp);
                }
            }
        }
        return ans;
    }

    public void merge(HashMap<String, Integer> returnedBook) {
        for (String book : machine.keySet()) {
            if (returnedBook.containsKey(book)) {
                int tmp = returnedBook.get(book);
                returnedBook.replace(book, tmp + machine.get(book));
            } else {
                returnedBook.put(book, machine.get(book));
            }
        }
        for (String book : maintain.keySet()) {
            if (returnedBook.containsKey(book)) {
                int tmp = returnedBook.get(book);
                returnedBook.replace(book, tmp + maintain.get(book));
            } else {
                returnedBook.put(book, maintain.get(book));
            }
        }
        lendLib.clear();
        machine.clear();
        maintain.clear();
    }
}
