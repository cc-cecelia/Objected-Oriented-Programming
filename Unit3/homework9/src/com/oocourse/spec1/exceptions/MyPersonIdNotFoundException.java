package com.oocourse.spec1.exceptions;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static final PersonCnt PERSON_CNT = new PersonCnt();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        PERSON_CNT.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + PERSON_CNT.getTotal() + ", " + id + "-" + PERSON_CNT.get(id));
    }
}
