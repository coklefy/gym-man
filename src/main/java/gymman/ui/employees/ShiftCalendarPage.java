package gymman.ui.employees;

import gymman.ui.AbstractPage;

public class ShiftCalendarPage extends AbstractPage {

    public ShiftCalendarPage() {
        super(ShiftCalendarPage.class.getResource("ShiftCalendar.fxml"));
    }

    @Override
    public String getId() {
        return "page_shift_calendar";
    }

    @Override
    public String getTitle() {
        return "Calendario turni";
    }

    @Override
    public boolean hasMenuEntry() {
        return true;
    }

}
