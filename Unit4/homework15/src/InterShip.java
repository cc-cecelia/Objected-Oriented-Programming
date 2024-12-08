public class InterShip {
    private int src;
    private int dst;
    private String book;
    private String stuId;
    private String type;

    public InterShip(int src, int dst, String book, String stuId, String type) {
        this.src = src;
        this.dst = dst;
        this.book = book;
        this.stuId = stuId;
        this.type = type;
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }

    public String getBook() {
        return book;
    }

    public String getStuId() {
        return stuId;
    }

    public String getType() {
        return type;
    }
}
