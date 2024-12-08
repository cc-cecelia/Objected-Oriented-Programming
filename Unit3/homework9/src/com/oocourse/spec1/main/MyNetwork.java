package com.oocourse.spec1.main;

import com.oocourse.spec1.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private int cntTri;
    private final HashMap<Integer, Integer> disJointSet;
    private int blockSum;

    public MyNetwork() {
        people = new HashMap<>();
        cntTri = 0;
        disJointSet = new HashMap<>();
        blockSum = 0;
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return people.getOrDefault(id, null);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int id = person.getId();
        if (people.containsKey(id)) {
            throw new MyEqualPersonIdException(id);
        } else {
            people.put(id, person);
        }
        disJointSet.put(id, id);
        blockSum++;
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!(people.containsKey(id1))) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!(people.containsKey(id2))) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (people.get(id1).isLinked(people.get(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        ((MyPerson)people.get(id1)).addAcq(people.get(id2), id2, value);
        ((MyPerson)people.get(id2)).addAcq(people.get(id1), id1, value);
        if (((MyPerson) people.get(id1)).getAcqNum() <= ((MyPerson) people.get(id2)).getAcqNum()) {
            cntTri += ((MyPerson) people.get(id1)).newTri(people.get(id2), id2);
        } else {
            cntTri += ((MyPerson) people.get(id2)).newTri(people.get(id1), id1);
        }
        union(id1, id2);
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!(people.containsKey(id1))) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!(people.containsKey(id2))) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!(people.get(id1).isLinked(people.get(id2)))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return people.get(id1).queryValue(people.get(id2));
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!(people.containsKey(id1))) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!(people.containsKey(id2))) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return Objects.equals(find(id1), find(id2));
    }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    @Override
    public int queryTripleSum() {
        return cntTri;
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        if (beforeData.keySet().equals(afterData.keySet())) {
            for (Integer i : beforeData.keySet()) {
                HashMap<Integer, Integer> before = beforeData.get(i);
                HashMap<Integer, Integer> after = afterData.get(i);
                if (!(before.keySet().equals(after.keySet()))) {
                    // System.out.println("1111");
                    return false;
                } else {
                    for (Integer j : before.keySet()) {
                        if (!Objects.equals(before.get(j), after.get(j))) {
                            // System.out.println("2222");
                            return false;
                        }
                    }
                }
            }
        } else {
            // System.out.println("3333");
            return false;
        }
        int ans = 0;
        ArrayList<Integer> before = new ArrayList<>(beforeData.keySet());
        for (int i = 0; i < before.size(); i++) {
            for (int j = i + 1; j < before.size(); j++) {
                for (int k = j + 1; k < before.size(); k++) {
                    if (beforeData.get(before.get(i)).containsKey(before.get(j))
                            && beforeData.get(before.get(j)).containsKey(before.get(k))
                            && beforeData.get(before.get(k)).containsKey(before.get(i))) {
                        ans++;
                    }
                }
            }
        }
        return (ans == result);
    }

    public void union(int id1, int id2) {
        int tmp1 = find(id1);
        int tmp2 = find(id2);
        if (tmp1 != tmp2) {
            blockSum--;
            disJointSet.put(tmp1, disJointSet.get(tmp2));
        }
    }

    public int find(int id) {
        int toFind = id;
        int idTmp = id;
        while (disJointSet.get(toFind) != toFind) {
            toFind = disJointSet.get(toFind);
        }
        while (toFind != idTmp) {
            int tmp = disJointSet.get(idTmp);
            disJointSet.put(idTmp, toFind);
            idTmp = tmp;
        }
        return toFind;
    }
}
