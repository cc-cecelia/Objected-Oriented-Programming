import javafx.util.Pair;

import java.util.ArrayList;

public class Library {
    private static final Library INSTANCE = new Library();
    private Machine machine;
    private LendLib lend;
    private ReserveLib reserve;
    private MaintainLib maintain;
    private SortLib sort;

    public void initialize() {
        this.machine = new Machine(1);
        this.lend = new LendLib(1);
        this.reserve = new ReserveLib(1);
        this.maintain = new MaintainLib(1);
        this.sort = new SortLib(1);
        BookSys.getIns().initialize();
    }

    public static Library getInstance() {
        return INSTANCE;
    }

    public void store(String book, int num) {
        BookSys.getIns().store(book, num);
    }

    public void borrow(String[] info) {
        machine.check(info);
        if (info[3].charAt(0) == 'B') {
            int num = BookSys.getIns().getBnum(info[3]);
            if (num > 0) {
                BookSys.getIns().bnumMinus(info[3]);
                lend.lend(info);
            } else {
                reserve.orderBbook(info);
            }
        } else if (info[3].charAt(0) == 'C') {
            int num = BookSys.getIns().getCnum(info[3]);
            if (num > 0) {
                BookSys.getIns().cnumMinus(info[3]);
                machine.lend(info);
            } else {
                reserve.orderCbook(info);
            }
        }
    }

    public void lose(String[] info) {
        lend.lose(info);
    }

    public void smear(String[] info) {
        BookSys.getIns().smeared(info[1], info[3]);
    }

    public void returned(String[] info) {
        boolean smeared = BookSys.getIns().checkSmeared(info[1], info[3]);
        if (info[3].charAt(0) == 'B') {
            lend.returned(info, smeared);
        } else if (info[3].charAt(0) == 'C') {
            if (smeared) {
                lend.punished(info);
            }
            machine.returned(info);
        }
        if (smeared) {
            maintain.maintain(info);
        }
    }

    public void sort(String date) {
        ArrayList<Pair<String, String>> ans = sort.sortBooks();
        reserve.satisfyOrders(ans, date);
    }
}
