package gymman.ui.customers;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.customers.SubscriptionType;
import gymman.customers.SubscriptionTypeRepository;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The Class SubscriptionController implements the controller that manages subscriptions.
 */
public final class SubscriptionController implements Controller {

    /** The table for subscriptions. */
    @FXML
    private TableView<SubscriptionType> subscriptionTable;

    /** The text field to search. */
    @FXML
    private TextField txtSearch;

    /** The button to create. */
    @FXML
    private Button btnCreate;

    /** The column for the name of subscription. */
    @FXML
    private TableColumn<SubscriptionType, String> nameCol;

    /** The column for the description of subscription. */
    @FXML
    private TableColumn<SubscriptionType, String> descriptionCol;

    /** The column for the price of subscription. */
    @FXML
    private TableColumn<SubscriptionType, String> priceCol;

    /** The column for buttons. */
    @FXML
    private TableColumn<SubscriptionType, Void> buttonsCol;

    /** The subscription repository. */
    private final SubscriptionTypeRepository subscriptionRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The authentication service. */
    private final AuthService authService;

    /** The permission to create a subscription. */
    private final Permission createSubscription = new PermissionImpl("subscription_create", "Creazione abbonamento");

    /** The permission to delete a subscription. */
    private final Permission deleteSubscription = new PermissionImpl("subscription_delete", "Cancellazione abbonamento");

    /** The list of subscriptions with which fill in the table. */
    private final ObservableList<SubscriptionType> subscriptionList = FXCollections.observableArrayList();



    /**
     * Instantiates a new subscription controller.
     *
     * @param subscriptionRepo the subscription repository
     * @param navService the navigation service
     * @param authService the authentication service
     */
    public SubscriptionController(
            final SubscriptionTypeRepository subscriptionRepo,
            final NavigationService navService,
            final AuthService authService
        ) {
            this.subscriptionRepo = subscriptionRepo;
            this.navService = navService;
            this.authService = authService;
            this.authService.registerPermission(createSubscription);
            this.authService.registerPermission(deleteSubscription);
        }


    /**
     * Method that initializes the table of subscriptions and adds buttons.
     */
    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        final ButtonsCellFactory<SubscriptionType> buttonsFactory = new ButtonsCellFactory<>(4, 4);

        buttonsFactory.addButton("Modifica",
                this.authService.userHasPermission(createSubscription),
                subscription -> {
            this.navService.getNavParams().set("subscription", subscription);
            this.navService.navigate("page_subscription_creation");
        });

        buttonsFactory.addButton(
                "Elimina",
                this.authService.userHasPermission(deleteSubscription),
                subscription -> {
                    final Alert alertDelete = new Alert(AlertType.CONFIRMATION, "Sicuro di voler eliminare questo abbonamento?", ButtonType.YES, ButtonType.NO);
                    alertDelete.showAndWait();
                    if (alertDelete.getResult().equals(ButtonType.YES)) {
                        subscriptionRepo.remove(subscription);
                        subscriptionTable.getItems().remove(subscription);
                    }
        });

        buttonsCol.setCellFactory(buttonsFactory.getFactory());

        subscriptionTable.setItems(subscriptionList);
    }

    /**
     * Method to search a subscription called when a new character
     * is typed.
     */
    @FXML
    private void onSearchTextChanged() {
        final String text = this.txtSearch.getText().toLowerCase();
        if (this.txtSearch.getText().trim().isEmpty()) {
            this.subscriptionList.setAll(this.subscriptionRepo.getAll());
            return;
        }
        this.subscriptionList.setAll(this.subscriptionRepo.searchByName(text));
    }

    /**
     * Method to open the page to create a new subscription.
     */
    @FXML
    private void handleNewSubscription() {
        this.navService.navigate("page_subscription_creation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplay() {
        this.btnCreate.setDisable(!this.authService.userHasPermission(createSubscription));
        this.subscriptionList.setAll(this.subscriptionRepo.getItems());
    }
}
