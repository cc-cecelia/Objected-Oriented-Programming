import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseLib {
    private String name;
    private BookSys bookSys;
    private HashMap<String, Integer> purchaseNum;
    private ArrayList<String> purchaseList;

    public PurchaseLib(String name, BookSys bookSys) {
        this.name = name;
        this.bookSys = bookSys;
        this.purchaseNum = new HashMap<>();
        this.purchaseList = new ArrayList<>();
    }

    public void addPurchase(String book) {
        if (!purchaseList.contains(book)) {
            purchaseList.add(book);
        }
        if (purchaseNum.containsKey(book)) {
            int tmp = purchaseNum.get(book);
            purchaseNum.replace(book, tmp + 1);
        } else {
            purchaseNum.put(book, 1);
        }
    }

    public void purchase(String date) {
        for (int i = 0; i < purchaseList.size(); i++) {
            String book = purchaseList.get(i);
            if (purchaseNum.get(book) < 3) {
                purchaseNum.replace(book, 3);
            }
            System.out.println("[" + date + "] " + name + "-" + book
                    + " got purchased by purchasing department in " + name);
        }
        updateSharedBooks();
    }

    public HashMap<String, Integer> getPurchaseNum() {
        return purchaseNum;
    }

    public void receive(String book) {
        if (purchaseNum.containsKey(book)) {
            int tmp = purchaseNum.get(book);
            purchaseNum.replace(book, tmp + 1);
        } else {
            purchaseNum.put(book, 1);
        }
    }

    public void clear() {
        purchaseNum.clear();
        purchaseList.clear();
    }

    public void updateSharedBooks() {
        bookSys.updateSharedBooks(purchaseList);
    }
}
