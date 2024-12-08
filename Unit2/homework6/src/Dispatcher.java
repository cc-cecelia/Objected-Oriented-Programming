import java.util.ArrayList;

public class Dispatcher extends Thread {
    private final RequestQueue mainQueue;
    private final ArrayList<RequestQueue> subQueues;
    private final ElevatorFac elevatorFac;
    private int flag = 0;
    private final ArrayList<Elevator> eleList;
    private final ArrayList<MtInfo> mtList;
    private final ArrayList<MtInfo> totalMtList;

    public Dispatcher(RequestQueue mainQueue, ArrayList<RequestQueue> subQueues,
                      ElevatorFac eleFac, ArrayList<Elevator> eleList, ArrayList<MtInfo> mtList) {
        this.mainQueue = mainQueue;
        this.subQueues = subQueues;
        this.elevatorFac = eleFac;
        this.eleList = eleList;
        this.mtList = mtList;
        this.totalMtList = new ArrayList<>();
        this.totalMtList.addAll(mtList);
    }

    @Override
    public void run() {
        while (true) {
            // TimableOutput.println("In**********");
            if (mainQueue.isEmpty() && mainQueue.isEnd() && elevatorFac.isEnd() &&
                    !elevatorFac.isIfAdd() && !elevatorFac.isIfMaintain()) {
                boolean ifEnd = true;
                for (MtInfo m : totalMtList) {
                    if (!m.getIfOut() && m.getIfMt()) {
                        ifEnd = false;
                        try {
                            m.myWait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
                if (ifEnd) {
                    for (RequestQueue r : this.subQueues) {
                        // System.out.println("subqueue set end " + subQueues.indexOf(r));
                        r.setEnd(true);
                    }
                    return;
                }
            }
            EleInfo newEle = elevatorFac.getEleInfo();
            if (newEle != null) {
                addEle(newEle);
            }
            int mtId = elevatorFac.getMtId();
            if (mtId != -1) {
                for (int i = 0; i < eleList.size(); i++) {
                    Elevator e = eleList.get(i);
                    if (e.getMyId() == mtId) {
                        // System.out.println("find succeed " + mtId);
                        RequestQueue mtRequest = subQueues.get(i);
                        mtRequest.clear(mainQueue);
                        mtList.get(i).setMtInfo(true);
                        subQueues.get(i).setEnd(true);
                        mtList.remove(i);
                        subQueues.remove(i);
                        eleList.remove(i);
                        // System.out.println("maintain succeed" + mtId);
                        break;
                    }
                }
            }
            MyRequest myRequest = this.mainQueue.getMainReq(elevatorFac);
            if (myRequest == null) {
                continue;
            }
            flag = (flag + 1) % subQueues.size();
            subQueues.get(flag).addRequest(myRequest);
            // TimableOutput.println("Out**********");
        }
    }

    public void addEle(EleInfo newEle) {
        RequestQueue newQueue = new RequestQueue();
        subQueues.add(newQueue);
        MtInfo mtInfo = new MtInfo();
        VerElevator elevator = new VerElevator(newQueue, newEle.getId(), newEle.getCurStorey(),
                newEle.getCapacity(), (int) (1000 * newEle.getMoveTime()), mainQueue, mtInfo);
        elevator.start();
        eleList.add(elevator);
        mtList.add(mtInfo);
        totalMtList.add(mtInfo);
        // System.out.println("add succeed" + newEle.getId());
    }
}
