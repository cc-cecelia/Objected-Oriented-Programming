import java.util.ArrayList;

public class Sequence {
    private static final Sequence INSTANCE = new Sequence();

    public static Sequence getInstance() {
        return INSTANCE;
    }

    public void queryInfo(String date) {
        System.out.println("(Sequence) " + date + " Library sends a message to Machine");
    }

    public void getInfo(String date) {
        System.out.println("(Sequence) " + date + " Machine sends a message to Library");
    }

    public void refused(String date) {
        System.out.println("(Sequence) " + date + " Machine sends a message to Library");
    }

    public void succeed(String date) {
        System.out.println("(Sequence) " + date + " Machine sends a message to someone");
    }

    public void order(String date, ArrayList<String> output) {
        String str = ("(Sequence) " + date + " Library sends a message to ReserveLib");
        output.add(str);
    }
}
