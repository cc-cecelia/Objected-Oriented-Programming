import java.io.*;
import java.util.ArrayList;

public class MyRequest implements Serializable {
    private int fromStorey;
    private int toStorey;
    private final int id;
    private boolean upOrDown;
    private final ArrayList<Integer> tranEleId;
    private final ArrayList<Integer> tranStorey;
    private int tranTimes;
    private boolean isTran;

    public MyRequest(int fromStorey, int toStorey, int id) {
        this.toStorey = toStorey;
        this.fromStorey = fromStorey;
        this.id = id;
        this.upOrDown = (this.toStorey > this.fromStorey);
        this.tranStorey = new ArrayList<>();
        this.tranEleId = new ArrayList<>();
        this.tranTimes = 0;
        this.isTran = false;
    }

    public synchronized int getFromStorey() { return this.fromStorey; }

    public synchronized int getToStorey() { return this.toStorey; }

    public synchronized int getId() { return this.id; }

    public synchronized boolean getDirection() { return this.upOrDown; }

    public synchronized int getTranTimes() { return this.tranTimes; }

    public synchronized void setFromStorey(int fromStorey) {
        this.fromStorey = fromStorey;
        upOrDown = (this.toStorey > this.fromStorey);
    }

    public synchronized void setToStorey(int toStorey) {
        this.toStorey = toStorey;
        upOrDown = (this.toStorey > this.fromStorey);
    }

    public synchronized void setTran(int storey, int ele) {
        tranStorey.add(storey);
        tranEleId.add(ele);
        tranTimes++;
    }

    public synchronized void setIsTran(boolean tmp) {
        isTran = tmp;
    }

    public synchronized boolean isTran() { return isTran; }

    public synchronized int iterate() {
        // System.out.println("iterate:id"+id);
        tranTimes--;
        fromStorey = toStorey;
        toStorey = tranStorey.get(0);
        upOrDown = (toStorey > fromStorey);
        tranStorey.remove(0);
        int tmp = tranEleId.get(0);
        tranEleId.remove(0);
        return tmp;
    }

    public synchronized void clear() {
        tranTimes = 0;
        if (!tranStorey.isEmpty()) {
            toStorey = tranStorey.get(tranStorey.size() - 1);
        }
        /* for (int i = 0 ;i < tranStorey.size(); i++) {
            System.out.println(tranStorey.get(i));
        } */
        upOrDown = (toStorey > fromStorey);
        tranStorey.clear();
        tranEleId.clear();
        isTran = false;
    }

    public synchronized MyRequest deepClone() {
        MyRequest copy = new MyRequest(this.fromStorey, this.toStorey, this.id);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (MyRequest) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }
}
