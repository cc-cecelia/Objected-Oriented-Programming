import java.io.*;
import java.util.ArrayList;

public class EleInfo implements Serializable {
    private int id = 0;
    private int curStorey = 0;
    private double moveTime = 0;
    private int capacity = 0;
    private ArrayList<Integer> access;

    public synchronized void build(int id, int curStorey, int capacity, double moveTime, int acc) {
        this.id = id;
        this.curStorey = curStorey;
        this.capacity = capacity;
        this.moveTime = moveTime;
        this.access = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if ((acc & (1 << i)) != 0) {
                this.access.add(i + 1);
            }
        }
    }

    public synchronized int getId() { return this.id; }

    public synchronized int getCapacity() { return this.capacity; }

    public synchronized int getCurStorey() { return this.curStorey; }

    public synchronized double getMoveTime() { return this.moveTime; }

    public synchronized ArrayList<Integer> getAccess() { return access; }

    public EleInfo deepClone() {
        EleInfo copy = new EleInfo();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (EleInfo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }
}
