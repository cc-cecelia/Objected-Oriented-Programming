import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class SortLib {
    private int id;
    private BookSys bookSys;

    public SortLib(int id, BookSys bookSys) {
        this.id = id;
        this.bookSys = bookSys;
    }

    public ArrayList<Pair<String, String>> sortBooks(HashMap<String, Integer> purchaseNum) {
        return bookSys.satisfyOrder(purchaseNum);
    }
}
