import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyEmojiMessage implements EmojiMessage {
    private final int type;
    private final int emojiId;
    private final int id;
    private final Person person1;
    private final Person person2;
    private final Group group;
    private final int socialVal;

    public MyEmojiMessage(int messageId, int emojiNumber, Person messagePerson1,
                          Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.emojiId = emojiNumber;
        this.socialVal = emojiId;
    }

    public MyEmojiMessage(int messageId, int emojiNumber, Person messagePerson1,
                          Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.emojiId = emojiNumber;
        this.socialVal = emojiId;
    }

    @Override
    public int getEmojiId() {
        return emojiId;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSocialValue() {
        return this.socialVal;
    }

    @Override
    public Person getPerson1() {
        return this.person1;
    }

    @Override
    public Person getPerson2() {
        return this.person2;
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Message) {
            return (((Message) obj).getId() == this.id);
        } else {
            return false;
        }
    }
}
