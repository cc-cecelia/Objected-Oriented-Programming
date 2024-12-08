public class LendLib {
    private int id;

    public LendLib(int id) {
        this.id = id;
    }

    public void lend(String[] info) {
        if (BookSys.getIns().checkStuBbook(info[1], info[3])) {
            System.out.println(info[0] + " " + info[1] + " borrowed "
                    + info[3] + " from borrowing and returning librarian");
        }
    }

    public void lose(String[] info) {
        BookSys.getIns().lose(info[1], info[3]);
        System.out.println(info[0] + " " + info[1]
                + " got punished by borrowing and returning librarian");
    }

    public void returned(String[] info, boolean tmp) {
        if (tmp) {
            System.out.println(info[0] + " " + info[1]
                    + " got punished by borrowing and returning librarian");
        }
        System.out.println(info[0] + " " + info[1] + " returned " + info[3]
                + " to borrowing and returning librarian");
        BookSys.getIns().returnBbook(info[1], info[3]);
    }

    public void punished(String[] info) {
        System.out.println(info[0] + " " + info[1]
                + " got punished by borrowing and returning librarian");
    }
}
