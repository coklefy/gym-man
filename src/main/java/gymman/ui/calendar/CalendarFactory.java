package gymman.ui.calendar;

import java.util.List;

public class CalendarFactory {
    private CalendarFactory() {}

    public static WeekView week(List<CalendarEntry> entries) {
        WeekView week = new WeekView();
        week.setEntries(entries);
        return week;
    }
}
