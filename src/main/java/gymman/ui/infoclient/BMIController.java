package gymman.ui.infoclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import gymman.common.DuplicateEntityException;
import gymman.customers.Customer;
import gymman.customers.InvalidValueException;
import gymman.infoclient.Bmi;
import gymman.infoclient.BmiRepository;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * The Class BMIController.
 */
public class BMIController implements Controller {

    /** The txt weight. */
    @FXML private TextField txtWeight;

    /** The txt height. */
    @FXML private TextField txtHeight;

    /** The lst BMI. */
    @FXML private TableView<Bmi> lstBMI;

    /** The clm weight. */
    @FXML private TableColumn<Bmi, Double> clmWeight;

    /** The clm height. */
    @FXML private TableColumn<Bmi, Double> clmHeight;

    /** The clm date. */
    @FXML private TableColumn<Bmi, String> clmDate;

    /** The clm buttons. */
    @FXML private TableColumn<Bmi, Void> clmButtons;

    /** The bmirepo. */
    private final BmiRepository bmirepo;

    /** The nav. */
    private final NavigationService nav;

    /** The customer. */
    private Optional<Customer> customer = Optional.empty();

    /** The bminew. */
    private Optional<Bmi> bminew = Optional.empty();

    /** The listbmi. */
    private final ObservableList<Bmi> listbmi = FXCollections.observableArrayList();

    /** The alert duplicate BMI. */
    private final Alert alertDuplicateBmi = new Alert(AlertType.WARNING, "Data Inserimento uguale");

    /** The alert Wrong Value BMI. */
    private final Alert alertWrongValue= new Alert(AlertType.WARNING, "Valori Inseriti Errati");

    /**
     * Instantiates a new BMI controller.
     *
     * @param bmirepo the bmirepo
     * @param nav the nav
     */
    public BMIController(
            final BmiRepository bmirepo,
            final NavigationService nav)
    {
        this.bmirepo = bmirepo;
        this.nav = nav;
    }

    /**
     * Initialize.
     */
    public void initialize() {
        this.clmWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.clmHeight.setCellValueFactory(new PropertyValueFactory<>("height"));
        this.clmDate.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy")))
            );
        final ButtonsCellFactory<Bmi> factory = new ButtonsCellFactory<>(4,4);
        factory.addButton("Modifica", (bmi) ->{
            bminew = Optional.of(bmi);
            this.txtHeight.setText(bmi.getHeight().toString());
            this.txtWeight.setText(bmi.getWeight().toString());
        });
        factory.addButton("Elimina", bmi -> {
            final Alert alert = new Alert(AlertType.CONFIRMATION, "Sicuro di Cancellare?", ButtonType.YES,ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult().equals(ButtonType.YES)) {
                bmirepo.remove(bmi);
                // System.out.println(lstBMI.getItems().get(0).getHeight().toString());
                this.nav.getNavParams().set("customer", customer.get());
                listbmi.remove(bmi);
                lstBMI.refresh();
            }
        });
        this.clmButtons.setCellFactory(factory.getFactory());
        this.lstBMI.setItems(listbmi);
    }

    /*
     * onDisplay()
     */
    @Override
    public void onDisplay() {
        cleartxt();
        this.listbmi.setAll(this.bmirepo.getAll());
        this.customer = Optional.empty();
        final Object customerObj = this.nav.getNavParams().get("customer");
        if(customerObj != null && Customer.class.isInstance(customerObj)) {
            final Customer cust = Customer.class.cast(customerObj);
            this.customer = Optional.of(cust);
            this.lstBMI.setItems(
                    listbmi.filtered(c -> c.getCustomerid().equals(cust.getId())));
            }
        this.bminew = Optional.empty();
        final Object bmiObj = this.nav.getNavParams().get("bmi");
        if(bmiObj != null && Bmi.class.isInstance(bmiObj)){
            final Bmi bmis = Bmi.class.cast(bmiObj);
            this.bminew = Optional.of(bmis);
            this.txtHeight.setText(bminew.get().getHeight().toString());
            this.txtWeight.setText(bminew.get().getWeight().toString());
        }
        this.nav.getNavParams().set("customer", customer.get());
    }


    /**
     * Creates the BMI.
     */
    @FXML
    public void create() {
        if(this.isFilledOut()) {
            final Alert alert = new Alert(AlertType.CONFIRMATION, "Aggiungere i dati?", ButtonType.YES,ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult().equals(ButtonType.YES)) {
                final Bmi.BmiBuilder bmiBuilder = Bmi.builder()
                        .weight(Double.valueOf(this.txtWeight.getText().trim()))
                        .height(Double.valueOf(this.txtHeight.getText().trim()))
                        .date(LocalDateTime.now())
                        .customerid(this.customer.get().getId());
                if(this.bminew.isPresent()) {
                    bmiBuilder.id(this.bminew.get().getId());
               }
                try {
                    final Bmi bminew = bmiBuilder.build();
                    this.bmirepo.add(bminew);
                    cleartxt();
                    this.nav.getNavParams().set("customer", customer.get());
                    this.nav.backOr("page_customer_info");
                }catch (DuplicateEntityException e) {
                    this.alertDuplicateBmi.showAndWait();
                }catch (InvalidValueException e) {
                    this.alertWrongValue.showAndWait();
                }
            }
        }
    }

    /**
     * Checks if is filled out.
     *
     * @return true, if is filled out
     */
    private boolean isFilledOut() {
        Boolean test = false;
        this.txtHeight.setStyle("");
        this.txtWeight.setStyle("");
        String error = "Errore nei i campi (inserire il punto non la virgola) \n";
        if (!this.txtHeight.getText().trim().matches("[0-9]+(\\.[0-9]+){0,1}")
            || Double.parseDouble(this.txtHeight.getText()) > 2.4
            || Double.parseDouble(this.txtHeight.getText()) < 0.50){
            this.highlight(this.txtHeight);
            error += " - Altezza \n";
            test = true;
        }
        if(!this.txtWeight.getText().trim().matches("[0-9]+(\\.[0-9]+){0,1}")
           || Double.parseDouble(this.txtWeight.getText()) < 20
           || Double.parseDouble(this.txtWeight.getText()) > 300 ){
            this.highlight(this.txtWeight);
            error += " - Peso \n";
            test = true;
        }
        if (test) {
            final Alert alert=new Alert(AlertType.WARNING,error,ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Highlight the textbox with wrong value.
     *
     * @param cont the cont
     */
    private void highlight(final Control cont) {
        cont.setStyle("-fx-border-color: red");
    }

    /**
     * method for clear txt value.
     */
    private void cleartxt() {
        this.txtHeight.setText("");
        this.txtWeight.setText("");
    }
}