import java.util.HashMap;

public class PersonCnt {
    private final HashMap<Integer, Integer> times;
    private int total;

    public PersonCnt() {
        this.times = new HashMap<>();
        this.total = 0;
    }

    public void add(int id) {
        if (times.containsKey(id)) {
            int tmp = times.get(id);
            tmp++;
            times.put(id, tmp);
        } else {
            times.put(id, 1);
        }
        total++;
    }

    public int get(int id) {
        return times.get(id);
    }

    public int getTotal() {
        return total;
    }
}