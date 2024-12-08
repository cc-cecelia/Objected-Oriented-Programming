import com.oocourse.spec3.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private static final RelationCnt CNT = new RelationCnt();
    private final int idMin;
    private final int idMax;

    public MyEqualRelationException(int id1, int id2) {
        if (id1 <= id2) {
            idMin = id1;
            idMax = id2;
        } else {
            idMin = id2;
            idMax = id1;
        }
        CNT.add(idMin, idMax);
    }

    @Override
    public void print() {
        System.out.println("er-" + CNT.getTotal() + ", " + idMin + "-" + CNT.get(idMin)
                + ", " + idMax + "-" + CNT.get(idMax));
    }
}
