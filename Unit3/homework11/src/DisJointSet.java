import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DisJointSet {
    private final HashMap<Integer, Integer> disJointSet;
    private int blockSum;

    public DisJointSet() {
        this.disJointSet = new HashMap<>();
        blockSum = 0;
    }

    public int getBlockSum() {
        return blockSum;
    }

    public void addBlockSum(int num) {
        blockSum += num;
    }

    public void addIntoSet(int key, int val) {
        disJointSet.put(key, val);
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

    public void reBuild(int id1, HashMap<Integer, Person> people) {
        int id1Find = find(id1);
        ArrayList<Integer> blockId = new ArrayList<>();
        for (Integer id : people.keySet()) {
            if (id1Find == find(id)) {
                blockId.add(id);
            }
        }
        blockSum += (blockId.size() - 1);
        for (Integer id : blockId) {
            disJointSet.put(id, id);
        }
        for (Integer id : blockId) {
            Set<Integer> tmp = ((MyPerson)people.getOrDefault(id, null)).getAcqId();
            for (Integer idd : tmp) {
                if (id < idd) {
                    union(id, idd);
                }
            }
        }
    }
}
