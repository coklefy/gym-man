package gymman.ui.customers;

import java.util.Optional;

import gymman.common.DuplicateEntityException;
import gymman.customers.SubscriptionType;
import gymman.customers.SubscriptionTypeRepository;
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
import static gymman.ui.FormHelper.isFilledOut;;


/**
 * The Class CreateSubscriptionController implements the controller
 * that create a new subscription or modify an existing subscription.
 */
public final class CreateSubscriptionController implements Controller {

    /** The subscription repository. */
    private final SubscriptionTypeRepository subscriptionRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The subscription. */
    private Optional<SubscriptionType> subscription = Optional.empty();

    /** The text field for userName. */
    @FXML
    private TextField txfUsername;

    /** The text field for description. */
    @FXML
    private TextArea txaDescription;

    /** The text field for unit price. */
    @FXML
    private TextField txfUnitPrice;

    /** The button to create. */
    @FXML
    private Button btnCreate;

    /** The form. */
    @FXML
    private Pane form;

    /**
     * Instantiates a new creates the subscription controller.
     *
     * @param subscriptionRepo the subscription repository
     * @param navService the navigation service
     */
    public CreateSubscriptionController(
            final SubscriptionTypeRepository subscriptionRepo,
            final NavigationService navService
        ) {
            this.subscriptionRepo = subscriptionRepo;
            this.navService = navService;
        }

    /**
     * Method to restrict text field for price with only numerical values in input.
     */
    public void initialize() {
        this.txfUnitPrice.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            final String newText = change.getControlNewText();
            if (newText.matches("^\\d{0,2}+[\\.]{0,1}\\d{0,2}$")) {
                return change;
            }

            return null;

        }));
    }

    /**
     * Method to create the new subscription if only each field are compiled correctly.
     * This method can also modify an existing subscription.
     */
    @FXML
    private void create() {
        this.deselectAllfields();
        final Alert alert = new Alert(AlertType.CONFIRMATION, "Creare l'abbonamento?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
            final SubscriptionType.SubscriptionTypeBuilder subscriptionBuilder = SubscriptionType.builder()
                    .name(getTextTrimmed(this.txfUsername).toLowerCase())
                    .description(this.txaDescription.getText())
                    .unitPrice(Double.valueOf(getTextTrimmed(this.txfUnitPrice)));
            if (this.subscription.isPresent()) {
                subscriptionBuilder.id(this.subscription.get().getId());
            }
                this.subscriptionRepo.add(subscriptionBuilder.build());
                clearForm();
                this.navService.back();
            } catch (DuplicateEntityException e) {
                new Alert(AlertType.WARNING, "Abbonamento già presente", ButtonType.OK).showAndWait();
            } catch (NumberFormatException e) {
                new Alert(AlertType.ERROR, "Inserito valore non valido nel campo prezzo", ButtonType.OK).showAndWait();
            }
        }
    }

    /**
     * Method to deselect the fields of form.
     */
    private void deselectAllfields() {
        this.txfUsername.setStyle("");
        this.txaDescription.setStyle("");
        this.txfUnitPrice.setStyle("");
    }

    /**
     * Method to clear the form.
     */
    private void clearForm() {
        this.txfUsername.clear();
        this.txaDescription.clear();
        this.txfUnitPrice.clear();
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
        this.subscription = Optional.empty();
        this.btnCreate.setDisable(true);

        this.clearForm();

        this.deselectAllfields();

        this.setSubscriptionInfoToModify();

    }

    /**
     * Method to compile all fields with the information of subscription
     * to modify if it is present.
     */
    private void setSubscriptionInfoToModify() {
        final Object subscriptionObj = this.navService.getNavParams().get("subscription");
        if (subscriptionObj != null && SubscriptionType.class.isInstance(subscriptionObj)) {
            final SubscriptionType subscription = SubscriptionType.class.cast(subscriptionObj);
            this.subscription = Optional.of(subscription);
            this.txfUsername.setText(subscription.getName());
            this.txaDescription.setText(subscription.getDescription());
            this.txfUnitPrice.setText(String.valueOf(subscription.getUnitPrice()));
        }
    }
}
