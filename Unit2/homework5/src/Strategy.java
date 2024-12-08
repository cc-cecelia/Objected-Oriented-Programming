import java.util.ArrayList;

public class Strategy {
    public Action getAction(boolean isUp, int curPass, int curStorey,
                            ArrayList<Request> myList, RequestQueue subQueue) {
        if (openForIn(isUp, curPass, curStorey, subQueue) || openForOut(myList, curStorey)) {
            return Action.OPENCLOSE;
        }
        if (curPass != 0) {
            return Action.FORWARD;
        } else {
            if (subQueue.isEmpty() && subQueue.isEnd()) {
                return Action.OVER;
            } else if (subQueue.isEmpty() && !subQueue.isEnd()) {
                return Action.WAIT;
            }
            // subQueue isn't empty
            if (subQueue.containDirection(isUp, curStorey)) {
                return Action.FORWARD;
            } else { return Action.BACKWARD; }
        }
    }

    public boolean openForIn(boolean isUp, int curPass, int curStorey, RequestQueue subQueue) {
        if (subQueue.containFromStorey(curStorey, isUp) && curPass < 6) {
            return true;
        } else { return false; }
    }

    public boolean openForOut(ArrayList<Request> myList, int curStorey) {
        for (Request r : myList) {
            if (r.getToStorey() == curStorey) {
                return true;
            }
        }
        return false;
    }

}
