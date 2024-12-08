import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.*;

public class MyPerson implements Person {
    private final int age;
    private final int id;
    private final String name;
    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> value;
    private int socialVal;
    private final ArrayList<Message> messages;
    private int money;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.socialVal = 0;
        this.messages = new ArrayList<>();
        this.money = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return (this.id == ((Person) obj).getId());
        } else {
            return false;
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        this.socialVal = this.socialVal + num;
    }

    @Override
    public int getSocialValue() {
        return this.socialVal;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        if (messages.size() <= 5) {
            return this.messages;
        } else {
            LinkedList<Message> ans = new LinkedList<>();
            for (int i = 0; i <= 4; i++) {
                ans.add(messages.get(i));
            }
            return ans;
        }
    }

    @Override
    public void addMoney(int num) {
        this.money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addAcq(Person person, int id, int value) {
        acquaintance.put(id, person);
        this.value.put(id, value);
    }

    public int getAcqNum() {
        return acquaintance.size();
    }

    public int newTri(Person person, int id) {
        int ans = 0;
        for (Integer i : acquaintance.keySet()) {
            if (acquaintance.get(i).isLinked(person) && i != id) {
                ans++;
            }
        }
        return ans;
    }

    public void messagesInsertHead(Message message)  {
        this.messages.add(0, message);
    }

    public int bestAcq() {
        int idMin = Integer.MAX_VALUE;
        int valMax = Integer.MIN_VALUE;
        for (Integer i : acquaintance.keySet()) {
            if (value.get(i) > valMax) {
                valMax = value.get(i);
                idMin = i;
            } else if (value.get(i) == valMax && i < idMin) {
                idMin = i;
            }
        }
        return idMin;
    }

    public void addValAlone(int id, int val) {
        int tmp = value.get(id);
        tmp += val;
        value.put(id, tmp);
    }

    public void delAcq(int id) {
        acquaintance.remove(id);
        value.remove(id);
    }

    public Set<Integer> getAcqId() {
        return acquaintance.keySet();
    }

    public void clearNotice() {
        messages.removeIf(message -> message instanceof NoticeMessage);
    }

    public int myQv(int id) {
        if (acquaintance.containsKey(id)) {
            return value.get(id);
        } else {
            return 0;
        }
    }
}