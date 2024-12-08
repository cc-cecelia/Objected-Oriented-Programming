import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ArrayList<RequestQueue> subQueue = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            RequestQueue newQueue = new RequestQueue();
            subQueue.add(newQueue);
            VerElevator elevator = new VerElevator(newQueue, i);
            elevator.start();
        }
        RequestQueue mainQueue = new RequestQueue();
        Dispatcher dispatcher = new Dispatcher(mainQueue, subQueue);
        dispatcher.start();
        InputThread inputThread = new InputThread(mainQueue);
        inputThread.start();
    }
}
