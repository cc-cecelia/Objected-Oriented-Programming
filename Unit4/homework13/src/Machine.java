public class Machine {
    private int id;

    public Machine(int id) {
        this.id = id;
    }

    public void check(String[] info) {
        System.out.println(info[0] + " " + info[1] + " queried "
                + info[3] + " from self-service machine");
    }

    public void lend(String[] info) {
        if (BookSys.getIns().checkStuCbook(info[1], info[3])) {
            System.out.println(info[0] + " " + info[1] + " borrowed "
                    + info[3] + " from self-service machine");
        }
    }

    public void returned(String[] info) {
        System.out.println(info[0] + " " + info[1] + " returned "
                + info[3] + " to self-service machine");
        BookSys.getIns().returnCbook(info[1], info[3]);
    }
}
