import javafx.util.Pair;

import java.util.ArrayList;

public class ReserveLib {
    private String name;
    private BookSys bookSys;
    private ArrayList<String> output;

    public ReserveLib(String name, BookSys bookSys) {
        this.name = name;
        this.bookSys = bookSys;
        this.output = new ArrayList<>();
    }

    public void orderBbook(String[] info) {
        if (bookSys.orderBook(info[1], info[3])) {
            String str = (info[0] + " " + info[1] + " ordered "
                    + name + "-" + info[3] + " from ordering librarian");
            output.add(str);
            String strr = (info[0] + " ordering librarian recorded " + info[1]
                    + "'s order of " + name + "-" + info[3]);
            output.add(strr);
        }
    }

    public void orderCbook(String[] info) {
        if (bookSys.orderBook(info[1], info[3])) {
            String str = (info[0] + " " + info[1] + " ordered "
                    + name + "-" + info[3] + " from ordering librarian");
            output.add(str);
            String strr = (info[0] + " ordering librarian recorded " + info[1]
                    + "'s order of " + name + "-" + info[3]);
            output.add(strr);
        }
    }

    public void satisfyOrders(ArrayList<Pair<String, String>> ans, String date) {
        for (int i = 0; i < ans.size(); i++) {
            System.out.println("[" + date + "] ordering librarian lent " + name
                    + "-" + ans.get(i).getValue() + " to " + ans.get(i).getKey());
            BookState.getInstance().lent("[" + date + "]", ans.get(i).getValue());
            System.out.println("[" + date + "] " + ans.get(i).getKey() + " borrowed "
                    + name + "-" + ans.get(i).getValue() + " from ordering librarian");
        }
    }

    public void printOrder() {
        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i));
        }
        output.clear();
    }
}
