package gymman.ui.customers;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.customers.Customer;
import gymman.customers.Registration;
import gymman.customers.RegistrationRepository;
import gymman.customers.SubscriptionType;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * The Class RegistrationController implements the controller
 * that manages registrations about a customer.
 */
public final class RegistrationController implements Controller {

    /** The table for registrations. */
    @FXML
    private TableView<Registration> registrationTable;

    /** The column for subscription. */
    @FXML
    private TableColumn<Registration, SubscriptionType> subscriptionCol;

    /** The column for deadline of registration. */
    @FXML
    private TableColumn<Registration, String> lastDayCol;

    /** The column for buttons. */
    @FXML
    private TableColumn<Registration, Void> buttonsCol;

    /** The label for signing date. */
    @FXML
    private Label lbSigninDate;

    /** The label for duration. */
    @FXML
    private Label lbDuration;

    /** The label for total price. */
    @FXML
    private Label lbPrice;

    /** The label for discount. */
    @FXML
    private Label lbDiscount;

    /** The box for additional services. */
    @FXML
    private VBox vboxService;

    /** The registration repository. */
    private final RegistrationRepository registrationRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The authentication service. */
    private final AuthService authService;

    /** The permission to edit registration. */
    private final Permission editRegistration = new PermissionImpl("registration_edit", "Modifica una iscrizione");

    /** The permission to delete registration. */
    private final Permission deleteRegistration = new PermissionImpl("registration_delete", "elimina una iscrizione");

    /** The list of registrations with which fill in the table. */
    private final ObservableList<Registration> registrationList = FXCollections.observableArrayList();

    private Customer customer;


    /**
     * Instantiates a new registration controller.
     *
     * @param registrationRepo the registration repository
     * @param navService the navigation service
     * @param authService the authentication service
     */
    public RegistrationController(
            final RegistrationRepository registrationRepo,
            final NavigationService navService,
            final AuthService authService
        ) {
            this.registrationRepo = registrationRepo;
            this.navService = navService;
            this.authService = authService;
            this.authService.registerPermission(deleteRegistration);
            this.authService.registerPermission(editRegistration);
        }

    /**
     * Method that initialize the table of registrations and adds buttons
     * to edit and to delete registrations.
     */
    @FXML
    private void initialize() {
        subscriptionCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        lastDayCol.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getSigningDate().plusMonths(cellData.getValue().getDuration())
                                .format(DateTimeFormatter.ofPattern("dd/MM/YYYY")).toString()));

        final ButtonsCellFactory<Registration> buttonsFactory = new ButtonsCellFactory<>(4, 4);

        buttonsFactory.addButton("Modifica",
        this.authService.userHasPermission(editRegistration),
                registration -> {
            this.navService.getNavParams().set("registration", registration);
            this.navService.getNavParams().set("customer", customer);
            this.navService.navigate("page_registration_creation");
        });


        buttonsFactory.addButton("Elimina",
                this.authService.userHasPermission(deleteRegistration),
                registration -> {
                    final Alert alertDelete = new Alert(AlertType.CONFIRMATION, "Sicuro di voler eliminare questa iscrizione?", ButtonType.YES, ButtonType.NO);
                    alertDelete.showAndWait();
                    if (alertDelete.getResult().equals(ButtonType.YES)) {
                        registrationRepo.remove(registration);
                        registrationTable.getItems().remove(registration);
                    }
        });

        buttonsCol.setCellFactory(buttonsFactory.getFactory());
        registrationTable.setItems(registrationList);
    }

    /**
     * Sets the information of registration when a cell is selected.
     */
    @FXML
    private void setRegistrationInfo() {
        this.vboxService.getChildren().clear();
        final Registration registration = this.registrationTable.getSelectionModel().getSelectedItem();
        if (registration != null) {
            this.lbSigninDate.setText(registration.getSigningDate() .format(DateTimeFormatter.ofPattern("dd/MM/YYYY")).toString());
            this.lbDuration.setText(String.valueOf(registration.getDuration()));
            this.lbPrice.setText(String.valueOf(new BigDecimal(registration.getPrice()).setScale(2, BigDecimal.ROUND_UP).doubleValue()));
            this.lbDiscount.setText(String.valueOf(registration.getDiscount() + " %"));

            if (this.vboxService.getChildren().isEmpty() && !registration.getAdditionalService().isEmpty()) {
                registration.getAdditionalService().stream()
                .forEach(c -> this.vboxService.getChildren().add(new Label(" " + c.getName() + " " + String.valueOf(c.getPrice()))));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplay() {
        this.vboxService.getChildren().clear();
        final Object customerObj = this.navService.getNavParams().get("customer");
        if (customerObj != null && Customer.class.isInstance(customerObj)) {
            customer = Customer.class.cast(customerObj);
            this.registrationList.setAll(this.registrationRepo.getByIdClient(customer.getId()));
        }
   }
}
