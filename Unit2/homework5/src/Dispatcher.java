import java.util.ArrayList;

public class Dispatcher extends Thread {
    private RequestQueue mainQueue;
    private ArrayList<RequestQueue> subQueues;
    private int flag = 0;

    public Dispatcher(RequestQueue mainQueue, ArrayList<RequestQueue> subQueues) {
        this.mainQueue = mainQueue;
        this.subQueues = subQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (this.mainQueue.isEmpty() && this.mainQueue.isEnd()) {
                for (RequestQueue r : this.subQueues) {
                    r.setEnd(true);
                }
                return;
            }
            Request request = this.mainQueue.getOneRequest();
            if (request == null) {
                continue;
            }
            flag = (flag + 1) % 6;
            subQueues.get(flag).addRequest(request);
        }
    }
}
