import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static final PersonCnt GROUP_CNT = new PersonCnt();
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        GROUP_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + GROUP_CNT.getTotal() + ", " + id + "-" + GROUP_CNT.get(id));
    }
}