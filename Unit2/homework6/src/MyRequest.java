import java.io.*;

public class MyRequest implements Serializable {
    private int fromStorey;
    private final int toStorey;
    private final int id;
    private final boolean upOrDown;

    public MyRequest(int fromStorey, int toStorey, int id) {
        this.toStorey = toStorey;
        this.fromStorey = fromStorey;
        this.id = id;
        this.upOrDown = (this.toStorey > this.fromStorey);
    }

    public int getFromStorey() { return this.fromStorey; }

    public int getToStorey() { return this.toStorey; }

    public int getId() { return this.id; }

    public boolean getDirection() { return this.upOrDown; }

    public void setFromStorey(int fromStorey) { this.fromStorey = fromStorey; }

    public MyRequest deepClone() {
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
