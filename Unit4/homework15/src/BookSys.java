import javafx.util.Pair;

import java.util.*;

public class BookSys {
    private ArrayList<String> sharedBooks;
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
    private ArrayList<OutBook> outBooks;
    private HashMap<Pair<String, String>, String> date;

    public BookSys() {
        this.sharedBooks = new ArrayList<>();
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
        this.outBooks = new ArrayList<>();
        this.date = new HashMap<>();
    }

    public void store(String[] info) {
        int num = Integer.parseInt(info[1]);
        if (info[0].charAt(0) == 'A') {
            // abooks.put(book, num);
        } else if (info[0].charAt(0) == 'B') {
            bbooks.put(info[0], num);
        } else if (info[0].charAt(0) == 'C') {
            cbooks.put(info[0], num);
        }
        if (info[2].equals("Y") && info[0].charAt(0) != 'A') {
            sharedBooks.add(info[0]);
        }
    }

    public int getBnum(String book) {
        return bbooks.getOrDefault(book, -1);
    }

    public void bnumMinus(String book) {
        int tmp = bbooks.get(book);
        bbooks.replace(book, tmp - 1);
    }

    public int getCnum(String book) {
        return cbooks.getOrDefault(book, -1);
    }

    public void cnumMinus(String book) {
        int tmp = cbooks.get(book);
        cbooks.replace(book, tmp - 1);
    }

    public boolean checkStuBbook(String stuId, String book, String date) {
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
            this.date.put(new Pair<>(stuId, book), date);
            /* for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getKey().equals(stuId)
                        && orderList.get(i).getValue().charAt(0) == 'B') {
                    orderList.remove(i);
                    i--;
                }
            }*/
            return true;
        }
    }

    public boolean checkStuCbook(String stuId, String book, String date) {
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
            this.date.put(new Pair<>(stuId, book), date);
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
        if (!pureCheck(stuId, book)) {
            return false;
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
        date.remove(new Pair<>(stuId, book));
    }

    public void smeared(String stuId, String book) {
        smear.put(new Pair<>(stuId, book), true);
    }

    public boolean checkSmeared(String stuId, String book) {
        return smear.containsKey(new Pair<>(stuId, book));
    }

    public boolean checkExpired(String stuId, String book) {
        String date = this.date.get(new Pair<>(stuId, book));
        if (book.charAt(0) == 'B') {
            return UnionSys.getInstance().checkbExpired(date);
        } else if (book.charAt(0) == 'C') {
            return UnionSys.getInstance().checkcExpired(date);
        }
        return false;
    }

    public OutBook returnBbook(String stuId, String book) {
        stuBbook.remove(stuId);
        boolean flag = false;
        OutBook o = null;
        for (int i = 0; i < outBooks.size(); i++) {
            o = outBooks.get(i);
            if (o.getBook().equals(book) && o.getStuId().equals(stuId)) {
                flag = true;
                outBooks.remove(i);
                break;
            }
        }
        date.remove(new Pair<>(stuId, book));
        if (checkSmeared(stuId, book)) {
            smear.remove(new Pair<>(stuId, book));
            if (!flag) { return null; }
        } else if (!flag) {
            if (lendLib.containsKey(book)) {
                int tmp = lendLib.get(book);
                lendLib.replace(book, tmp + 1);
            } else {
                lendLib.put(book, 1);
            }
            return null;
        }
        return o;
    }

    public OutBook returnCbook(String stuId, String book) {
        stuCbook.get(stuId).remove(book);
        if (stuCbook.get(stuId).isEmpty()) {
            stuCbook.remove(stuId);
        }
        boolean flag = false;
        OutBook o = null;
        for (int i = 0; i < outBooks.size(); i++) {
            o = outBooks.get(i);
            if (o.getBook().equals(book) && o.getStuId().equals(stuId)) {
                flag = true;
                outBooks.remove(i);
                break;
            }
        }
        date.remove(new Pair<>(stuId, book));
        if (checkSmeared(stuId, book)) {
            smear.remove(new Pair<>(stuId, book));
            if (!flag) { return null; }
        } else if (!flag) {
            if (machine.containsKey(book)) {
                int tmp = machine.get(book);
                machine.replace(book, tmp + 1);
            } else {
                machine.put(book, 1);
            }
            return null;
        }
        return o;
    }

    public void maintain(String book) {
        if (maintain.containsKey(book)) {
            int tmp = maintain.get(book);
            maintain.replace(book, tmp + 1);
        } else {
            maintain.put(book, 1);
        }
    }

    public ArrayList<Pair<String, String>> satisfyOrder(HashMap<String, Integer> purchaseNum
            , String date) {
        ArrayList<Pair<String, String>> ans = new ArrayList<>();
        // collect all the returned books
        HashMap<String, Integer> returnedBook = new HashMap<>(lendLib);
        merge(returnedBook, purchaseNum);
        // check if any order in the orderList can be satisfied
        for (int i = 0; i < orderList.size(); i++) {
            String stuId = orderList.get(i).getKey();
            Iterator<Map.Entry<String, Integer>> iter = returnedBook.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> tmp = iter.next();
                String book = tmp.getKey();
                if (orderList.get(i).getValue().equals(book)) {
                    if (!pureCheck(stuId, orderList.get(i).getValue())) {
                        orderList.remove(i);
                        i--;
                    } else {
                        // add into ans
                        ans.add(orderList.get(i));
                        // change the number in returnedBook
                        if (tmp.getValue() == 1) { iter.remove(); } else {
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
                    }
                    break;
                }
            }
        }
        returnToShelf(returnedBook);
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
            this.date.put(p, date);
        }
        return ans;
    }

    public void merge(HashMap<String, Integer> returnedBook, HashMap<String, Integer> purchaseNum) {
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
        for (String book : purchaseNum.keySet()) {
            if (returnedBook.containsKey(book)) {
                int tmp = returnedBook.get(book);
                returnedBook.replace(book, tmp + purchaseNum.get(book));
            } else {
                returnedBook.put(book, purchaseNum.get(book));
            }
        }
        lendLib.clear();
        machine.clear();
        maintain.clear();
    }

    public void returnToShelf(HashMap<String, Integer> returnedBook) {
        // return the books into the bookShelf
        for (String book : returnedBook.keySet()) {
            if (book.charAt(0) == 'B') {
                if (bbooks.containsKey(book)) {
                    int tmpp = bbooks.get(book);
                    bbooks.put(book, tmpp + returnedBook.get(book));
                } else {
                    bbooks.put(book, returnedBook.get(book));
                }
            } else if (book.charAt(0) == 'C') {
                if (cbooks.containsKey(book)) {
                    int tmpp = cbooks.get(book);
                    cbooks.put(book, tmpp + returnedBook.get(book));
                } else {
                    cbooks.put(book, returnedBook.get(book));
                }
            }
        }
    }

    public boolean ifHaveBook(String book) {
        if (sharedBooks.contains(book) && book.charAt(0) == 'B') {
            if (bbooks.containsKey(book)) {
                return (bbooks.get(book) > 0);
            }
        } else if (sharedBooks.contains(book) && book.charAt(0) == 'C') {
            if (cbooks.containsKey(book)) {
                return (cbooks.get(book) > 0);
            }
        }
        return false;
    }

    public boolean pureCheck(String stuId, String book) {
        if (book.charAt(0) == 'B' && stuBbook.containsKey(stuId)) {
            return false;
        } else if (book.charAt(0) == 'C') {
            if (stuCbook.containsKey(stuId)) {
                return !stuCbook.get(stuId).contains(book);
            }
        }
        return true;
    }

    public void addOutBooks(String book, String stuId, String src, String date) {
        OutBook tmp = new OutBook(book, stuId, src);
        outBooks.add(tmp);
        this.date.put(new Pair<>(stuId, book), date);
        if (book.charAt(0) == 'B') {
            stuBbook.put(stuId, book);
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getKey().equals(stuId)
                        && orderList.get(i).getValue().charAt(0) == 'B') {
                    orderList.remove(i);
                    i--;
                }
            }
        } else if (book.charAt(0) == 'C') {
            if (stuCbook.containsKey(stuId)) {
                stuCbook.get(stuId).add(book);
            } else {
                HashSet<String> tmpp = new HashSet<>();
                tmpp.add(book);
                stuCbook.put(stuId, tmpp);
            }
        }
    }

    public void updateSharedBooks(ArrayList<String> list) {
        for (String book : list) {
            if (!sharedBooks.contains(book)) {
                sharedBooks.add(book);
            }
        }
    }
}
