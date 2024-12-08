import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class SemaStorey {
    private final HashMap<Integer, Semaphore> all;
    private final HashMap<Integer, Semaphore> onlyIn;

    public SemaStorey() {
        all = new HashMap<>();
        onlyIn = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            all.put(i + 1, new Semaphore(4));
            onlyIn.put(i + 1, new Semaphore(2));
        }
    }

    public void acquireAll(int storey) {
        try {
            all.get(storey).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseAll(int storey) {
        all.get(storey).release();
    }

    public void acquireOnly(int storey) {
        try {
            onlyIn.get(storey).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseOnly(int storey) {
        onlyIn.get(storey).release();
    }
}
