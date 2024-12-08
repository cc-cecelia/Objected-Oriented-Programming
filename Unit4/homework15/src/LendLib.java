public class LendLib {
    private String name;
    private BookSys bookSys;

    public LendLib(String name, BookSys bookSys) {
        this.name = name;
        this.bookSys = bookSys;
    }

    public void lend(String[] info) {
        String[] stuInfo = info[1].split("-");
        if (bookSys.checkStuBbook(info[1], info[3], info[0].substring(1, 11))) {
            System.out.println(info[0] + " borrowing and returning librarian lent "
                    + stuInfo[0] + "-" + info[3] + " to " + info[1]);
            BookState.getInstance().lent(info[0], info[3]);
            Sequence.getInstance().succeed(info[0]);
            System.out.println(info[0] + " " + info[1] + " borrowed "
                    + stuInfo[0] + "-" + info[3] + " from borrowing and returning librarian");
        } else {
            System.out.println(info[0] + " borrowing and returning librarian refused lending "
                    + stuInfo[0] + "-" + info[3] + " to " + info[1]);
            BookState.getInstance().refused(info[0], info[3]);
            Sequence.getInstance().refused(info[0]);
        }
    }

    public void lose(String[] info) {
        bookSys.lose(info[1], info[3]);
        System.out.println(info[0] + " " + info[1]
                + " got punished by borrowing and returning librarian");
        System.out.println(info[0] + " borrowing and returning librarian received "
                + info[1] + "'s fine");
    }

    public OutBook returned(String[] info, boolean smeared, boolean expired) {
        if (smeared || expired) {
            System.out.println(info[0] + " " + info[1]
                    + " got punished by borrowing and returning librarian");
            System.out.println(info[0] + " borrowing and returning librarian received "
                    + info[1] + "'s fine");
        }
        OutBook o = bookSys.returnBbook(info[1], info[3]);
        if (o == null) {
            System.out.println(info[0] + " " + info[1] + " returned " + name + "-" + info[3]
                    + " to borrowing and returning librarian");
            System.out.println(info[0] + " borrowing and returning librarian collected "
                    + name + "-" + info[3] + " from " + info[1]);
            BookState.getInstance().returned(info[0], info[3]);
        } else {
            System.out.println(info[0] + " " + info[1] + " returned " + o.getSrc() + "-" + info[3]
                    + " to borrowing and returning librarian");
            System.out.println(info[0] + " borrowing and returning librarian collected "
                    + o.getSrc() + "-" + info[3] + " from " + info[1]);
            BookState.getInstance().returned(info[0], info[3]);
        }
        return o;
    }

    public void punished(String[] info) {
        System.out.println(info[0] + " " + info[1]
                + " got punished by borrowing and returning librarian");
        System.out.println(info[0] + " borrowing and returning librarian received "
                + info[1] + "'s fine");
    }
}
