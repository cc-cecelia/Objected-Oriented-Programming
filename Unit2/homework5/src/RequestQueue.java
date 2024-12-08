import java.util.ArrayList;

public class RequestQueue {
    private final ArrayList<Request> requestList;
    private boolean isEnd;

    public RequestQueue() {
        this.requestList = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(Request request) {
        requestList.add(request);
        notifyAll();
    }

    public synchronized Request getOneRequest() {
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
        Request request = requestList.get(0);
        requestList.remove(0);
        notifyAll();
        return request;
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
        for (Request r : requestList) {
            if (r.getFromStorey() == storey && r.getDirection() == isUp) { return true; }
        }
        notifyAll();
        return false;
    }

    public synchronized boolean containToStorey(int storey, boolean isUp) {
        if (requestList.isEmpty()) { return false; }
        for (Request r : requestList) {
            if (r.getToStorey() == storey && r.getDirection() == isUp) { return true; }
        }
        notifyAll();
        return false;
    }

    public synchronized boolean containDirection(boolean isUp, int curStorey) {
        for (Request r : requestList) {
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

    public synchronized Request getOneFrom(int storey, boolean isUp) {
        for (int i = 0; i < requestList.size(); i++) {
            if (requestList.get(i).getFromStorey() == storey
                    && requestList.get(i).getDirection() == isUp) {
                Request tmp = requestList.get(i).deepClone();
                requestList.remove(i);
                notifyAll();
                return tmp;
            }
        }
        notifyAll();
        return null;
    }
}
