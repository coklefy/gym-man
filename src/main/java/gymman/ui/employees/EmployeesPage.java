package gymman.ui.employees;

import gymman.ui.AbstractPage;

public class EmployeesPage extends AbstractPage {
    public EmployeesPage() {
        super(EmployeesPage.class.getResource("Employees.fxml"));
    }

    @Override
    public String getId() {
        return "page_employee_list";
    }

    @Override
    public String getTitle() {
        return "Staff";
    }

    @Override
    public boolean hasMenuEntry() {
        return true;
    }
}
