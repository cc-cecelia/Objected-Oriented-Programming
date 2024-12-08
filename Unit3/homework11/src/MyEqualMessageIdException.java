import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static final PersonCnt MESSAGE_CNT = new PersonCnt();
    private final int id;

    public MyEqualMessageIdException(int id) {
        MESSAGE_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("emi-" + MESSAGE_CNT.getTotal() + ", " + id + "-" + MESSAGE_CNT.get(id));
    }
}
