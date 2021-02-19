package gymman.ui.employees;

import java.util.Optional;
import java.util.stream.Collectors;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.employees.Employee;
import gymman.employees.EmployeeRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class EmployeesController implements Controller {
    @FXML private TextField txtSearch;
    @FXML private Button btnNewEmployee;
    @FXML private FlowPane employeeList;

    private final EmployeeRepository employeeRepo;
    private final NavigationService nav;
    private final AuthService auth;

    private static final Permission PERM_EMPLOYEE_ADD = new PermissionImpl("employee_add", "Può aggiungere nuovi impiegati");
    private static final Permission PERM_EMPLOYEE_EDIT = new PermissionImpl("employee_edit", "Può modificare gli impiegati");
    private static final Permission PERM_EMPLOYEE_DELETE = new PermissionImpl("employee_delete", "Può cancellare impiegati");

    private final Alert alertDelete = new Alert(AlertType.CONFIRMATION, "Vuoi davvero eliminare questo dipendente?");

    private final ObservableList<Employee> listData = FXCollections.observableArrayList();

    public EmployeesController(
        EmployeeRepository employeeRepo,
        NavigationService nav,
        AuthService auth
    ) {
        this.employeeRepo = employeeRepo;
        this.nav = nav;
        this.auth = auth;

        auth.registerPermission(PERM_EMPLOYEE_ADD);
        auth.registerPermission(PERM_EMPLOYEE_EDIT);
        auth.registerPermission(PERM_EMPLOYEE_DELETE);

        this.listData.addListener((Change<? extends Employee> c) -> this.refreshList());
    }

    @FXML
    public void initialize() {
    }

    @Override
    public void onDisplay() {
        this.listData.setAll(this.employeeRepo.getAll());
        this.btnNewEmployee.setDisable(!this.auth.userHasPermission(PERM_EMPLOYEE_ADD));
    }

    @FXML
    public void onSearchTextChanged() {
        final String text = this.txtSearch.getText();
        if (text.isEmpty()) {
            this.listData.setAll(this.employeeRepo.getAll());
            return;
        }
        this.listData.setAll(this.employeeRepo.searchByName(text));
    }

    @FXML
    public void onNewEmployeeClicked() {
        this.nav.navigate("page_employee_edit");
    }

    private void refreshList() {
        this.employeeList.getChildren().setAll(
            this.listData.stream()
                .map(e -> this.getCardForEmployee(e))
                .collect(Collectors.toList())
        );
    }

    private Pane getCardForEmployee(Employee employee) {
        EmployeeCard card = new EmployeeCard(employee);

        card.onEdit(e -> {
            this.nav.getNavParams().set("employee", e);
            this.nav.navigate("page_employee_edit");
        });

        card.onDelete(e -> {
            Optional<ButtonType> res = this.alertDelete.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                this.employeeRepo.remove(e);
                this.listData.remove(e);
            }
        });

        card.setEditEnabled(this.auth.userHasPermission(PERM_EMPLOYEE_EDIT));
        card.setDeleteEnabled(this.auth.userHasPermission(PERM_EMPLOYEE_DELETE));

        return card;
    }
}
