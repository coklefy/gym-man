package gymman.ui.dashboard;

import gymman.auth.AuthService;
import gymman.auth.NotLoggedInException;
import gymman.employees.Employee;
import gymman.employees.EmployeeRepository;
import gymman.ui.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController implements Controller {

    private AuthService auth;
    private EmployeeRepository employeeRepo;
    private Employee employee;

    @FXML
    private Label lblHello;

    public DashboardController(
        AuthService auth,
        EmployeeRepository employeeRepo
    ) {
        this.auth = auth;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public void onDisplay() {
        if (!this.auth.isLoggedIn()) {
            return;
        }

        this.employee = this.employeeRepo.getByUsername(this.auth.getLoggedInUser().getUsername()).get();

        String msg;
        try {
            msg = String.format("Ciao %s, scegli una pagina dal menu.",
                    this.employee.getFirstname());
            this.lblHello.setText(msg);
        } catch (NotLoggedInException e) {

        }
    }
}
