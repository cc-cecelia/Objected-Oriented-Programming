import javafx.util.Pair;

import java.util.ArrayList;

public class Library {
    private String name;
    private Machine machine;
    private LendLib lend;
    private ReserveLib reserve;
    private MaintainLib maintain;
    private SortLib sort;
    private PurchaseLib purchase;
    private BookSys bookSys;

    public Library(String name) {
        this.name = name;
        this.bookSys = new BookSys();
        this.machine = new Machine(name, bookSys);
        this.lend = new LendLib(name, bookSys);
        this.reserve = new ReserveLib(name, bookSys);
        this.maintain = new MaintainLib(name, bookSys);
        this.sort = new SortLib(1, bookSys);
        this.purchase = new PurchaseLib(name, bookSys);
    }

    public boolean checkName(String name) {
        return this.name.equals(name);
    }

    public void store(String[] info) {
        bookSys.store(info);
    }

    public boolean borrow(String[] info) {
        machine.check(info);
        if (info[3].charAt(0) == 'B') {
            int num = bookSys.getBnum(info[3]);
            if (num > 0) {
                bookSys.bnumMinus(info[3]);
                lend.lend(info);
                return true;
            } else {
                // reserve.orderBbook(info);
                return false;
            }
        } else if (info[3].charAt(0) == 'C') {
            int num = bookSys.getCnum(info[3]);
            if (num > 0) {
                bookSys.cnumMinus(info[3]);
                machine.lend(info);
                return true;
            } else {
                // reserve.orderCbook(info);
                return false;
            }
        }
        return false;
    }

    public void lose(String[] info) {
        lend.lose(info);
    }

    public void smear(String[] info) {
        bookSys.smeared(info[1], info[3]);
    }

    public void returned(String[] info) {
        boolean smeared = bookSys.checkSmeared(info[1], info[3]);
        OutBook o = null;
        if (info[3].charAt(0) == 'B') {
            o = lend.returned(info, smeared);
        } else if (info[3].charAt(0) == 'C') {
            if (smeared) {
                lend.punished(info);
            }
            o = machine.returned(info);
        }
        if (smeared) {
            maintain.maintain(info, o);
        }
        if (o != null) {
            UnionSys.getInstance().returned(name, o.getSrc(), o.getBook(), o.getStuId());
        }
    }

    public void sort(String date) {
        ArrayList<Pair<String, String>> ans = sort.sortBooks(purchase.getPurchaseNum());
        purchase.clear();
        reserve.satisfyOrders(ans, date);
    }

    public BookSys getBookSys() {
        return bookSys;
    }

    public boolean ifHaveBook(String book) {
        return bookSys.ifHaveBook(book);
    }

    public boolean checkIfHad(String stuId, String book) {
        return bookSys.pureCheck(stuId, book);
    }

    public String getName() {
        return name;
    }

    public void reserve(String[] info) {
        if (info[3].charAt(0) == 'B') {
            reserve.orderBbook(info);
        } else if (info[3].charAt(0) == 'C') {
            reserve.orderCbook(info);
        }
    }

    public void addPurchase(String book) {
        if (bookSys.getBnum(book) == -1 && book.charAt(0) == 'B') {
            purchase.addPurchase(book);
        } else if (bookSys.getCnum(book) == -1 && book.charAt(0) == 'C') {
            purchase.addPurchase(book);
        }
    }

    public ReserveLib getReserveLib() {
        return reserve;
    }

    public void purchase(String date) {
        purchase.purchase(date);
    }

    public void receive(String book) {
        purchase.receive(book);
    }
}
