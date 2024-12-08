package com.oocourse.spec1.exceptions;

import java.util.HashMap;

public class RelationCnt {
    private final HashMap<Integer, Integer> times;
    private int total;

    public RelationCnt() {
        this.times = new HashMap<>();
        this.total = 0;
    }

    public void add(int id1, int id2) {
        if (times.containsKey(id1)) {
            int tmp = times.get(id1);
            tmp++;
            times.put(id1, tmp);
        } else {
            times.put(id1, 1);
        }
        if (id1 != id2) {
            if (times.containsKey(id2)) {
                int tmp = times.get(id2);
                tmp++;
                times.put(id2, tmp);
            } else {
                times.put(id2, 1);
            }
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
