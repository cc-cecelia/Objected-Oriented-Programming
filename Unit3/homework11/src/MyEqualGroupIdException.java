import com.oocourse.spec3.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static final PersonCnt GROUP_CNT = new PersonCnt();
    private final int id;

    public MyEqualGroupIdException(int id) {
        GROUP_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("egi-" + GROUP_CNT.getTotal() + ", " + id + "-" + GROUP_CNT.get(id));
    }
}