import com.oocourse.spec3.exceptions.PathNotFoundException;

public class MyPathNotFoundException extends PathNotFoundException {
    private static final PersonCnt PERSON_CNT = new PersonCnt();
    private final int id;

    public MyPathNotFoundException(int id) {
        PERSON_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("pnf-" + PERSON_CNT.getTotal() + ", " + id + "-" + PERSON_CNT.get(id));
    }
}
