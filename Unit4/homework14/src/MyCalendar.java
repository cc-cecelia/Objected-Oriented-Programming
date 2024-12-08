import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyCalendar {
    private LocalDate today;
    private LocalDate sortDate;

    public MyCalendar() {
        today = LocalDate.parse("2023-01-01");
        sortDate = LocalDate.parse("2023-01-01");
    }

    public void update(LocalDate nextOpDate) {
        if (!(today.isEqual(nextOpDate))) {
            //-------------library is closed------------------------
            UnionSys.getInstance().reserve();
            UnionSys.getInstance().printForToday(today.format(DateTimeFormatter.ISO_LOCAL_DATE));
            UnionSys.getInstance().printForNextDay(today.plusDays(1)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE));
            UnionSys.getInstance().cleanOrderTimes();
            UnionSys.getInstance().clearInterships();
            //-------------library is closed------------------------
            today = nextOpDate;
            if (nextOpDate.toEpochDay() - sortDate.toEpochDay() >= 3) {
                sortDate = sortDate.plusDays(3);
                UnionSys.getInstance().purchase(sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                System.out.println("[" + sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        + "] arranging librarian arranged all the books");
                UnionSys.getInstance().sort(sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                while (nextOpDate.isAfter(sortDate.plusDays(3))
                        || nextOpDate.isEqual(sortDate.plusDays(3))) {
                    sortDate = sortDate.plusDays(3);
                    System.out.println("[" + sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                            + "] arranging librarian arranged all the books");
                }
            }
        }
    }

    public void endLastDay() {
        UnionSys.getInstance().reserve();
        UnionSys.getInstance().printForToday(today.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public void end(Library library) {
        if (!sortDate.plusDays(3).isAfter(LocalDate.parse("2023-12-31"))) {
            sortDate = sortDate.plusDays(3);
            library.sort(sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
