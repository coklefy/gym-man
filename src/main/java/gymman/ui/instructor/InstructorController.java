package gymman.ui.instructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.customers.Customer;
import gymman.customers.CustomerRepository;
import gymman.customers.Registration;
import gymman.customers.RegistrationRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

/**
 * Class to implement the instructor controller. There is the customers
 * table and the customer card. Everyone has the permission to see these
 * informations. In this interface, the user that has the permissions,
 * can go the Customer's  TrainingProgram page or can decide
 * to create a new one.
 *
 */
public class InstructorController implements Controller {
    /** repository of all customers. */
    private final CustomerRepository customerRepo;
    /** repository of all registrations. */
    private final RegistrationRepository registrationRepo;
    /** service of authentication. */
    private final AuthService authService;
    /** service of navigation. */
    private final NavigationService nav;
    /** customers list to fulfill the table. */
    private final ObservableList<Customer> myCustomerList = FXCollections.observableArrayList();


    /** pane of the customer card. To be hidden if no row has been selected */
    @FXML private Pane rightPane;
    /** interface labels. */
    @FXML private Label emailLabel, birthdayLabel, durationLabel, signInLabel, typeLabel, cardNameLabel;
    /** Customer table. Each row represents a customer */
    @FXML private TableView<Customer> customersTable;
    /** search field to filter the list customers rows. */
    @FXML private TextField searchInputTxt;
    /**  icon that if clicked, changes the interface to Training Program . */
    @FXML private ImageView programIcon;
    /** Surname interface column. */
    @FXML private TableColumn<Customer, String> surnameCol = new TableColumn<>("Cognome");
    /** Name interface column. */
    @FXML private TableColumn<Customer, String> nameCol = new TableColumn<>("Nome");
    /** CoficeFiscale interface column. */
    @FXML private TableColumn<Customer, String> codiceFiscaleCol = new TableColumn<>("CF");
    /** Contact interface column. */
    @FXML private TableColumn<Customer, String> telephoneCol = new TableColumn<>("Telefono");
    /** Birthday interface column. */
    @FXML private TableColumn<Customer, String> birthdayCol = new TableColumn<>("Nato il");

    /** Permission to view all the customers list. **/
    private final Permission viewInfoBox = new PermissionImpl("view_info_box", "Vedi le view card dei clienti");

    /**
     * Constructor of the instructor interface controller.
     * Uploads the table of the customers and their card
     * @param nav : navigation service
     * @param customerRepo : all customers repository
     * @param registrationRepo : customers registrations repository
     * @param auth : authentication service to control users permissions
     */
    public InstructorController(
            final NavigationService nav,
            final CustomerRepository customerRepo,
            final RegistrationRepository registrationRepo,
            final AuthService auth
     ) {
        this.nav = nav;
        this.customerRepo = customerRepo;
        this.registrationRepo = registrationRepo;
        this.authService = auth;
        this.authService.registerPermission(viewInfoBox);
    }

    /**
     * Method to initialize the view.
     */
    @FXML
    public void initialize() {

        surnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        codiceFiscaleCol.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));
        birthdayCol.setCellValueFactory(new PropertyValueFactory<>("birthdate"));

        attachListenerToRows();
        this.rightPane.setVisible(false);

        this.myCustomerList.setAll(this.customerRepo.getAll());
        this.customersTable.setItems(this.myCustomerList);
    }



    /**
     * Method to update the view on user navigation.
     */
    @Override
    public void onDisplay() {
        /*
         * Check if user has the permission to see
         * customer training program.
         */
    	this.programIcon.setVisible(false);
        this.rightPane.setVisible(false);
        this.programIcon.setVisible(this.authService.userHasPermission(viewInfoBox));
        this.myCustomerList.setAll(this.customerRepo.getItems());
    }


    private void attachListenerToRows() {
        this.customersTable.setRowFactory(t -> {
            final TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    this.programIcon.setVisible(this.authService.userHasPermission(viewInfoBox));
                    this.rightPane.setVisible(this.authService.userHasPermission(viewInfoBox));
                    passDataToController(row.getItem());
                    showCustomerCard(row.getItem());
                }
            });
            return row;
        });
    }

    /*
     * Method to upload the customer card. If the customer has no
     * registration saved, throw NullPointerException.
     *
     */
    private void showCustomerCard(final Customer customer) {
        this.rightPane.setVisible(this.authService.userHasPermission(viewInfoBox));
       if (customer != null) {

             if (this.registrationRepo.getByIdClient(customer.getUsername()).size() > 0) {
                final List<Registration> regList = this.registrationRepo.getByIdClient(customer.getUsername());
                final Registration customerReg = regList.get(regList.size() - 1);

                this.cardNameLabel.setText(customer.getFirstname() + " " + customer.getLastname());
                this.birthdayLabel.setText(customer.getBirthdate().toString());
                this.durationLabel.setText(customerReg.getDuration() + " days");
                this.typeLabel.setText(customerReg.getType().getName());
                this.signInLabel.setText(customerReg.getSigningDate().toString());
                this.emailLabel.setText(customer.getEmail());
                this.rightPane.setVisible(true);
            }
        } else {
            throw new NullPointerException("Customer has not membership saved.");
        }
    }


    private void passDataToController(final Customer customer) {

        if (customer != null) {
             if (this.registrationRepo.getByIdClient(customer.getUsername()).size() > 0) {
              	final List<Registration> regList = this.registrationRepo.getByIdClient(customer.getUsername());
                final Registration customerReg = regList.get(regList.size() - 1);

                this.nav.getNavParams().set("customer", customer);
                this.nav.getNavParams().set("type", customerReg.getType().getName());
                this.nav.getNavParams().set("sinceDate", customerReg.getSigningDate());
                this.nav.getNavParams().set("duration", customerReg.getDuration());
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Method to filter the customers from the table. Any time the user inserts
     * characters to the field, the table with the rows will be updated.
     * This method will call a static method to be sure the first character of the
     * inserted string will be uppercase.
     */
    @FXML
    public void onSearchTextChanged() {
        final String text = controlledInput(this.searchInputTxt.getText());
        if (text.isEmpty()) {
            this.myCustomerList.setAll(this.customerRepo.getAll());
            return;
        }

        this.myCustomerList.setAll(Stream.of(this.customerRepo.searchByUserName(text).stream(),
        		this.customerRepo.searchByName(text).stream()).flatMap(e -> e).distinct().collect(Collectors.toList()));

    }

    /**
     * Method to be called when the user has clicked to see the customer
     * training program.
     */
    public void callProgramView() {

        this.nav.navigate("page_trainingProgram");
    }


    private static String controlledInput(final String txt) {
        String text = "";
        if (!txt.isEmpty()) {
            text = text + txt.substring(0, 1).toUpperCase() + txt.substring(1).toLowerCase();
        }
        return text;
    }
}
