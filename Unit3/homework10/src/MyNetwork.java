import com.oocourse.spec2.exceptions.*;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.*;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Message> messages;
    // ---------------------------------------------------------
    private int cntTri;
    private final HashMap<Integer, Integer> disJointSet;
    private int blockSum;
    private final HashMap<Integer, Integer> bestAcq;
    private int coupleSum;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        cntTri = 0;
        disJointSet = new HashMap<>();
        blockSum = 0;
        bestAcq = new HashMap<>();
        coupleSum = 0;
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
        if (!bestAcq.containsKey(id1)) {
            bestAcq.put(id1, id2);
        } else {
            int bestOfId1 = bestAcq.get(id1);
            int tmp = getPerson(id1).queryValue(getPerson(bestOfId1));
            if (value > tmp || (value == tmp && id2 < bestOfId1)) {
                if (bestAcq.get(bestOfId1) == id1) {
                    coupleSum--;
                }
                bestAcq.put(id1, id2);
            }
        }
        if (!bestAcq.containsKey(id2)) {
            bestAcq.put(id2, id1);
            if (bestAcq.get(id1) == id2) {
                coupleSum++;
            }
        } else {
            int bestOfId2 = bestAcq.get(id2);
            int tmp = getPerson(id2).queryValue(getPerson(bestOfId2));
            if (value > tmp || (value == tmp && id1 < bestOfId2)) {
                if (bestAcq.get(bestOfId2) == id2) {
                    coupleSum--;
                }
                bestAcq.put(id2, id1);
                if (bestAcq.get(id1) == id2) {
                    coupleSum++;
                }
            }
        }
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!(contains(id1))) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!(contains(id2))) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (!(getPerson(id1).isLinked(getPerson(id2)))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        if (getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
            Person p1 = getPerson(id1);
            Person p2 = getPerson(id2);
            ((MyPerson)p1).addValAlone(id2, value);
            ((MyPerson)p2).addValAlone(id1, value);
            mrBestAcq(id1, id2, value, p1, p2);
        } else {
            ((MyPerson)getPerson(id1)).delAcq(id2);
            ((MyPerson)getPerson(id2)).delAcq(id1);
            if (bestAcq.get(id1) == id2 && bestAcq.get(id2) == id1) {
                coupleSum--;
            }
            if (((MyPerson) getPerson(id1)).getAcqNum() == 0) {
                bestAcq.remove(id1);
            } else if (bestAcq.get(id1) == id2) {
                int newOfId1 = ((MyPerson) getPerson(id1)).bestAcq();
                bestAcq.put(id1, newOfId1);
                if (bestAcq.get(newOfId1) == id1) {
                    coupleSum++;
                }
            }
            if (((MyPerson) getPerson(id2)).getAcqNum() == 0) {
                bestAcq.remove(id2);
            } else if (bestAcq.get(id2) == id1) {
                int newOfId2 = ((MyPerson) getPerson(id2)).bestAcq();
                bestAcq.put(id2, newOfId2);
                if (bestAcq.get(newOfId2) == id2) {
                    coupleSum++;
                }
            }
            reBuild(id1);
            if (((MyPerson) people.get(id1)).getAcqNum()
                    <= ((MyPerson) people.get(id2)).getAcqNum()) {
                cntTri -= ((MyPerson) people.get(id1)).newTri(people.get(id2), id2);
            } else {
                cntTri -= ((MyPerson) people.get(id2)).newTri(people.get(id1), id1);
            }
        }
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
    public void addGroup(Group group) throws EqualGroupIdException {
        int id = group.getId();
        if (groups.containsKey(id)) {
            throw new MyEqualGroupIdException(id);
        } else {
            groups.put(id, group);
        }
    }

    @Override
    public Group getGroup(int id) {
        return groups.getOrDefault(id, null);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!(groups.containsKey(id2))) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!(people.containsKey(id1))) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else if (getGroup(id2).getSize() <= 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!(groups.containsKey(id))) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return getGroup(id).getValueSum();
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!(groups.containsKey(id))) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return getGroup(id).getAgeVar();
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!(groups.containsKey(id2))) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!(people.containsKey(id1))) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!(getGroup(id2).hasPerson(getPerson(id1)))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            getGroup(id2).delPerson(getPerson(id1));
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        int messageId = message.getId();
        if (messages.containsKey(messageId)) {
            throw new MyEqualMessageIdException(messageId);
        } else if ((message.getType() == 0) && (message.getPerson1() == message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(messageId, message);
        }
    }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return messages.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!(containsMessage(id))) {
            throw new MyMessageIdNotFoundException(id);
        } else if ((getMessage(id).getType() == 0)
                && !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if ((getMessage(id).getType() == 1)
                && !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else if (getMessage(id).getType() == 0) {
            Person p1 = getMessage(id).getPerson1();
            Person p2 = getMessage(id).getPerson2();
            p1.addSocialValue(getMessage(id).getSocialValue());
            p2.addSocialValue(getMessage(id).getSocialValue());
            ((MyPerson)p2).messagesInsertHead(getMessage(id));
            messages.remove(id);
        } else if (getMessage(id).getType() == 1) {
            Group g = getMessage(id).getGroup();
            ((MyGroup)g).allAddSocialVal(getMessage(id).getSocialValue());
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!(contains(id))) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getSocialValue();
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!(contains(id))) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
    }

    @Override
    public int queryBestAcquaintance(int id)
            throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!(contains(id))) {
            throw new MyPersonIdNotFoundException(id);
        } else if (((MyPerson)getPerson(id)).getAcqNum() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        } else {
            return bestAcq.get(id);
        }
    }

    @Override
    public int queryCoupleSum() {
        return coupleSum;
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return new MyModifyRelationOkTest().modifyRelationOkTest(
                id1, id2, value, beforeData, afterData);
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

    public void reBuild(int id1) {
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
            Set<Integer> tmp = ((MyPerson)getPerson(id)).getAcqId();
            for (Integer idd : tmp) {
                if (id < idd) {
                    union(id, idd);
                }
            }
        }
    }

    public void mrBestAcq(int id1, int id2, int value, Person p1, Person p2) {
        int oldOfId1 = bestAcq.get(id1);
        int oldOfId2 = bestAcq.get(id2);
        if (value > 0) {
            if (oldOfId1 != id2 && ((p1.queryValue(p2) > p1.queryValue(getPerson(oldOfId1)))
                    || (p1.queryValue(p2) == p1.queryValue(getPerson(oldOfId1))
                    && id2 < oldOfId1))) {
                bestAcq.put(id1, id2);
            }
            if (oldOfId2 != id1 && ((p2.queryValue(p1) > p2.queryValue(getPerson(oldOfId2)))
                    || (p2.queryValue(p1) == p2.queryValue(getPerson(oldOfId2))
                    && id1 < oldOfId2))) {
                bestAcq.put(id2, id1);
            }
        } else if (value < 0) {
            if (oldOfId1 == id2) {
                bestAcq.put(id1, ((MyPerson) p1).bestAcq());
            }
            if (oldOfId2 == id1) {
                bestAcq.put(id2, ((MyPerson) p2).bestAcq());
            }
        }
        if (oldOfId2 == bestAcq.get(id2) && oldOfId1 != bestAcq.get(id1)) {
            if (bestAcq.get(oldOfId1) == id1) { coupleSum--; }
            if (bestAcq.get(bestAcq.get(id1)) == id1) { coupleSum++; }
        } else if (oldOfId1 == bestAcq.get(id1) && oldOfId2 != bestAcq.get(id2)) {
            if (bestAcq.get(oldOfId2) == id2) { coupleSum--; }
            if (bestAcq.get(bestAcq.get(id2)) == id2) { coupleSum++; }
        } else if (oldOfId1 != bestAcq.get(id1) && oldOfId2 != bestAcq.get(id2)) {
            if (oldOfId1 == id2 && oldOfId2 == id1) {
                coupleSum--;
            } else if (oldOfId1 == id2) {
                coupleSum -= (bestAcq.get(oldOfId2) == id2) ? 1 : 0;
            } else if (oldOfId2 == id1) {
                coupleSum -= (bestAcq.get(oldOfId1) == id1) ? 1 : 0;
            } else {
                coupleSum -= (bestAcq.get(oldOfId1) == id1) ? 1 : 0;
                coupleSum -= (bestAcq.get(oldOfId2) == id2) ? 1 : 0;
                coupleSum -= (bestAcq.get(bestAcq.get(id1)) == id1
                        && bestAcq.get(bestAcq.get(id2)) == id2) ? 1 : 0;
            }
            coupleSum += (bestAcq.get(bestAcq.get(id1)) == id1) ? 1 : 0;
            coupleSum += (bestAcq.get(bestAcq.get(id2)) == id2) ? 1 : 0;
        }
    }
}
