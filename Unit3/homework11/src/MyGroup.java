import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, Person> people;
    private int ageSum;
    private int agePowSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.ageSum = 0;
        this.agePowSum = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return (this.id == ((Group) obj).getId());
        } else {
            return false;
        }
    }

    @Override
    public void addPerson(Person person) {
        int age = person.getAge();
        ageSum += age;
        agePowSum += (age * age);
        people.put(person.getId(), person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int ans = 0;
        for (Integer i : people.keySet()) {
            for (Integer j : people.keySet()) {
                if (people.get(i).isLinked(people.get(j))) {
                    ans += people.get(i).queryValue(people.get(j));
                }
            }
        }
        return ans;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            return 0;
        } else {
            return (ageSum / people.size());
        }
    }

    @Override
    public int getAgeVar() {
        if (people.isEmpty()) {
            return 0;
        } else {
            int tmp = getAgeMean();
            /* (pow - 2*sum*getAgeMean + n*getAgeMean^2)/length
               System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+
               (agePowSum - 2 * ageSum * tmp)+"@"+(tmp * tmp)+"@"+people.size()); */
            return (((agePowSum - 2 * ageSum * tmp + tmp * tmp * people.size()) / people.size()));
        }
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        int age = person.getAge();
        ageSum -= age;
        agePowSum -= (age * age);
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void allAddSocialVal(int socialVal) {
        for (Person p : people.values()) {
            p.addSocialValue(socialVal);
        }
    }

    public void redPocket(Person p1, int moneyTotal) {
        int moneySingle = moneyTotal / getSize();
        for (Person p : people.values()) {
            if (p.equals(p1)) {
                p1.addMoney(-(moneySingle * (getSize() - 1)));
            } else {
                p.addMoney(moneySingle);
            }
        }
    }
}
