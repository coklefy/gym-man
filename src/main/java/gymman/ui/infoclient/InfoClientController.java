package gymman.ui.infoclient;

import java.math.BigDecimal;
import java.util.Optional;
import gymman.customers.AdditionalService;
import gymman.customers.AdditionalServiceRepository;
import gymman.customers.Customer;
import gymman.customers.Registration;
import gymman.customers.RegistrationRepository;
import gymman.infoclient.Bmi;
import gymman.infoclient.BmiRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 * The Class InfoClientController.
 */
public class InfoClientController implements Controller {

    /** The lbl name surname. */
    @FXML private Label lblNameSurname;

    /** The lbl date. */
    @FXML private Label lblDate;

    /** The lbl fiscal code. */
    @FXML private Label lblFiscalCode;

    /** The lbl email. */
    @FXML private Label lblEmail;

    /** The lbl phone. */
    @FXML private Label lblPhone;

    /** The lbl weight. */
    @FXML private Label lblWeight;

    /** The lbl height. */
    @FXML private Label lblHeight;

    /** The lbl BMI. */
    @FXML private Label lblBMI;

    /** The lbl tipologia abb. */
    @FXML private Label lblTipologiaAbb;

    /** The lbl data abb. */
    @FXML private Label lblDataAbb;

    /** The lbl durata abb. */
    @FXML private Label lblDurataAbb;

    /** The btn updates. */
    @FXML private Button btnUpdates;

    /** The btn scheda. */
    @FXML private Button btnScheda;

    /** The grd graph bmi. */
    @FXML private GridPane grdGraphBmi;

    /** The lbl bmi 1. */
    @FXML private Label lblBmi1;

    /** The lbl bmi 2. */
    @FXML private Label lblBmi2;

    /** The lbl bmi 3. */
    @FXML private Label lblBmi3;

    /** The lbl bmi 4. */
    @FXML private Label lblBmi4;

    /** The lbl bmi 5. */
    @FXML private Label lblBmi5;

    /** The lbl bmi 6. */
    @FXML private Label lblBmi6;

    /** The lbl bmi 7. */
    @FXML private Label lblBmi7;

    /** The lbl bmi 8. */
    @FXML private Label lblBmi8;

    /** The lbl bmi 9. */
    @FXML private Label lblBmi9;

    /** The lbl bmi 10. */
    @FXML private Label lblBmi10;

    /** The lbl bmi 11. */
    @FXML private Label lblBmi11;

    /** The lbl bmi 12. */
    @FXML private Label lblBmi12;

    /** The service table. */
    @FXML private TableView<AdditionalService> serviceTable;

    /** The name col. */
    @FXML private TableColumn<AdditionalService, String> nameCol;

    /** The description col. */
    @FXML private TableColumn<AdditionalService, String> descriptionCol;

    /** The bmirepo. */
    private final BmiRepository bmirepo;

    /** The customer. */
    private Optional<Customer> customer = Optional.empty();

    /** The service repo. */
    private final AdditionalServiceRepository serviceRepo;

    /** The regi repo. */
    private final RegistrationRepository regiRepo;

    /** The nav. */
    private final NavigationService nav;

    /**
     * Instantiates a new info client controller.
     *
     * @param serviceRepo the service repo
     * @param regiRepo the regi repo
     * @param bmirepo the bmirepo
     * @param nav the nav
     */
    public InfoClientController(
            final AdditionalServiceRepository serviceRepo,
            final RegistrationRepository regiRepo,
            final BmiRepository bmirepo,
            final NavigationService nav) {
        this.serviceRepo=serviceRepo;
        this.regiRepo = regiRepo;
        this.bmirepo = bmirepo;
        this.nav = nav;
    }


    /**
     * Initialize.
     */
    public void initialize() {}

    /*
     * Set all value on label
     */
    @Override
    public void onDisplay() {
        clearAll();
        //PRIMO SETTORE DATI PERSONALI
        this.customer = Optional.empty();
        final Object customerObj = this.nav.getNavParams().get("customer");
        if(customerObj != null && Customer.class.isInstance(customerObj)) {
            final Customer cust = Customer.class.cast(customerObj);
            this.customer = Optional.of(cust);
            this.lblEmail.setText(cust.getEmail().toString());
            this.lblPhone.setText(cust.getTelephoneNumber().toString());
            this.lblFiscalCode.setText(cust.getFiscalCode().toString());
            this.lblDate.setText(cust.getBirthdate().toString());
            this.lblNameSurname.setText(cust.getFirstname().toString() + " " + cust.getLastname().toString());

            // SECONDO SETTORE CALCOLO BMI
            final Optional<Bmi> bmis = this.bmirepo.getByCustomerId(cust.getId()).stream()
                    .sorted((a,b) -> b.getDate().compareTo(a.getDate()))
                    .findFirst();
           styleclear();
           if(bmis.isPresent()) {
               this.lblHeight.setText(
                       bmis.get().getHeight().toString());
               this.lblWeight.setText(
                       bmis.get().getWeight().toString());
               final String valore = new BigDecimal(bmis.get().getBmicalc()).setScale(2,BigDecimal.ROUND_UP).toString();
               this.lblBMI.setText(valore + GetCalcBmi(Double.valueOf(valore)));
           }
           // TERZO SETTORE ABBONAMENTI
           final Optional<Registration> regis = this.regiRepo.getByIdClient(cust.getId()).stream().findFirst();

           if(regis.isPresent()) {
               this.lblDurataAbb.setText(String.valueOf(regis.get().getDuration()));
               this.lblDataAbb.setText(regis.get().getSigningDate().plusDays(regis.get().getDuration()*30).toString());
               this.lblTipologiaAbb.setText(regis.get().getType().getName().toString());
           }
           final ObservableList<AdditionalService> values = FXCollections.observableArrayList(this.serviceRepo.getByIdClient(cust.getId()));
           if(!values.isEmpty()) {
               nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
               descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
           }
        }
    }

   /**
    * Clear all.
    */
   public void clearAll() {
       this.lblDate.setText("Data");
       this.lblBMI.setText("BMI");
       this.lblHeight.setText("Altezza");
       this.lblWeight.setText("Peso");
       this.lblEmail.setText("Email");
       this.lblFiscalCode.setText("Codice Fiscale");
       this.lblNameSurname.setText("Nome Cognome");
       this.lblPhone.setText("Numero Tel.");
       this.lblDataAbb.setText("Data Scadenza");
       this.lblDurataAbb.setText("Durata");
       this.lblTipologiaAbb.setText("Tipologia");
       initialize();
   }

   /**
    * Gets the calc bmi.
    *
    * @param media the media
    * @return the string
    */
   public String GetCalcBmi(final Double media) {
       String messageBmi = "";
      if(media < 18.5) {
           this.lblBmi1.setStyle("-fx-background-color: #66c2ff ");
           this.lblBmi2.setStyle("-fx-background-color: #66c2ff ");
           messageBmi += " Sottopeso";
      } else if (media >= 18.5 && media <= 24.9) {
           this.lblBmi3.setStyle("-fx-background-color: Aqua");
           this.lblBmi4.setStyle("-fx-background-color: Aqua");
           messageBmi += " Peso normale o forma";
       } else if (media >= 25.0 && media <= 29.9) {
           this.lblBmi5.setStyle("-fx-background-color: GreenYellow");
           this.lblBmi6.setStyle("-fx-background-color: GreenYellow");
           messageBmi += " Sovrappeso (Pre-obesità)";
       } else if (media >= 30.0 && media <= 34.9) {
           this.lblBmi7.setStyle("-fx-background-color: Yellow");
           this.lblBmi8.setStyle("-fx-background-color: Yellow");
           messageBmi += " Obesità di 1° classe";
       } else if (media >= 35.0 && media <= 39.9) {
           this.lblBmi9.setStyle("-fx-background-color: Orange");
           this.lblBmi10.setStyle("-fx-background-color: Orange");
           messageBmi += " Obesità di 2° classe";
       } if (media >= 40.0) {
           this.lblBmi11.setStyle("-fx-background-color: #ff6666");
           this.lblBmi12.setStyle("-fx-background-color: #ff6666");
           messageBmi += " Obesità di 3° classe";
       }
       return messageBmi;
   }

    /**
     * Styleclear all label.
     */
    private void styleclear() {
        this.lblBmi1.setStyle("");
        this.lblBmi2.setStyle("");
        this.lblBmi3.setStyle("");
        this.lblBmi4.setStyle("");
        this.lblBmi5.setStyle("");
        this.lblBmi6.setStyle("");
        this.lblBmi7.setStyle("");
        this.lblBmi8.setStyle("");
        this.lblBmi9.setStyle("");
        this.lblBmi10.setStyle("");
        this.lblBmi11.setStyle("");
        this.lblBmi12.setStyle("");
    }

    /**
     * Updates.
     */
    @FXML
    public void Updates() {
        this.nav.getNavParams().set("customer", customer.get());
        this.nav.navigate("page_bmi");
    }

    /**
     * View scheda customer
     */
    @FXML
    public void viewScheda() {
        final Registration customerReg = this.regiRepo.getByIdClient(customer.get().getId()).get(0);
        this.nav.getNavParams().set("customer", customer.get());
        this.nav.getNavParams().set("type", customerReg.getType().getName());
        this.nav.getNavParams().set("sinceDate", customerReg.getSigningDate());
        this.nav.getNavParams().set("duration", customerReg.getDuration());
        this.nav.navigate("page_trainingProgram");

    }
}
