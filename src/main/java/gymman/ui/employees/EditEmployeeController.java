package gymman.ui.employees;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import gymman.auth.AuthService;
import gymman.auth.Role;
import gymman.auth.RoleRepository;
import gymman.common.DuplicateEntityException;
import gymman.employees.Employee;
import gymman.employees.EmployeeService;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditEmployeeController implements Controller {
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final NavigationService nav;
    private final RoleRepository roleRepo;
    private Optional<Employee> employee = Optional.empty();

    @FXML private TextField txtUsername;
    @FXML private TextField txtFirstname;
    @FXML private TextField txtLastname;
    @FXML private TextField txtBirthdate;
    @FXML private TextField txtFiscalCode;
    @FXML private TextField txtAddress;
    @FXML private TextField txtPhone;
    @FXML private TextField txtPassword;
    @FXML private TextField txtPasswordAgain;
    @FXML private ComboBox<Role> cmbRole;
    @FXML private Button btnSave;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Alert alertDuplicateEmployee = new Alert(AlertType.WARNING,
            "Un impiegato con lo stesso username esiste già");

    public EditEmployeeController(AuthService auth, EmployeeService employeeService,
            NavigationService nav, RoleRepository roleRepo) {
        this.authService = auth;
        this.employeeService = employeeService;
        this.nav = nav;
        this.roleRepo = roleRepo;
    }

    @Override
    public void onDisplay() {
        this.clearForm();
        this.btnSave.setDisable(true);

        this.cmbRole.setItems(FXCollections.observableArrayList(this.roleRepo.getAll()));

        final Object employeeObj = this.nav.getNavParams().get("employee");
        if (employeeObj != null && Employee.class.isInstance(employeeObj)) {
            final Employee employee = Employee.class.cast(employeeObj);
            final Role role = this.employeeService.getRoleByEmployee(employee);

            this.employee = Optional.of(employee);
            this.txtUsername.setText(employee.getUsername());
            this.txtFirstname.setText(employee.getFirstname());
            this.txtLastname.setText(employee.getLastname());
            this.txtBirthdate.setText(this.dateFormatter.format(employee.getBirthdate()));
            this.txtFiscalCode.setText(employee.getFiscalCode());
            this.txtAddress.setText(employee.getAddress());
            this.txtPhone.setText(employee.getPhone());
            this.cmbRole.setValue(role);
        }
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void onSaveClicked() {
        if (!this.txtPassword.getText().isEmpty()
                && !this.txtPassword.getText().equals(this.txtPasswordAgain.getText())) {
            new Alert(AlertType.WARNING, "Le password non coincidono").showAndWait();
            return;
        }

        Employee.EmployeeBuilder employeeBuilder = Employee.builder()
            .username(this.txtUsername.getText())
            .firstname(this.txtFirstname.getText())
            .lastname(this.txtLastname.getText())
            .birthdate(LocalDate.parse(txtBirthdate.getText(), dateFormatter))
            .fiscalCode(this.txtFiscalCode.getText())
            .address(this.txtAddress.getText()).phone(this.txtPhone.getText());

        if (this.employee.isPresent()) {
            employeeBuilder.id(this.employee.get().getId());
        }

        try {
            this.employeeService.addEmployee(employeeBuilder.build(), this.txtPassword.getText(), this.cmbRole.getValue());
            this.clearForm();
            this.nav.backOr("page_employee_list");
        } catch (DuplicateEntityException e) {
            this.alertDuplicateEmployee.showAndWait();
        } catch (DateTimeParseException e) {
            new Alert(AlertType.WARNING, "La data inserita non è corretta").showAndWait();
        } catch (IllegalArgumentException e) {
            new Alert(AlertType.WARNING, "Uno o più campi non sono corretti: " + e.getMessage()).showAndWait();
        }
    }

    public void onFormInputChange() {
        boolean filledOut = !isEmpty(this.txtUsername)
            && !isEmpty(this.txtFirstname)
            && !isEmpty(this.txtLastname)
            && !isEmpty(this.txtBirthdate)
            && !isEmpty(this.txtFiscalCode)
            && !isEmpty(this.txtAddress)
            && !isEmpty(this.txtPhone)
            && this.cmbRole.getValue() != null;

        this.btnSave.setDisable(!filledOut);
    }

    private void clearForm() {
        this.txtUsername.clear();
        this.txtFirstname.clear();
        this.txtLastname.clear();
        this.txtBirthdate.clear();
        this.txtFiscalCode.clear();
        this.txtAddress.clear();
        this.txtPhone.clear();
        this.txtPassword.clear();
        this.txtPasswordAgain.clear();
        this.cmbRole.setValue(null);
    }

    private static boolean isEmpty(TextField field) {
        return field.getText().trim().isEmpty();
    }
}
