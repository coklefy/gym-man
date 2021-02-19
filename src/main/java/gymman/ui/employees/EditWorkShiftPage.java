package gymman.ui.employees;

import gymman.ui.AbstractPage;

public class EditWorkShiftPage extends AbstractPage {
    public EditWorkShiftPage() {
        super(EditWorkShiftPage.class.getResource("EditWorkShift.fxml"));
    }

    @Override
    public String getId() {
        return "page_workshift_edit";
    }

    @Override
    public String getTitle() {
        return "Modifica turni";
    }

    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}
