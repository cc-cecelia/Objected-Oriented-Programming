public class Machine {
    private String name;
    private BookSys bookSys;

    public Machine(String name, BookSys bookSys) {
        this.name = name;
        this.bookSys = bookSys;
    }

    public void check(String[] info) {
        System.out.println(info[0] + " " + info[1] + " queried "
                + info[3] + " from self-service machine");
        Sequence.getInstance().queryInfo(info[0]);
        System.out.println(info[0] + " self-service machine provided information of " + info[3]);
        Sequence.getInstance().getInfo(info[0]);
    }

    public void lend(String[] info) {
        String[] stuInfo = info[1].split("-");
        if (bookSys.checkStuCbook(info[1], info[3], info[0].substring(1, 11))) {
            System.out.println(info[0] + " self-service machine lent " + stuInfo[0]
                    + "-" + info[3] + " to " + info[1]);
            BookState.getInstance().lent(info[0], info[3]);
            Sequence.getInstance().succeed(info[0]);
            System.out.println(info[0] + " " + info[1] + " borrowed "
                    + stuInfo[0] + "-" + info[3] + " from self-service machine");
        } else {
            System.out.println(info[0] + " self-service machine refused lending " + stuInfo[0]
                    + "-" + info[3] + " to " + info[1]);
            BookState.getInstance().refused(info[0], info[3]);
            Sequence.getInstance().refused(info[0]);
        }
    }

    public OutBook returned(String[] info) {
        OutBook o = bookSys.returnCbook(info[1], info[3]);
        if (o == null) {
            System.out.println(info[0] + " " + info[1] + " returned " + name + "-" + info[3]
                    + " to self-service machine");
            System.out.println(info[0] + " self-service machine collected " + name + "-"
                    + info[3] + " from " + info[1]);
            BookState.getInstance().returned(info[0], info[3]);
        } else {
            System.out.println(info[0] + " " + info[1] + " returned " + o.getSrc() + "-" + info[3]
                    + " to self-service machine");
            System.out.println(info[0] + " self-service machine collected " + o.getSrc()
                    + "-" + info[3] + " from " + info[1]);
            BookState.getInstance().returned(info[0], info[3]);
        }
        return o;
    }
}
