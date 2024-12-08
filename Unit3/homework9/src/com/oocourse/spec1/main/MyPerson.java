package com.oocourse.spec1.main;

import java.util.HashMap;

public class MyPerson implements Person {
    private int age;
    private int id;
    private String name;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
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
}
