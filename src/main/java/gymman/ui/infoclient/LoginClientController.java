package gymman.ui.infoclient;

import java.util.Optional;

import gymman.customers.Customer;
import gymman.customers.CustomerRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * The Class LoginClientController.
 */
public class LoginClientController implements Controller {

    /** The txt user name. */
    @FXML private TextField txtUserName;

    /** The txt email. */
    @FXML private TextField txtEmail;

    /** The btnlogin. */
    @FXML private Button btnlogin;

    /** The customer. */
    private Optional<Customer> customer = Optional.empty();

    /** The nav. */
    private final NavigationService nav;

    /** The cust repo. */
    private final CustomerRepository custRepo;

    /**
     * Instantiates a new login client controller.
     *
     * @param custRepo the cust repo
     * @param nav the nav
     */
    public LoginClientController(final CustomerRepository custRepo,final NavigationService nav) {
        this.custRepo = custRepo;
        this.nav = nav;
    }

    /**
     * Initialize ui.
     */
    public void initialize() {
    }

    @Override
    public void onDisplay() {
    }

    /**
     * Login client.
     */
    @FXML
    public void loginClient() {
        if(login()) {
            this.nav.getNavParams().set("customer", customer.get());
            this.nav.navigate("page_customer_info");
        }else{
            final Alert alert = new Alert(AlertType.WARNING, "Dati Immessi Errati", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Login.
     *
     * @return true, if successful
     */
    public boolean login() {
        customer = custRepo.getAll().stream().filter(c -> c.getUsername().equals(this.txtUserName.getText().trim()))
                                             .filter(c -> c.getEmail().equals(this.txtEmail.getText().trim())).findFirst();
        return customer.isPresent();
    }
}
