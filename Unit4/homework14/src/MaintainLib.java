public class MaintainLib {
    private String name;
    private BookSys bookSys;

    public MaintainLib(String name, BookSys bookSys) {
        this.name = name;
        this.bookSys = bookSys;
    }

    public void maintain(String[] info, OutBook o) {
        if (o == null) {
            System.out.println(info[0] + " " + name + "-" + info[3]
                    + " got repaired by logistics division in " + name);
            BookState.getInstance().repaired(info[0], info[3]);
            bookSys.maintain(info[3]);
        } else {
            System.out.println(info[0] + " " + o.getSrc() + "-" + info[3]
                    + " got repaired by logistics division in " + name);
            BookState.getInstance().repaired(info[0], info[3]);
        }
    }
}