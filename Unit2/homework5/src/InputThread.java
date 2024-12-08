import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private final RequestQueue mainQueue;

    public InputThread(RequestQueue input) {
        this.mainQueue = input;
    }

    @Override
    public void run() {
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = input.nextPersonRequest();
            if (request == null) {
                mainQueue.setEnd(true);
                break;
            } else {
                Request request1 = new Request(request.getFromFloor(),
                        request.getToFloor(), request.getPersonId());
                mainQueue.addRequest(request1);
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
