import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {
    private final HashMap<Integer, Boolean> visited;
    private final PriorityQueue<MyDot> queue;
    private final HashMap<Integer, MyDot> dots;
    private final Set<Integer> blockId;

    public Dijkstra() {
        this.visited = new HashMap<>();
        this.queue = new PriorityQueue<>();
        // dotOfId is not initialized at first
        this.dots = new HashMap<>();
        this.blockId = new HashSet<>();
    }

    int qlm(HashMap<Integer, Person> people, int id) {
        for (Integer i : people.keySet()) {
            visited.put(i, false);
        }
        visited.put(id, true);
        blockId.add(id);
        // Initialize
        MyPerson personOfId = (MyPerson)(people.get(id));
        for (Integer i : personOfId.getAcqId()) {
            MyDot tmp = new MyDot(personOfId.myQv(i), Integer.MAX_VALUE, i, -1, i);
            queue.add(tmp);
            dots.put(i, tmp);
            blockId.add(i);
        }
        // iterate
        while (!queue.isEmpty()) {
            MyDot tmp = queue.poll();
            int curId = tmp.getId();
            if (visited.get(curId)) {
                continue;
            }
            int val1 = tmp.getVal1();
            int root1 = tmp.getRoot1();
            int val2 = tmp.getVal2();
            int root2 = tmp.getRoot2();
            for (Integer i : ((MyPerson)(people.get(curId))).getAcqId()) {
                if (i != id) {
                    blockId.add(i);
                    MyDot doti;
                    if (dots.containsKey(i)) { doti = dots.get(i); }
                    else {
                        doti = new MyDot(Integer.MAX_VALUE, Integer.MAX_VALUE, -1, -1, i);
                        dots.put(i, doti);
                    }
                    boolean flag1 = false;
                    boolean flag2 = false;
                    flag1 = doti.update(val1, root1, ((MyPerson)(people.get(curId))).myQv(i));
                    if (val2 != Integer.MAX_VALUE) {
                        flag2 = doti.update(val2, root2, ((MyPerson)(people.get(curId))).myQv(i));
                    }
                    if ((flag1 || flag2) && !visited.get(i)) { queue.add(doti); }
                }
            }
            visited.put(curId, true);
        }
        // return value
        int ans = Integer.MAX_VALUE;
        boolean ifCircle = false;
        for (Integer i : blockId) {
            if (dots.containsKey(i)) {
                MyDot dotOfi = dots.get(i);
                if (dotOfi.getVal2() != Integer.MAX_VALUE
                        && dotOfi.getVal1() != Integer.MAX_VALUE) {
                    if (dotOfi.getVal2() + dotOfi.getVal1() < ans) {
                        ans = dotOfi.getVal2() + dotOfi.getVal1();
                        ifCircle = true;
                    }
                }
            }
        }
        if (ifCircle) { return ans; }
        else { return -1; }
    }
}
