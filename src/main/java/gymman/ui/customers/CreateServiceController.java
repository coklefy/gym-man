package gymman.ui.customers;

import java.util.Optional;
import gymman.common.DuplicateEntityException;
import gymman.customers.AdditionalService;
import gymman.customers.AdditionalServiceRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

import static gymman.ui.FormHelper.getTextTrimmed;
import static gymman.ui.FormHelper.isFilledOut;

/**
 * The Class CreateServiceController implements the controller
 * that create a new additional service or modify an existing
 * additional service..
 */
public final class CreateServiceController implements Controller {

    /** The additional service repository. */
    private final AdditionalServiceRepository addServiceRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The service. */
    private Optional<AdditionalService> service = Optional.empty();

    /** The text field for name. */
    @FXML
    private TextField txfName;

    /** The text area for description. */
    @FXML
    private TextArea txaDescription;

    /** The text field for price. */
    @FXML
    private TextField txfPrice;

    /** The button to create. */
    @FXML
    private Button btnCreate;

    /** The form. */
    @FXML
    private Pane form;

    /**
     * Instantiates a new additional service controller.
     *
     * @param addServiceRepo the additional service repository
     * @param navService the navigation service
     */
    public CreateServiceController(final AdditionalServiceRepository addServiceRepo, final NavigationService navService) {
        this.addServiceRepo = addServiceRepo;
        this.navService = navService;
    }

    /**
     * Method to restrict text field for price with only numerical values in input.
     */
    public void initialize() {
        this.txfPrice.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            final String newText = change.getControlNewText();
            if (newText.matches("^\\d{0,2}+[\\.]{0,1}\\d{0,2}$")) {
                return change;
            }
             return null;

        }));
    }

    /**
     * Method to create the new additional service if only each field are compiled correctly.
     * This method can also modify an existing additional service.
     */
    @FXML
    private void createService() {
        this.deselectAllfields();

        this.txfPrice.setText(this.txfPrice.getText().trim());

        final Alert alert = new Alert(AlertType.CONFIRMATION, "Creare il servizio aggiuntivo?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                final AdditionalService.AdditionalServiceBuilder addServiceBuilder = AdditionalService.builder()
                    .name(getTextTrimmed(this.txfName).toLowerCase())
                    .description(this.txaDescription.getText().trim())
                    .price(Double.valueOf(getTextTrimmed(this.txfPrice)));
            if (this.service.isPresent()) {
                addServiceBuilder.id(this.service.get().getId());
            }
                this.addServiceRepo.add(addServiceBuilder.build());
                clearForm();
                this.navService.back();
            } catch (DuplicateEntityException e) {
                new Alert(AlertType.WARNING, "Servizio già presente", ButtonType.OK).showAndWait();
            } catch (NumberFormatException e) {
                new Alert(AlertType.ERROR, "Inserito valore non valido nel campo prezzo", ButtonType.OK).showAndWait();
            }
        }
    }

    /**
     * Method to clear the form.
     */
    private void clearForm() {
        this.txfName.clear();
        this.txaDescription.clear();
        this.txfPrice.clear();
    }

    /**
     * Method to deselect the fields of form.
     */
    private void deselectAllfields() {
        this.txfName.setStyle("");
        this.txaDescription.setStyle("");
        this.txfPrice.setStyle("");
    }


    /**
     * Method that enable and disable the button to create if all fields
     * are compiled.
     */
    @FXML
    private void onTextChange() {
        this.btnCreate.setDisable(!isFilledOut(this.form));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplay() {
        this.service = Optional.empty();
        this.btnCreate.setDisable(true);

        this.deselectAllfields();

        this.clearForm();

        this.setServiceInfoToModify();

    }

    /**
     * Method to compile all fields with the information of additional service
     * to modify if it is present.
     */
    private void setServiceInfoToModify() {
        final Object addServiceObj = this.navService.getNavParams().get("addService");
        if (addServiceObj != null && AdditionalService.class.isInstance(addServiceObj)) {
            final AdditionalService service = AdditionalService.class.cast(addServiceObj);
            this.service = Optional.of(service);
            this.txfName.setText(service.getName());
            this.txaDescription.setText(service.getDescription());
            this.txfPrice.setText(String.valueOf(service.getPrice()));
        }
    }

}
