import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ArrayList<RequestQueue> subQueue = new ArrayList<>();
        ArrayList<Elevator> eleList = new ArrayList<>();
        RequestQueue mainQueue = new RequestQueue();
        ArrayList<MtInfo> mtList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            RequestQueue newQueue = new RequestQueue();
            subQueue.add(newQueue);
            MtInfo mtInfo = new MtInfo();
            VerElevator elevator = new VerElevator(newQueue, i, 1, 6, 400, mainQueue, mtInfo);
            eleList.add(elevator);
            mtList.add(mtInfo);
            elevator.start();
        }
        ElevatorFac elevatorFac = new ElevatorFac();
        Dispatcher dispatcher = new Dispatcher(mainQueue, subQueue, elevatorFac, eleList, mtList);
        dispatcher.start();
        InputThread inputThread = new InputThread(mainQueue, elevatorFac);
        inputThread.start();
    }
}
