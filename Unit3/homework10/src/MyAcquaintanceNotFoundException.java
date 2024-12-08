import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private static final PersonCnt ACQ_CNT = new PersonCnt();
    private final int id;

    public MyAcquaintanceNotFoundException(int id) {
        ACQ_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("anf-" + ACQ_CNT.getTotal() + ", " + id + "-" + ACQ_CNT.get(id));
    }
}
