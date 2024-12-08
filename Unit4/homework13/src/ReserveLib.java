import javafx.util.Pair;

import java.util.ArrayList;

public class ReserveLib {
    private int id;

    public ReserveLib(int id) {
        this.id = id;
    }

    public void orderBbook(String[] info) {
        if (BookSys.getIns().orderBook(info[1], info[3])) {
            System.out.println(info[0] + " " + info[1] + " ordered "
                    + info[3] + " from ordering librarian");
        }
    }

    public void orderCbook(String[] info) {
        if (BookSys.getIns().orderBook(info[1], info[3])) {
            System.out.println(info[0] + " " + info[1] + " ordered "
                    + info[3] + " from ordering librarian");
        }
    }

    public void satisfyOrders(ArrayList<Pair<String, String>> ans, String date) {
        for (int i = 0; i < ans.size(); i++) {
            System.out.println("[" + date + "] " + ans.get(i).getKey()
                    + " borrowed " + ans.get(i).getValue() + " from ordering librarian");
        }
    }
}
