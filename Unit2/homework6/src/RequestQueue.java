import java.util.ArrayList;

public class RequestQueue {
    private final ArrayList<MyRequest> requestList;
    private boolean isEnd;

    public RequestQueue() {
        this.requestList = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(MyRequest myRequest) {
        requestList.add(myRequest);
        notifyAll();
    }

    public synchronized MyRequest getOneRequest() {
        if (requestList.isEmpty() && !this.isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requestList.isEmpty()) {
            return null;
        }
        MyRequest myRequest = requestList.get(0);
        requestList.remove(0);
        notifyAll();
        return myRequest;
    }

    public synchronized MyRequest getMainReq(ElevatorFac elevatorFac) {
        if (requestList.isEmpty() && !this.isEnd && !elevatorFac.isIfAdd()
                && !elevatorFac.isIfMaintain()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requestList.isEmpty()) {
            return null;
        }
        MyRequest myRequest = requestList.get(0);
        requestList.remove(0);
        notifyAll();
        return myRequest;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requestList.isEmpty();
    }

    public synchronized boolean containFromStorey(int storey, boolean isUp) {
        if (requestList.isEmpty()) { return false; }
        for (MyRequest r : requestList) {
            if (r.getFromStorey() == storey && r.getDirection() == isUp) { return true; }
        }
        notifyAll();
        return false;
    }

    public synchronized boolean containToStorey(int storey, boolean isUp) {
        if (requestList.isEmpty()) { return false; }
        for (MyRequest r : requestList) {
            if (r.getToStorey() == storey && r.getDirection() == isUp) { return true; }
        }
        notifyAll();
        return false;
    }

    public synchronized boolean containDirection(boolean isUp, int curStorey) {
        for (MyRequest r : requestList) {
            if (((r.getFromStorey() - curStorey) > 0) && isUp) {
                return true;
            } else if (((r.getFromStorey() - curStorey) < 0) && !isUp) {
                return true;
            }
        }
        notifyAll();
        return false;
    }

    public synchronized void waitForRequest() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyAll();
    }

    public synchronized MyRequest getOneFrom(int storey, boolean isUp) {
        for (int i = 0; i < requestList.size(); i++) {
            if (requestList.get(i).getFromStorey() == storey
                    && requestList.get(i).getDirection() == isUp) {
                MyRequest tmp = requestList.get(i).deepClone();
                requestList.remove(i);
                notifyAll();
                return tmp;
            }
        }
        notifyAll();
        return null;
    }

    public synchronized void clear(RequestQueue mainQueue) {
        for (int i = 0; i < requestList.size(); i++) {
            mainQueue.addRequest(requestList.get(i));
            requestList.remove(i);
            i--;
        }
    }

    public synchronized void myNotify() { notifyAll(); }
}
