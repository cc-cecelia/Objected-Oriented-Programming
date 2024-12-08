import com.oocourse.spec3.exceptions.*;
import com.oocourse.spec3.main.*;

import java.util.*;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Message> messages;
    private final ArrayList<Integer> emojiIdList;
    private final HashMap<Integer, Integer> emojiHeatList;
    // ---------------------------------------------------------
    private final HashMap<Integer, ArrayList<Integer>> emojiId2Mid;
    private int cntTri;
    private final DisJointSet disJointSet;
    private final HashMap<Integer, Integer> bestAcq;
    private int coupleSum;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        emojiIdList = new ArrayList<>();
        emojiHeatList = new HashMap<>();
        emojiId2Mid = new HashMap<>();
        cntTri = 0;
        disJointSet = new DisJointSet();
        bestAcq = new HashMap<>();
        coupleSum = 0;
    }

    @Override
    public boolean contains(int id) { return people.containsKey(id); }

    @Override
    public Person getPerson(int id) { return people.getOrDefault(id, null); }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int id = person.getId();
        if (people.containsKey(id)) { throw new MyEqualPersonIdException(id); }
        else { people.put(id, person); }
        disJointSet.addIntoSet(id, id);
        disJointSet.addBlockSum(1);
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!(people.containsKey(id1))) { throw new MyPersonIdNotFoundException(id1); }
        else if (!(people.containsKey(id2))) { throw new MyPersonIdNotFoundException(id2); }
        else if (people.get(id1).isLinked(people.get(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        ((MyPerson)people.get(id1)).addAcq(people.get(id2), id2, value);
        ((MyPerson)people.get(id2)).addAcq(people.get(id1), id1, value);
        if (((MyPerson) people.get(id1)).getAcqNum() <= ((MyPerson) people.get(id2)).getAcqNum()) {
            cntTri += ((MyPerson) people.get(id1)).newTri(people.get(id2), id2);
        } else {
            cntTri += ((MyPerson) people.get(id2)).newTri(people.get(id1), id1);
        }
        disJointSet.union(id1, id2);
        if (!bestAcq.containsKey(id1)) { bestAcq.put(id1, id2); }
        else {
            int bestOfId1 = bestAcq.get(id1);
            int tmp = getPerson(id1).queryValue(getPerson(bestOfId1));
            if (value > tmp || (value == tmp && id2 < bestOfId1)) {
                if (bestAcq.get(bestOfId1) == id1) { coupleSum--; }
                bestAcq.put(id1, id2);
            }
        }
        if (!bestAcq.containsKey(id2)) {
            bestAcq.put(id2, id1);
            if (bestAcq.get(id1) == id2) { coupleSum++; }
        } else {
            int bestOfId2 = bestAcq.get(id2);
            int tmp = getPerson(id2).queryValue(getPerson(bestOfId2));
            if (value > tmp || (value == tmp && id1 < bestOfId2)) {
                if (bestAcq.get(bestOfId2) == id2) { coupleSum--; }
                bestAcq.put(id2, id1);
                if (bestAcq.get(id1) == id2) { coupleSum++; }
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
            if (bestAcq.get(id1) == id2 && bestAcq.get(id2) == id1) { coupleSum--; }
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
            disJointSet.reBuild(id1, people);
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
        return Objects.equals(disJointSet.find(id1), disJointSet.find(id2));
    }

    @Override
    public int queryBlockSum() {
        return disJointSet.getBlockSum();
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
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        int messageId = message.getId();
        if (messages.containsKey(messageId)) {
            throw new MyEqualMessageIdException(messageId);
        } else if (message instanceof EmojiMessage
                && !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if ((message.getType() == 0) && (message.getPerson1() == message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(messageId, message);
            if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                if (emojiId2Mid.containsKey(emojiId)) {
                    ArrayList<Integer> tmp = emojiId2Mid.get(emojiId);
                    tmp.add(messageId);
                } else {
                    ArrayList<Integer> tmp = new ArrayList<>();
                    tmp.add(messageId);
                    emojiId2Mid.put(emojiId, tmp);
                }
            }
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
        }
        if (getMessage(id).getType() == 0) {
            Person p1 = getMessage(id).getPerson1();
            Person p2 = getMessage(id).getPerson2();
            p1.addSocialValue(getMessage(id).getSocialValue());
            p2.addSocialValue(getMessage(id).getSocialValue());
            ((MyPerson)p2).messagesInsertHead(getMessage(id));
            if (getMessage(id) instanceof RedEnvelopeMessage) {
                p1.addMoney(-((RedEnvelopeMessage) getMessage(id)).getMoney());
                p2.addMoney(((RedEnvelopeMessage) getMessage(id)).getMoney());
            } else if (getMessage(id) instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) getMessage(id)).getEmojiId();
                int tmp = emojiHeatList.get(emojiId);
                emojiHeatList.put(emojiId, tmp + 1);
                ArrayList<Integer> listOfEmoji = emojiId2Mid.get(emojiId);
                listOfEmoji.remove((Integer) id);
                if (listOfEmoji.isEmpty()) {
                    emojiId2Mid.remove(emojiId);
                }
            }
            messages.remove(id);
        } else if (getMessage(id).getType() == 1) {
            Group g = getMessage(id).getGroup();
            ((MyGroup)g).allAddSocialVal(getMessage(id).getSocialValue());
            if (getMessage(id) instanceof RedEnvelopeMessage) {
                ((MyGroup) g).redPocket(getMessage(id).getPerson1(),
                        ((RedEnvelopeMessage) getMessage(id)).getMoney());
            } else if (getMessage(id) instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) getMessage(id)).getEmojiId();
                int tmp = emojiHeatList.get(emojiId);
                emojiHeatList.put(emojiId, tmp + 1);
                ArrayList<Integer> listOfEmoji = emojiId2Mid.get(emojiId);
                listOfEmoji.remove((Integer) id);
                if (listOfEmoji.isEmpty()) {
                    emojiId2Mid.remove(emojiId);
                }
            }
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
    public boolean containsEmojiId(int id) {
        return emojiIdList.contains(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiIdList.contains(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiIdList.add(id);
        emojiHeatList.put(id, 0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiIdList.contains(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeatList.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        Iterator<Integer> iter = emojiIdList.iterator();
        while (iter.hasNext()) {
            int next = iter.next();
            if (emojiHeatList.get(next) < limit) {
                emojiHeatList.remove(next);
                if (emojiId2Mid.containsKey(next)) {
                    ArrayList<Integer> messOfEmoji = emojiId2Mid.get(next);
                    for (Integer i : messOfEmoji) {
                        messages.remove(i);
                    }
                    emojiId2Mid.remove(next);
                }
                iter.remove();
            }
        }
        return emojiIdList.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        ((MyPerson)getPerson(personId)).clearNotice();
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
    public int queryCoupleSum() { return coupleSum; }

    @Override
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        Dijkstra test = new Dijkstra();
        int ans = test.qlm(people, id);
        if (ans == -1) {
            throw new MyPathNotFoundException(id);
        }
        return ans;
    }

    @Override
    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        MyDeleteColdEmojiOkTest test = new MyDeleteColdEmojiOkTest();
        return test.test(limit, beforeData, afterData, result);
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
