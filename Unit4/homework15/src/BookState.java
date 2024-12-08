public class BookState {
    private static final BookState INSTANCE = new BookState();

    public static BookState getInstance() {
        return INSTANCE;
    }

    public void refused(String date, String book) {
        System.out.println("(State) " + date + " " + book +  " transfers from Available to Moved");
    }

    public void repaired(String date, String book) {
        System.out.println("(State) " + date + " " + book +  " transfers from Moved to Moved");
    }

    public void received(String date, String book) {
        System.out.println("(State) [" + date + "] " + book +  " transfers from Moved to Moved");
    }

    public void transported(String date, String book) {
        System.out.println("(State) [" + date + "] " + book
                +  " transfers from Available to Available");
    }

    public void lent(String date, String book) {
        System.out.println("(State) " + date + " " + book +  " transfers from Available to Lent");
    }

    public void returned(String date, String book) {
        System.out.println("(State) " + date + " " + book +  " transfers from Lent to Moved");
    }

    public void sorted() {}
}
