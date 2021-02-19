package gymman.ui.calendar;

import java.util.List;

public interface CalendarView {
    void setEntries(List<CalendarEntry> entries);
    void refresh();
}
