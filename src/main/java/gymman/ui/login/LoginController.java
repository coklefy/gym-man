package gymman.ui.login;

import gymman.auth.AuthService;
import gymman.auth.LoginException;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Controller {

    private AuthService auth;
    private NavigationService navService;

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    public LoginController(AuthService auth, NavigationService navService) {
        this.auth = auth;
        this.navService = navService;
    }

    @Override
    public void onDisplay() {

    }

    public void btnLoginClicked() {
        try {
            this.auth.login(this.txtUsername.getText(), this.txtPassword.getText());
            this.clearForm();
            this.navService.navigate("page_dashboard");
        } catch (LoginException e) {
            new Alert(AlertType.ERROR, "Impossibile accedere. Username o password errati.")
                .show();
        }
    }

    public void btnLoginAsCustomerClicked() {
        this.navService.navigate("page_customer_login");
    }

    public void textChanged() {
        boolean inputsAreEmpty = this.txtUsername.getText().trim().isEmpty()
            || this.txtPassword.getText().trim().isEmpty();
        this.btnLogin.setDisable(inputsAreEmpty);
    }

    private void clearForm() {
        this.txtUsername.clear();
        this.txtPassword.clear();
    }
}
