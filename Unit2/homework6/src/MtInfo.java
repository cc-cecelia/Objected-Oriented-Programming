public class MtInfo {
    private boolean ifMt;
    private boolean ifOut;

    public MtInfo() {
        this.ifOut = false;
        this.ifMt = false;
    }

    public synchronized void setMtInfo(boolean ifMt) {
        this.ifMt = ifMt;
    }

    public synchronized void setIfOut(boolean ifOut) { this.ifOut = ifOut; }

    public synchronized boolean getIfMt() {
        return this.ifMt;
    }

    public synchronized boolean getIfOut() { return this.ifOut; }

    public  synchronized void myWait() throws InterruptedException {
        wait();
    }

    public synchronized void myNotify() { notifyAll(); }
}
