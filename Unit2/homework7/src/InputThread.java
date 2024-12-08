import com.oocourse.elevator3.*;

import java.io.IOException;

public class InputThread extends Thread {
    private final RequestQueue mainQueue;
    private final ElevatorFac elevatorFac;

    public InputThread(RequestQueue input, ElevatorFac elevatorFac) {
        this.mainQueue = input;
        this.elevatorFac = elevatorFac;
    }

    @Override
    public void run() {
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            Request request = input.nextRequest();
            if (request == null) {
                mainQueue.setEnd(true);
                elevatorFac.setEnd(true);
                break;
            } else {
                if (request instanceof PersonRequest) {
                    MyRequest request1 = new MyRequest(((PersonRequest) request).getFromFloor(),
                            ((PersonRequest) request).getToFloor(),
                            ((PersonRequest) request).getPersonId());
                    mainQueue.addRequest(request1);
                } else if (request instanceof ElevatorRequest) {
                    try {
                        elevatorFac.build(((ElevatorRequest) request).getElevatorId(),
                                ((ElevatorRequest) request).getFloor(),
                                ((ElevatorRequest) request).getCapacity(),
                                ((ElevatorRequest) request).getSpeed(),
                                ((ElevatorRequest) request).getAccess());
                        mainQueue.myNotify();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (request instanceof MaintainRequest) {
                    try {
                        elevatorFac.maintain(((MaintainRequest) request).getElevatorId());
                        mainQueue.myNotify();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
