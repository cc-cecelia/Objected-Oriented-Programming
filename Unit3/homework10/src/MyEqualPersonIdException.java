import com.oocourse.spec2.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static final PersonCnt PERSON_CNT = new PersonCnt();
    private final int id;

    public MyEqualPersonIdException(int id) {
        PERSON_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("epi-" + PERSON_CNT.getTotal() + ", " + id + "-" + PERSON_CNT.get(id));
    }
}
