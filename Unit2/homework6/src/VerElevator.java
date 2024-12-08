import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;

public class VerElevator extends Elevator {
    private final RequestQueue subQueue;
    private final ArrayList<MyRequest> myList;
    private final int id;
    private boolean isUp;
    private int curPass;
    private int curStorey;
    private final int openTime;
    private final int closeTime;
    private final int moveTime;
    private final int capacity;
    private final MtInfo mtInfo;
    private int mtStorey;
    private final RequestQueue mainQueue;

    public VerElevator(RequestQueue subQueue, int id, int curStorey, int capacity, int moveTime,
                       RequestQueue mainQueue, MtInfo mtInfo) {
        this.subQueue = subQueue;
        this.myList = new ArrayList<>();
        this.id = id;
        this.isUp = true;
        this.curPass = 0;
        this.curStorey = curStorey;
        this.openTime = 200;
        this.closeTime = 200;
        this.moveTime = moveTime;
        this.capacity = capacity;
        this.mtInfo = mtInfo;
        this.mtStorey = 0;
        this.mainQueue = mainQueue;
    }

    @Override
    public void run() {
        while (true) {
            Action action = getAction(mtInfo.getIfMt());
            // System.out.println("In------"+this.id);
            if (action == Action.WAIT) {
                // System.out.println("Wait------"+this.id);
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
            } else if (action == Action.MAINTAIN) {
                if (curPass != 0) {
                    try {
                        open();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    passOut();
                    passAllOut();
                    try {
                        close();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                maintain();
                // System.out.println("Maintain------"+this.id);
                return;
            }
            // System.out.println("Out------"+this.id);
        }
    }

    public Action getAction(boolean ifMt) {
        if (ifMt) {
            return Action.MAINTAIN;
        }
        if (openForIn(isUp, curPass, curStorey, subQueue, capacity)
                || openForOut(myList, curStorey)) {
            return Action.OPENCLOSE;
        }
        if (curPass != 0) {
            // if (ifMt && mtStorey == 0) { mtStorey++; }
            return Action.FORWARD;
        } else {
            // if (ifMt) { return Action.MAINTAIN; }
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

    public boolean openForIn(boolean isUp, int curPass, int curStorey,
                             RequestQueue subQueue, int capacity) {
        return subQueue.containFromStorey(curStorey, isUp) && curPass < capacity;
    }

    public boolean openForOut(ArrayList<MyRequest> myList, int curStorey) {
        for (MyRequest r : myList) {
            if (r.getToStorey() == curStorey) {
                return true;
            }
        }
        return false;
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

    public void passAllOut() {
        for (int i = 0; i < myList.size(); i++) {
            MyRequest tmp = myList.get(i);
            TimableOutput.println("OUT-" + tmp.getId() + "-" + this.curStorey + "-" + this.id);
            tmp.setFromStorey(curStorey);
            mainQueue.addRequest(tmp);
            myList.remove(i);
            i--;
            this.curPass = this.curPass - 1;
        }
    }

    public void passIn() {
        while (subQueue.containFromStorey(curStorey, isUp) && curPass < capacity) {
            MyRequest tmp = subQueue.getOneFrom(this.curStorey, this.isUp);
            TimableOutput.println("IN-" + tmp.getId() + "-" + this.curStorey + "-" + this.id);
            myList.add(tmp);
            this.curPass = this.curPass + 1;
        }
    }

    public void maintain() {
        mtInfo.setIfOut(true);
        mtInfo.myNotify();
        TimableOutput.println("MAINTAIN_ABLE-" + this.id);
    }

    @Override
    public int getMyId() {
        return id;
    }
}
