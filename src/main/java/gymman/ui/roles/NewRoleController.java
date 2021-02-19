package gymman.ui.roles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.Role;
import gymman.auth.RoleRepository;
import gymman.common.DuplicateEntityException;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import gymman.ui.selectable_list.SelectableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewRoleController implements Controller {
    private final AuthService authService;
    private final RoleRepository roleRepo;
    private final NavigationService nav;
    private Optional<Role> role = Optional.empty();

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private SelectableList<Permission> lstPermissions;

    private Alert alertMissingName = new Alert(AlertType.WARNING, "'Name' needs to be filled out.");
    private Alert alertDuplicateRole = new Alert(AlertType.WARNING, "A role with the same name already exists.");

    public NewRoleController(
        AuthService auth,
        RoleRepository roleRepo,
        NavigationService nav
    ) {
        this.authService = auth;
        this.roleRepo = roleRepo;
        this.nav = nav;
    }

    @Override
    public void onDisplay() {
        this.clearForm();
        List<Permission> permissions = this.authService.getRegisteredPermissions();
        permissions.sort((a, b) -> a.getName().compareTo(b.getName()));
        this.lstPermissions.setItems(permissions);

        final Object roleObj = this.nav.getNavParams().get("role");
        if (roleObj != null && Role.class.isInstance(roleObj)) {
            final Role role = Role.class.cast(roleObj);
            this.role = Optional.of(role);
            this.txtName.setText(role.getName());
            this.txtDescription.setText(role.getDescription());
            this.lstPermissions.setSelected(new ArrayList<>(role.getPermissions()));
        }
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void onSaveClicked() {
        if (this.txtName.getText().trim().isEmpty()) {
            this.alertMissingName.showAndWait();
            return;
        }

        Role.RoleBuilder roleBuilder = Role.builder().name(this.txtName.getText().trim())
            .description(this.txtDescription.getText().trim())
            .permissions(new HashSet<>(this.lstPermissions.getSelectedItems()));

        if (this.role.isPresent()) {
            roleBuilder.id(this.role.get().getId());
        }

        try {
            this.roleRepo.add(roleBuilder.build());
            this.clearForm();
            this.nav.backOr("page_role_list");
        } catch (DuplicateEntityException e) {
            this.alertDuplicateRole.showAndWait();
        }
    }

    private void clearForm() {
        this.txtName.clear();
        this.txtDescription.clear();
        this.lstPermissions.clear();
    }
}
