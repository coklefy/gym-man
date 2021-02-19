package gymman.ui.customers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import gymman.common.DuplicateEntityException;
import gymman.customers.Customer;
import gymman.customers.Customer.Gender;
import gymman.customers.CustomerRepository;
import gymman.customers.InvalidValueException;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import static gymman.ui.FormHelper.getTextTrimmed;
import static gymman.ui.FormHelper.highlightWrongField;
import static gymman.ui.FormHelper.isFilledOut;


/**
 * The Class CreateCustomerController implements the controller
 * that create a new customer or modify an existing customer.
 */
public final class CreateCustomerController implements Controller {

    /** The customer repository. */
    private final CustomerRepository customerRepo;

    /** The navigation service. */
    private final NavigationService navService;

    /** The optional customer to modify. */
    private Optional<Customer> customer = Optional.empty();

    /** The check functions. */
    private final Map<String, Function<String, Boolean>> checkFunctions = new HashMap<>();

    /** The text field first name. */
    @FXML
    private TextField txfFirstName;

    /** The text field last name. */
    @FXML
    private TextField txfLastName;

    /** The combo box gender. */
    @FXML
    private ComboBox<Gender> cmbGender;

    /** The text field fiscal code. */
    @FXML
    private TextField txfFiscalCode;

    /** The text field email. */
    @FXML
    private TextField txfEmail;

    /** The text field telephone number. */
    @FXML
    private TextField txfTelephoneNumber;

    /** The text field userName. */
    @FXML
    private TextField txfUsername;

    /** The form. */
    @FXML
    private Pane form;

    /** The text field birth date. */
    @FXML
    private TextField txfBirthdate;

    /** The button to create. */
    @FXML
    private Button btnCreate;

    /**
     * Instantiates a new customer controller.
     *
     * @param customerRepo the customer repository
     * @param navService the navigation service
     */
    public CreateCustomerController(
            final CustomerRepository customerRepo,
            final NavigationService navService
        ) {
            this.customerRepo = customerRepo;
            this.navService = navService;

            this.checkFunctions.put("txfFirstName", Customer::isNameValid);
            this.checkFunctions.put("txfLastName", Customer::isNameValid);
            this.checkFunctions.put("txfUsername", Customer::isNameValid);
            this.checkFunctions.put("txfFiscalCode", Customer::isFiscalcodeValid);
            this.checkFunctions.put("txfEmail", Customer::isEmailValid);
            this.checkFunctions.put("txfTelephoneNumber", Customer::isTelephoneNumberValid);
            this.checkFunctions.put("txfBirthdate", Customer::isBirthdateValid);
        }


    /**
     * Method to restrict text field for telephone number with only integer number in input.
     */
    public void initialize() {
        this.txfTelephoneNumber.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            final String newText = change.getControlNewText();
            if (newText.matches("[0-9]*")){
                return change;
            }

            return null;
        }));
    }

    /**
     * Method to create new customer if only each field are compiled correctly.
     * This method can also modify an existing customer.
     */
    @FXML
    private void create() {
        final Alert alertConfirmation = new Alert(AlertType.CONFIRMATION, "Creare il cliente?", ButtonType.YES, ButtonType.NO);
        alertConfirmation.showAndWait();
        if (alertConfirmation.getResult().equals(ButtonType.YES)) {
            try {
                final Customer.CustomerBuilder customerBuilder = Customer.builder()
                        .firstname(getTextTrimmed(this.txfFirstName).toLowerCase(Locale.getDefault()))
                        .lastname(getTextTrimmed(this.txfLastName).toLowerCase(Locale.getDefault()))
                        .gender(this.cmbGender.getValue())
                        .fiscalCode(getTextTrimmed(this.txfFiscalCode).toUpperCase(Locale.getDefault()))
                        .birthdate(LocalDate.parse(getTextTrimmed(this.txfBirthdate), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .email(getTextTrimmed(this.txfEmail).toLowerCase(Locale.getDefault()))
                        .telephoneNumber(getTextTrimmed(this.txfTelephoneNumber))
                        .username(getTextTrimmed(this.txfUsername));
            if (this.customer.isPresent()) {
                customerBuilder.id(this.customer.get().getId());
            }
                this.customerRepo.add(customerBuilder.build());
                clearForm();
                this.navService.back();
            } catch (InvalidValueException e) {
                new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            } catch (DateTimeParseException e) {
                new Alert(AlertType.ERROR, "Inserito valore non valido nel campo data di nascita", ButtonType.OK).showAndWait();
            } catch (DuplicateEntityException e) {
                new Alert(AlertType.WARNING, "Cliente già presente").showAndWait();
            }
        }
    }

    /**
     * Method to clear the form.
     */
    private void clearForm() {
        this.txfFirstName.clear();
        this.txfLastName.clear();
        this.txfBirthdate.clear();
        this.txfEmail.clear();
        this.txfFiscalCode.clear();
        this.txfTelephoneNumber.clear();
        this.txfUsername.clear();
        this.cmbGender.setValue(null);
    }

    /**
     * Method to deselect the fields of form.
     */
    private void deselectAllfields() {
        this.txfFirstName.setStyle("");
        this.txfLastName.setStyle("");
        this.txfFiscalCode.setStyle("");
        this.txfBirthdate.setStyle("");
        this.txfEmail.setStyle("");
        this.txfTelephoneNumber.setStyle("");
        this.txfUsername.setStyle("");
    }

    /**
     * Method that enable and disable the button to create if it is not selected
     * a value for gender.
     * This method is called when combo box change value.
     */
    @FXML
    private void onComboBoxChange() {
        this.btnCreate.setDisable(!isFilledOut(this.form) || this.cmbGender.getValue() == null);
    }

    /**
     * Method that enable and disable the button to create if all fields
     * are compiled and highlights the fields that it are not correctly compiled.
     * Therefore it is not possible to press the button when all fields are not
     * compiled.
     * This method is called when a field change value.
     *
     * @param event the event
     */
    @FXML
    private void onTextChange(final KeyEvent event) {
        this.btnCreate.setDisable(!isFilledOut(this.form) || this.cmbGender.getValue() == null);
        final TextField field = TextField.class.cast(event.getSource());
        final boolean res = this.checkFunctions.get(field.getId()).apply(field.getText());
        if (res) {
            field.setStyle("");
        } else {
            highlightWrongField(field);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplay() {
        customer = Optional.empty();
        this.btnCreate.setDisable(true);
        this.deselectAllfields();
        this.clearForm();
        this.cmbGender.setItems(FXCollections.observableArrayList(Gender.M, Gender.F));
        this.setCustomerInfoToModify();
    }

    /**
     * Method to compile all fields with the customer's information to modify
     * if it is present.
     */
    private void setCustomerInfoToModify() {
        final Object customerObj = this.navService.getNavParams().get("customer");
        if (customerObj != null && Customer.class.isInstance(customerObj)) {
            final Customer customer = Customer.class.cast(customerObj);
            this.customer = Optional.of(customer);
            this.txfFirstName.setText(customer.getFirstname());
            this.txfLastName.setText(customer.getLastname());
            this.cmbGender.setValue(customer.getGender());
            this.txfFiscalCode.setText(customer.getFiscalCode());
            this.txfEmail.setText(customer.getEmail());
            this.txfTelephoneNumber.setText(customer.getTelephoneNumber());
            this.txfUsername.setText(customer.getUsername());
            this.txfBirthdate.setText(customer.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }
}
