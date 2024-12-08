import java.io.*;

public class Request implements Serializable {
    private final int fromStorey;
    private final int toStorey;
    private final int id;
    private final boolean upOrDown;

    public Request(int fromStorey, int toStorey, int id) {
        this.toStorey = toStorey;
        this.fromStorey = fromStorey;
        this.id = id;
        if (this.toStorey > this.fromStorey) {
            this.upOrDown = true;
        } else { this.upOrDown = false; }
    }

    public int getFromStorey() { return this.fromStorey; }

    public int getToStorey() { return this.toStorey; }

    public int getId() { return this.id; }

    public boolean getDirection() { return this.upOrDown; }

    public Request deepClone() {
        Request copy = new Request(this.fromStorey, this.toStorey, this.id);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (Request) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }
}
