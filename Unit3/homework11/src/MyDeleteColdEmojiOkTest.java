import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyDeleteColdEmojiOkTest {
    int test(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
             ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        HashMap<Integer, Integer> beforeEmoHeat = beforeData.get(0);
        HashMap<Integer, Integer> afterEmoHeat = afterData.get(0);
        HashMap<Integer, Integer> afterMess = afterData.get(1);
        //-------------------1---------------------------------
        for (Integer emojiId : beforeEmoHeat.keySet()) {
            if (beforeEmoHeat.get(emojiId) >= limit) {
                if (!afterEmoHeat.containsKey(emojiId)) { return 1; }
            }
        }
        //-------------------2---------------------------------
        for (Integer emojiId : afterEmoHeat.keySet()) {
            if (!beforeEmoHeat.containsKey(emojiId)) { return 2; }
            if (!Objects.equals(beforeEmoHeat.get(emojiId), afterEmoHeat.get(emojiId))) {
                return 2;
            }
        }
        //-------------------3---------------------------------
        int length = 0;
        for (Integer emojiId : beforeEmoHeat.keySet()) {
            if (beforeEmoHeat.get(emojiId) >= limit) {
                length++;
            }
        }
        if (length != afterEmoHeat.size()) { return 3; }
        //-------------------4---------------------------------
        //-------------------5---------------------------------
        HashMap<Integer, Integer> beforeMess = beforeData.get(1);
        for (Integer messId : beforeMess.keySet()) {
            if (beforeMess.get(messId) != null) {
                if (afterEmoHeat.containsKey(beforeMess.get(messId))) {
                    if (!afterMess.containsKey(messId)) {
                        return 5;
                    }
                    if (!Objects.equals(beforeMess.get(messId), afterMess.get(messId))) {
                        return 5;
                    }
                }
            } else {
                //-------------------6---------------------------------
                if (!afterMess.containsKey(messId)) {
                    return 6;
                }
                if (!Objects.equals(beforeMess.get(messId), afterMess.get(messId))) {
                    return 6;
                }
            }
        }
        //-------------------7---------------------------------
        int messLength = 0;
        for (Integer messId : beforeMess.keySet()) {
            if (beforeMess.get(messId) != null) {
                if (afterEmoHeat.containsKey(beforeMess.get(messId))) {
                    messLength++;
                }
            } else {
                messLength++;
            }
        }
        if (messLength != afterMess.size()) {
            return 7;
        }
        //-------------------8---------------------------------
        if (afterEmoHeat.size() != result) {
            return 8;
        }
        return 0;
    }
}
