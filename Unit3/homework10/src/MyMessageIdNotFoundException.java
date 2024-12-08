import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static final PersonCnt MESSAGE_CNT = new PersonCnt();
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        MESSAGE_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("minf-" + MESSAGE_CNT.getTotal() + ", "
                + id + "-" + MESSAGE_CNT.get(id));
    }
}
