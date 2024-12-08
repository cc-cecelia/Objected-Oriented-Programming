import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;

public class VerElevator extends Elevator {
    private RequestQueue subQueue;
    private ArrayList<Request> myList;
    private int id;
    private boolean isUp;
    private int curPass;
    private int curStorey;
    private Strategy strategy;
    private int openTime;
    private int closeTime;
    private int moveTime;

    public VerElevator(RequestQueue subQueue, int id) {
        this.subQueue = subQueue;
        this.myList = new ArrayList<>();
        this.id = id;
        this.isUp = true;
        this.curPass = 0;
        this.curStorey = 1;
        this.strategy = new Strategy();
        this.openTime = 200;
        this.closeTime = 200;
        this.moveTime = 400;
    }

    @Override
    public void run() {
        while (true) {
            Action action = strategy.getAction(this.isUp, this.curPass, this.curStorey,
                    this.myList, this.subQueue);
            if (action == Action.WAIT) {
                subQueue.waitForRequest();
            } else if (action == Action.OVER) {
                return;
            } else if (action == Action.BACKWARD) {
                this.isUp = !this.isUp;
            } else if (action == Action.FORWARD) {
                try {
                    forward();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (action == Action.OPENCLOSE) {
                try {
                    open();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                passOut();
                passIn();
                try {
                    close();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void forward() throws InterruptedException {
        Thread.sleep(this.moveTime);
        if (this.isUp) {
            this.curStorey = this.curStorey + 1;
            TimableOutput.println("ARRIVE-" + this.curStorey + "-" + this.id);
        } else {
            this.curStorey = this.curStorey - 1;
            TimableOutput.println("ARRIVE-" + this.curStorey + "-" + this.id);
        }
    }

    public void open() throws InterruptedException {
        TimableOutput.println("OPEN-" + this.curStorey + "-" + this.id);
        Thread.sleep(this.openTime);
    }

    public void close() throws InterruptedException {
        Thread.sleep(this.closeTime);
        TimableOutput.println("CLOSE-" + this.curStorey + "-" + this.id);
    }

    public void passOut() {
        for (int i = 0; i < myList.size(); i++) {
            if (myList.get(i).getToStorey() == curStorey) {
                TimableOutput.println("OUT-" + myList.get(i).getId() + "-" + this.curStorey
                        + "-" + this.id);
                myList.remove(i);
                i--;
                this.curPass = this.curPass - 1;
            }
        }
    }

    public void passIn() {
        while (subQueue.containFromStorey(this.curStorey, this.isUp) && this.curPass < 6) {
            Request tmp = subQueue.getOneFrom(this.curStorey, this.isUp);
            TimableOutput.println("IN-" + tmp.getId() + "-" + this.curStorey + "-" + this.id);
            myList.add(tmp);
            this.curPass = this.curPass + 1;
        }
    }
}
