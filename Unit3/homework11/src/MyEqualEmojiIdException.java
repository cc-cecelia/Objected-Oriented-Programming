import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static final PersonCnt EMOJI_CNT = new PersonCnt();
    private final int id;

    public MyEqualEmojiIdException(int id) {
        EMOJI_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("eei-" + EMOJI_CNT.getTotal() + ", " + id + "-" + EMOJI_CNT.get(id));
    }
}