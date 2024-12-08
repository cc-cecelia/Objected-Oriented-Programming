public class MyDot implements Comparable<MyDot> {
    private int val1;
    private int val2;
    private int root1;
    private int root2;
    private final int id;

    public MyDot(int val1, int val2, int root1, int root2, int id) {
        this.val1 = val1;
        this.val2 = val2;
        this.root1 = root1;
        this.root2 = root2;
        this.id = id;
    }

    @Override
    public int compareTo(MyDot o) {
        return this.val1 - o.getVal1();
    }

    public int getVal1() { return val1; }

    public int getVal2() { return val2; }

    public int getRoot1() { return root1; }

    public int getRoot2() { return root2; }

    public int getId() { return id; }

    public boolean update(int val, int root, int edge) {
        if (val + edge < val1) {
            if (root != root1) {
                val2 = val1;
                root2 = root1;
            }
            root1 = root;
            val1 = val + edge;
            return true;
        } else if (val + edge < val2) {
            if (root != root1) {
                val2 = val + edge;
                root2 = root;
            }
        }
        return false;
    }
}
