package gymman.ui.roles;

import java.util.Optional;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.auth.Role;
import gymman.auth.RoleRepository;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class RolesController implements Controller {
    @FXML private TextField txtSearch;
    @FXML private Button btnNewRole;
    @FXML private TableView<Role> lstRoles;
    @FXML private TableColumn<Role, String> colName;
    @FXML private TableColumn<Role, String> colDescription;
    @FXML private TableColumn<Role, Void> colButtons;

    private final RoleRepository roleRepo;
    private final NavigationService nav;

    private ObservableList<Role> listData = FXCollections.observableArrayList();

    private static final Permission PERM_ROLE_ADD = new PermissionImpl("role_add", "Può aggiungere nuovi ruoli");

    private final Alert alertDelete = new Alert(AlertType.CONFIRMATION, "Vuoi davvero cancellare questo ruolo?");

    public RolesController(
        RoleRepository roleRepo,
        NavigationService nav,
        AuthService auth
    ) {
        this.roleRepo = roleRepo;
        this.nav = nav;

        auth.registerPermission(PERM_ROLE_ADD);
    }

    @FXML
    public void initialize() {
        this.colName.setCellValueFactory(
            cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName())
        );

        this.colDescription.setCellValueFactory(
            cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription())
        );

        final ButtonsCellFactory<Role> buttonsFactory = new ButtonsCellFactory<>(4, 4);

        buttonsFactory.addButton("Modifica", role -> {
            this.nav.getNavParams().set("role", role);
            this.nav.navigate("page_role_edit");
        });

        buttonsFactory.addButton("Elimina", role -> {
            Optional<ButtonType> res = this.alertDelete.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                this.roleRepo.remove(role);
                this.refreshList();
            }
        });

        this.colButtons.setCellFactory(buttonsFactory.getFactory());
        this.lstRoles.setItems(this.listData);
    }

    @Override
    public void onDisplay() {
        this.refreshList();
    }

    @FXML
    public void onSearchTextChanged() {
        final String text = this.txtSearch.getText();
        if (text.trim().isEmpty()) {
            this.listData.setAll(this.roleRepo.getAll());
            return;
        }
        this.listData.setAll(this.roleRepo.searchByName(text));
    }

    @FXML
    public void onNewRoleClicked() {
        this.nav.navigate("page_role_edit");
    }

    private void refreshList() {
        this.listData.setAll(this.roleRepo.getAll());
    }
}
