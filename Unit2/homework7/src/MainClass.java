import com.oocourse.elevator3.TimableOutput;

import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        HashMap<Integer, RequestQueue> subQueue = new HashMap<>();
        RequestQueue mainQueue = new RequestQueue();
        HashMap<Integer, MtInfo> mtList = new HashMap<>();
        SemaStorey sema = new SemaStorey();
        for (int i = 1; i < 7; i++) {
            RequestQueue newQueue = new RequestQueue();
            subQueue.put(i, newQueue);
            MtInfo mtInfo = new MtInfo();
            VerElevator elevator = new VerElevator(newQueue, i, 1, 6, 400, mainQueue, mtInfo, sema);
            mtList.put(i, mtInfo);
            elevator.start();
        }
        ElevatorFac elevatorFac = new ElevatorFac();
        Dispatcher dispatcher = new Dispatcher(mainQueue, subQueue, elevatorFac, mtList, sema);
        dispatcher.start();
        InputThread inputThread = new InputThread(mainQueue, elevatorFac);
        inputThread.start();
    }
}
