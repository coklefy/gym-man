package gymman.ui.customers;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.customers.Customer;
import gymman.customers.Customer.Gender;
import gymman.customers.CustomerRepository;
import gymman.customers.RegistrationRepository;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The Class CustomerController implements the controller that manages customers.
 */
public final class CustomerController implements Controller {

    /** The text field to search a customer. */
    @FXML
    private TextField txtSearch;

    /** The customer table. */
    @FXML
    private TableView<Customer> customerTable;

    /** The button to create a customer. */
    @FXML
    private Button btnCreate;

    /** The column for userName. */
    @FXML
    private TableColumn<Customer, String> usernameCol;

    /** The column for first name. */
    @FXML
    private TableColumn<Customer, String> firstnameCol;

    /** The column for last name. */
    @FXML
    private TableColumn<Customer, String> lastnameCol;

    /** The column for gender. */
    @FXML
    private TableColumn<Customer, Gender> genderCol;

    /** The column for fiscal code. */
    @FXML
    private TableColumn<Customer, String> fiscalCodeCol;

    /** The column for birth date. */
    @FXML
    private TableColumn<Customer, String> birthdateCol;

    /** The column for email. */
    @FXML
    private TableColumn<Customer, String> emailCol;

    /** The column for telephone number. */
    @FXML
    private TableColumn<Customer, String> telephoneNumberCol;

    /** The column for buttons. */
    @FXML
    private TableColumn<Customer, Void> buttonsCol;

    /** The customer repository. */
    private final CustomerRepository customerRepo;

    /** The registration repository. */
    private final RegistrationRepository registrationRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The authorization service. */
    private final AuthService authService;

    /** The permission to create customer. */
    private final Permission createCustomer = new PermissionImpl("customer_create", "Creazione cliente");

    /** The permission to delete customer. */
    private final Permission deleteCustomer = new PermissionImpl("customer_delete", "Cancellazione cliente");

    /** The permission to create registration. */
    private final Permission createRegistration = new PermissionImpl("registration_create", "Creazione iscrizione");



    /** The list of customers with which fill in the table. */
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    /**
     * Instantiates a new customer controller.
     *
     * @param customerRepo the customer repository
     * @param registrationRepo the registration repository
     * @param navService the navigation service
     * @param authService the authorization service
     */
    public CustomerController(
            final CustomerRepository customerRepo,
            final NavigationService navService,
            final AuthService authService,
            final RegistrationRepository registrationRepo
        ) {
            this.customerRepo = customerRepo;
            this.navService = navService;
            this.authService = authService;
            this.registrationRepo = registrationRepo;
            this.authService.registerPermission(createCustomer);
            this.authService.registerPermission(createRegistration);
            this.authService.registerPermission(deleteCustomer);
        }

    /**
     * Method that initializes the table of customers and adds the buttons
     * to create new registrations, to edit customers,
     * to delete customers and to show the registrations about a customer.
     */
    @FXML
    private void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));

        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        fiscalCodeCol.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));

        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        birthdateCol.setCellValueFactory(cellData ->
                                    new SimpleStringProperty(cellData.getValue().getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        telephoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));

        final ButtonsCellFactory<Customer> buttonsFactory = new ButtonsCellFactory<>(4, 4);

        buttonsFactory.addButton(
            "Modifica",
            this.authService.userHasPermission(createCustomer),
            customer -> {
                this.navService.getNavParams().set("customer", customer);
                this.navService.navigate("page_customer_creation");
        });

        buttonsFactory.addButton(
            "Nuova iscrizione",
            this.authService.userHasPermission(createRegistration),
            customer -> {
            this.navService.getNavParams().set("customer", customer);
            this.navService.navigate("page_registration_creation");
        });

        buttonsFactory.addButton("Mostra iscrizioni", customer -> {
            this.navService.getNavParams().set("customer", customer);
            this.navService.navigate("page_registrations_list");
        });

        buttonsFactory.addButton("Elimina",
                this.authService.userHasPermission(deleteCustomer),
                customer -> {
                    final Alert alertDelete = new Alert(AlertType.CONFIRMATION, "Sicuro di voler eliminare questo cliente", ButtonType.YES, ButtonType.NO);
                    alertDelete.showAndWait();
                    if (alertDelete.getResult().equals(ButtonType.YES)) {
                        this.customerRepo.remove(customer);
                        this.registrationRepo.getByIdClient(customer.getId())
                        .stream()
                        .forEach(e -> this.registrationRepo.remove(e));
                        customerTable.getItems().remove(customer);
                    }
        });

        buttonsCol.setCellFactory(buttonsFactory.getFactory());
        customerTable.setItems(customerList);
    }

    /**
     * Method to search by user name or name a customer called when a new character
     * is typed.
     */
    @FXML
    private void onSearchTextChanged() {
        final String text = this.txtSearch.getText().toLowerCase(Locale.getDefault());
        if (text.trim().isEmpty()) {
            this.customerList.setAll(this.customerRepo.getAll());
            return;
        }
        this.customerList.setAll(Stream.of(this.customerRepo.searchByUserName(text).stream(),
                this.customerRepo.searchByName(text).stream()).flatMap(e -> e).distinct().collect(Collectors.toList()));

    }

    /**
     * Method to open the page to create a new customer.
     */
    @FXML
    private void handleNewCustomer() {
        this.navService.navigate("page_customer_creation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplay() {
        this.btnCreate.setDisable(!this.authService.userHasPermission(createCustomer));
        this.customerList.setAll(this.customerRepo.getItems());
        this.navService.getNavParams().get("customer");
    }

}
