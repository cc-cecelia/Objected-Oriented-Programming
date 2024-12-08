public class MaintainLib {
    private int id;

    public MaintainLib(int id) {
        this.id = id;
    }

    public void maintain(String[] info) {
        System.out.println(info[0] + " " + info[3] + " got repaired by logistics division");
        BookSys.getIns().maintain(info[3]);
    }
}
