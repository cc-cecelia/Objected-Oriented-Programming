import javafx.util.Pair;

import java.util.ArrayList;

public class SortLib {
    private int id;

    public SortLib(int id) {
        this.id = id;
    }

    public ArrayList<Pair<String, String>> sortBooks() {
        return BookSys.getIns().satisfyOrder();
    }
}
