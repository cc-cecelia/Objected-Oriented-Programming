import java.util.HashMap;
import java.util.Objects;

public class MyModifyRelationOkTest {
    public int modifyRelationOkTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)
                || !(beforeData.get(id1).containsKey(id2)) || id1 == id2) {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        }
        //-----------------2-------------------
        for (Integer id : beforeData.keySet()) {
            if (!afterData.containsKey(id)) {
                return 2;
            }
        }
        //-----------------3-------------------
        for (Integer id : beforeData.keySet()) {
            if (id != id1 && id != id2) {
                if (!(beforeData.get(id).equals(afterData.get(id)))) {
                    return 3;
                }
            }
        }
        if (beforeData.get(id1).get(id2) + value > 0) {
            int tmp = bigger(id1, id2, value, beforeData, afterData);
            if (tmp > 0) {
                return tmp;
            }
        } else {
            //-----------------15-------------------
            if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
                return 15;
            }
            //-----------------16-------------------
            if (beforeData.get(id1).values().size() != afterData.get(id1).keySet().size() + 1) {
                return 16;
            }
            //-----------------17-------------------
            if (beforeData.get(id2).values().size() != afterData.get(id2).keySet().size() + 1) {
                return 17;
            }
            //-----------------18-------------------
            if (afterData.get(id1).values().size() != afterData.get(id1).keySet().size()) {
                return 18;
            }
            //-----------------19-------------------
            if (afterData.get(id2).values().size() != afterData.get(id2).keySet().size()) {
                return 19;
            }
            //-----------------20-------------------
            for (Integer id : afterData.get(id1).keySet()) {
                if (!Objects.equals(afterData.get(id1).get(id), beforeData.get(id1).get(id))) {
                    return 20;
                }
            }
            //-----------------21-------------------
            for (Integer id : afterData.get(id2).keySet()) {
                if (!Objects.equals(afterData.get(id2).get(id), beforeData.get(id2).get(id))) {
                    return 21;
                }
            }
        }
        return 0;
    }

    public int bigger(int id1, int id2, int value,
                      HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                      HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        //-----------------4-------------------
        if (!(afterData.get(id1).containsKey(id2)) || !(afterData.get(id2).containsKey(id1))) {
            return 4;
        }
        //-----------------5-------------------
        if (afterData.get(id1).get(id2) != beforeData.get(id1).get(id2) + value) {
            return 5;
        }
        //-----------------6-------------------
        if (afterData.get(id2).get(id1) != beforeData.get(id2).get(id1) + value) {
            return 6;
        }
        //-----------------7-------------------
        if (beforeData.get(id1).size() != afterData.get(id1).size()) {
            return 7;
        }
        //-----------------8-------------------
        if (beforeData.get(id2).size() != afterData.get(id2).size()) {
            return 8;
        }
        //-----------------9-------------------
        for (Integer id : beforeData.get(id1).keySet()) {
            if (!(afterData.get(id1).containsKey(id))) {
                return 9;
            }
        }
        //-----------------10-------------------
        for (Integer id : beforeData.get(id2).keySet()) {
            if (!(afterData.get(id2).containsKey(id))) {
                return 10;
            }
        }
        //-----------------11-------------------
        for (Integer id : afterData.get(id1).keySet()) {
            if (id != id2) {
                if (!Objects.equals(beforeData.get(id1).get(id), afterData.get(id1).get(id))) {
                    return 11;
                }
            }
        }
        //-----------------12-------------------
        for (Integer id : afterData.get(id2).keySet()) {
            if (id != id1) {
                if (!Objects.equals(beforeData.get(id2).get(id), afterData.get(id2).get(id))) {
                    return 12;
                }
            }
        }
        //-----------------13-------------------
        if (afterData.get(id1).values().size() != afterData.get(id1).keySet().size()) {
            return 13;
        }
        //-----------------14-------------------
        if (afterData.get(id2).values().size() != afterData.get(id2).keySet().size()) {
            return 14;
        }
        return 0;
    }
}
