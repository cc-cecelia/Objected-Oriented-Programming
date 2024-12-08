import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorFac {
    private boolean ifAdd = false;
    private boolean isEnd = false;
    private final EleInfo eleInfo = new EleInfo();
    private boolean ifMaintain = false;
    private int mtId = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition conAdd = lock.newCondition();
    private final Condition conMain = lock.newCondition();

    public void build(int id, int curStorey, int capacity, double moveTime, int access)
            throws InterruptedException {
        lock.lock();
        try {
            if (this.ifAdd) {
                // System.out.println("wait new" + id);
                conAdd.await();
            }
            eleInfo.build(id, curStorey, capacity, moveTime, access);
            this.ifAdd = true;
        } finally {
            lock.unlock();
        }
    }

    public void setEnd(boolean isEnd) {
        lock.lock();
        try {
            this.isEnd = isEnd;
        } finally {
            lock.unlock();
        }
    }

    public void maintain(int mtId) throws InterruptedException {
        lock.lock();
        try {
            if (this.ifMaintain) {
                conMain.await();
            }
            this.mtId = mtId;
            this.ifMaintain = true;
        } finally {
            lock.unlock();
        }
    }

    public EleInfo getEleInfo() {
        lock.lock();
        try {
            if (this.ifAdd) {
                this.ifAdd = false;
                conAdd.signal();
                return this.eleInfo.deepClone();
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public int getMtId() {
        lock.lock();
        try {
            if (this.ifMaintain) {
                this.ifMaintain = false;
                conMain.signal();
                return this.mtId;
            } else {
                return -1;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isEnd() {
        lock.lock();
        try {
            return isEnd;
        } finally {
            lock.unlock();
        }
    }

    public boolean isIfAdd() {
        lock.lock();
        try { return ifAdd; }
        finally { lock.unlock(); }
    }

    public boolean isIfMaintain() {
        lock.lock();
        try { return ifMaintain; }
        finally { lock.unlock(); }
    }
}
