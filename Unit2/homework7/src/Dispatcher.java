import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Dispatcher extends Thread {
    private final RequestQueue mainQueue;
    private final HashMap<Integer, RequestQueue> subQueues;
    private final ElevatorFac elevatorFac;
    private final HashMap<Integer, MtInfo> mtList;
    private final ArrayList<MtInfo> totalMtList;
    private final SemaStorey semaStorey;
    private final HashMap<Integer, ArrayList<Integer>> access;

    public Dispatcher(RequestQueue mainQueue, HashMap<Integer, RequestQueue> subQueues,
                      ElevatorFac eleFac, HashMap<Integer, MtInfo> mtList, SemaStorey semaStorey) {
        this.mainQueue = mainQueue;
        this.subQueues = subQueues;
        this.elevatorFac = eleFac;
        this.mtList = mtList;
        this.totalMtList = new ArrayList<>();
        for (Integer i : mtList.keySet()) {
            this.totalMtList.add(mtList.get(i));
        }
        this.semaStorey = semaStorey;
        this.access = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            for (int j = 1; j <= 11; j++) {
                tmp.add(j);
            }
            access.put(i,tmp);
        }
    }

    @Override
    public void run() {
        while (true) {
            // TimableOutput.println("In**********");
            if (mainQueue.isEmpty() && mainQueue.isEnd() && elevatorFac.isEnd() &&
                    !elevatorFac.isIfAdd() && !elevatorFac.isIfMaintain()
                    && mainQueue.isTranEnd()) {
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
                    for (Integer i : subQueues.keySet()) {
                        // System.out.println("subqueue set end " + subQueues.indexOf(r));
                        subQueues.get(i).setEnd(true);
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
                RequestQueue mtRequest = subQueues.get(mtId);
                mtRequest.clear(mainQueue);
                mtList.get(mtId).setMtInfo(true);
                subQueues.get(mtId).setEnd(true);
                mtList.remove(mtId);
                subQueues.remove(mtId);
                access.remove(mtId);
            }
            MyRequest myRequest = this.mainQueue.getMainReq(elevatorFac);
            if (myRequest == null) {
                continue;
            }
            // flag = (flag + 1) % subQueues.size();
            // subQueues.get(flag).addRequest(myRequest);
            dispatch(myRequest);
            // TimableOutput.println("Out**********");
        }
    }

    public void addEle(EleInfo newEle) {
        RequestQueue newQueue = new RequestQueue();
        int id = newEle.getId();
        subQueues.put(id, newQueue);
        MtInfo mtInfo = new MtInfo();
        access.put(id, newEle.getAccess());
        VerElevator elevator = new VerElevator(newQueue, id, newEle.getCurStorey(),
                newEle.getCapacity(), (int) (1000 * newEle.getMoveTime()),
                mainQueue, mtInfo, semaStorey);
        elevator.start();
        mtList.put(id, mtInfo);
        totalMtList.add(mtInfo);
        // System.out.println("add succeed" + newEle.getId());
    }

    public void dispatch(MyRequest request) {
        if (request.isTran()) { hasDispatched(request); }
        else {
            int fromStorey = request.getFromStorey();
            int toStorey = request.getToStorey();
            int weight = 100000;
            ArrayList<Integer> route = null;
            ArrayList<Integer> roStorey = null;
            Queue<ArrayList<Integer>> queue = new LinkedList<>();
            Queue<ArrayList<Integer>> queueStorey = new LinkedList<>();
            for (Integer i : access.keySet()) {
                if (access.get(i).contains(fromStorey)) {
                    queueAdd(i, queue, fromStorey, queueStorey); }
            }
            while (!queue.isEmpty()) {
                ArrayList<Integer> sequence = queue.poll();
                ArrayList<Integer> ele = queueStorey.poll();
                int lastEle = sequence.get(sequence.size() - 1);
                if (access.get(lastEle).contains(toStorey)) {
                    int myWeight = 0;
                    for (Integer integer : sequence) {
                        myWeight += subQueues.get(integer).getSize();
                    }
                    myWeight *= sequence.size();
                    if (myWeight < weight) {
                        weight = myWeight;
                        route = sequence;
                        roStorey = ele;
                    }
                } else {
                    addIntoSeq(sequence, lastEle, ele, toStorey, queue, queueStorey);
                }
            }
            if (route.size() == 1) { subQueues.get(route.get(0)).addRequest(request); }
            else {
                mainQueue.tranIncrease();
                roStorey.add(toStorey);
                request.setIsTran(true);
                request.setToStorey(roStorey.get(1));
                // System.out.println(request.getId()+" "+route.get(0)+" "+roStorey.get(1));
                for (int i = 1; i < route.size(); i++) {
                    request.setTran(roStorey.get(i + 1), route.get(i));
                    // System.out.println(request.getId()+" "+route.get(i)+" "+roStorey.get(i+1));
                }
                subQueues.get(route.get(0)).addRequest(request);
            }
        }
    }

    public void hasDispatched(MyRequest request) {
        int eleId = request.iterate();
        if (!access.containsKey(eleId)) {
            request.clear();
            mainQueue.tranDecrease();
            dispatch(request);
        } else { subQueues.get(eleId).addRequest(request); }
    }

    public void queueAdd(int i, Queue<ArrayList<Integer>> queue,
                         int fromStorey, Queue<ArrayList<Integer>> queueStorey) {
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(i);
        queue.add(tmp);
        ArrayList<Integer> tmpp = new ArrayList<>();
        tmpp.add(fromStorey);
        queueStorey.add(tmpp);
    }

    public void addIntoSeq(ArrayList<Integer> sequence, int lastEle, ArrayList<Integer> ele,
                           int toStorey, Queue<ArrayList<Integer>> queue,
                           Queue<ArrayList<Integer>> queueStorey) {
        for (Integer elevator : access.keySet()) {
            if (sequence.contains(elevator)) { continue; }
            int delta = 12;
            int stoTmp = 0;
            for (Integer storey : access.get(lastEle)) {
                if (ele.contains(storey)) { continue; }
                if (access.get(elevator).contains(storey)
                        && Math.abs(toStorey - storey) < delta) {
                    stoTmp = storey;
                    delta = Math.abs(toStorey - storey);
                }
            }
            if (delta < 12) {
                ArrayList<Integer> newSe = new ArrayList<>(sequence);
                newSe.add(elevator);
                queue.add(newSe);
                ArrayList<Integer> newEle = new ArrayList<>(ele);
                newEle.add(stoTmp);
                queueStorey.add(newEle);
            }
        }
    }
}
