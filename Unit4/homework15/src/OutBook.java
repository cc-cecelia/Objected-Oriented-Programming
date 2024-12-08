public class OutBook {
    private String book;
    private String stuId;
    private String src;

    public OutBook(String book, String stuId, String src) {
        this.book = book;
        this.stuId = stuId;
        this.src = src;
    }

    public String getBook() {
        return book;
    }

    public String getSrc() {
        return src;
    }

    public String getStuId() {
        return stuId;
    }
}
