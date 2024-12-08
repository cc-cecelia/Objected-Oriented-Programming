import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static final PersonCnt EMOJI_CNT = new PersonCnt();
    private final int id;

    public MyEmojiIdNotFoundException(int id) {
        EMOJI_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("einf-" + EMOJI_CNT.getTotal() + ", " + id + "-" + EMOJI_CNT.get(id));
    }
}
