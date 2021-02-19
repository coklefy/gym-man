package gymman.ui.customers;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.customers.AdditionalService;
import gymman.customers.AdditionalServiceRepository;
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
 * The Class ServiceController implements the controller that manages additional services.
 */
public final class ServiceController implements Controller {

    /** The table for additional service. */
    @FXML
    private TableView<AdditionalService> serviceTable;

    /** The text field to search. */
    @FXML
    private TextField txfSearch;

    /** The button to create. */
    @FXML
    private Button btnCreate;

    /** The column for the name of additional service. */
    @FXML
    private TableColumn<AdditionalService, String> nameCol;

    /** The column for the description of additional service. */
    @FXML
    private TableColumn<AdditionalService, String> descriptionCol;

    /** The column for the price of additional service. */
    @FXML
    private TableColumn<AdditionalService, Double> priceCol;

    /** The column for buttons. */
    @FXML
    private TableColumn<AdditionalService, Void> buttonsCol;

    /** The service repository. */
    private final AdditionalServiceRepository serviceRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The authentication service. */
    private final AuthService authService;

    /** The permission to create an additional service. */
    private final Permission createService = new PermissionImpl("service_create", "Creazione servizio aggiuntivo");

    /** The permission to delete an additional service. */
    private final Permission deleteService = new PermissionImpl("service_delete", "Cancellazione servizio aggiuntivo");

    /** The list of additional services with which fill in the table. */
    private final ObservableList<AdditionalService> serviceList = FXCollections.observableArrayList();

    /**
     * Instantiates a new additional service controller.
     *
     * @param serviceRepo the service repository
     * @param navService the navigation service
     * @param authService the authentication service
     */
    public ServiceController(final AdditionalServiceRepository serviceRepo,
                              final NavigationService navService,
                              final AuthService authService
            ) {
        this.serviceRepo = serviceRepo;
        this.navService = navService;
        this.authService = authService;
        this.authService.registerPermission(createService);
        this.authService.registerPermission(deleteService);

    }

    /**
     * Method that initializes the table of additional services and adds buttons
     * to edit and delete additional services.
     */
    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        final ButtonsCellFactory<AdditionalService> buttonsFactory = new ButtonsCellFactory<>(4, 4);

        buttonsFactory.addButton("Modifica",
                this.authService.userHasPermission(createService),
                addService -> {
            this.navService.getNavParams().set("addService", addService);
            this.navService.navigate("page_additionalService_creation");
        });

        buttonsFactory.addButton("Elimina",
                this.authService.userHasPermission(deleteService),
                service -> {
                    final Alert alertDelete = new Alert(AlertType.CONFIRMATION, "Sicuro di voler eliminare questo servizio?", ButtonType.YES, ButtonType.NO);
                    alertDelete.showAndWait();
                    if (alertDelete.getResult().equals(ButtonType.YES)) {
                        serviceRepo.remove(service);
                        serviceTable.getItems().remove(service);
                    }
        });

        buttonsCol.setCellFactory(buttonsFactory.getFactory());

        serviceTable.setItems(serviceList);
    }

    /**
     * Method to search an additional service called when a new character
     * is typed.
     */
    @FXML
    private void onSearchTextChanged() {
        final String text = this.txfSearch.getText().toLowerCase();
        if (text.trim().isEmpty()) {
            this.serviceList.setAll(this.serviceRepo.getAll());
            return;
        }
        this.serviceList.setAll(this.serviceRepo.searchByName(text));
    }

    /**
     * Method to open the page to create a new additional service.
     */
    @FXML
    private void handleNewService() {
        this.navService.navigate("page_additionalService_creation");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplay() {
        this.btnCreate.setDisable(!this.authService.userHasPermission(createService));
        this.serviceList.setAll(this.serviceRepo.getList());
    }

}
