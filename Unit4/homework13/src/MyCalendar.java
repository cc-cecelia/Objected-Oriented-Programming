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
            today = nextOpDate;
            BookSys.getIns().cleanOrderTimes();
            if (nextOpDate.toEpochDay() - sortDate.toEpochDay() >= 3) {
                sortDate = sortDate.plusDays(3);
                Library.getInstance().sort(sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                while (nextOpDate.isAfter(sortDate)) {
                    sortDate = sortDate.plusDays(3);
                }
                if (!sortDate.isEqual(nextOpDate)) {
                    sortDate = sortDate.minusDays(3);
                }
            }
        }
    }

    public void end() {
        if (!sortDate.plusDays(3).isAfter(LocalDate.parse("2023-12-31"))) {
            sortDate = sortDate.plusDays(3);
            Library.getInstance().sort(sortDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
