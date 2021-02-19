package gymman.ui.employees;

import gymman.ui.AbstractPage;

public class EditEmployeePage extends AbstractPage {
    public EditEmployeePage() {
        super(EditEmployeePage.class.getResource("EditEmployee.fxml"));
    }

    @Override
    public String getId() {
        return "page_employee_edit";
    }

    @Override
    public String getTitle() {
        return "Edit employee";
    }

    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}
