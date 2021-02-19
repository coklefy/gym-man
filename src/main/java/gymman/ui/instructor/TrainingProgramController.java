package gymman.ui.instructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.customers.Customer;
import gymman.fitness.Exercise;
import gymman.fitness.TrainingProgram;
import gymman.fitness.TrainingProgramRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import gymman.utils.DateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 * Class to create the training program interface controller.
 * It will get the data from the instructor interface and will
 * upload them in the view.
 *
 */
public class TrainingProgramController implements Controller {
    /** navigation service. */
    private final NavigationService nav;
    /** repository of all the training programs. */
    private final TrainingProgramRepository programsRepo;
    /** service of authentication. */
    private final AuthService authService;

    /** View Labels. **/
    @FXML private Label customerName, contactLabel, cfLabel, typeLabel, durationLabel, goalLabel, validFromLabel, validToLabel, instructorLabel;
    /** training program container. **/
    @FXML private AnchorPane programAnchorPane;
    /** exercise container. **/
    @FXML private FlowPane exContainer;
    /**  icon that if clicked, changes the interface to Edit Training Program . */
    @FXML private ImageView editIcon;
    /** alert window. */
    private final Alert noProgramAlert  = new Alert(AlertType.WARNING,
    												"Questo cliente non ha una scheda d'allenamento.\n Vorresti compilare una nuova ?!",
                                                     ButtonType.YES, ButtonType.NO);

    /** permission to compile a new training program. **/
    private final Permission compileProgram = new PermissionImpl("compile_program", "Compile una nuova scheda d'allenamento");
    /** permission to edit an existing training program. **/
    private final Permission editProgram = new PermissionImpl("editProgram", "Modifica una scheda d'allenamento");

    private boolean canEdit = false;

    /**
     * Constructor to create the TrainingProgram Controller.
     * @param nav : the navigation service passed to all controllers
     * @param programsRepo : the repository of all training programs
     *@param auth : authentication service
     */
    public TrainingProgramController(
            final NavigationService nav,
            final TrainingProgramRepository programsRepo,
            final AuthService auth
    ) {
        this.nav = nav;
        this.programsRepo = programsRepo;
        this.authService = auth;
        this.authService.registerPermission(compileProgram);
        this.authService.registerPermission(editProgram);
    }


    @Override
    public final void onDisplay() {

        /* Check if user has the permission to edit the training program.
         * If he hasn't the permission, hide edit icon.
         */
        if (this.authService.userHasPermission(editProgram)) {
            this.editIcon.setVisible(true);
        } else {
            this.editIcon.setVisible(false);
        }

        uploadCustomerDataToView();
    }

    /**
     *  Method to pass to the editing interface page.
     */
    @FXML
    public void editTrainingProgram() {
    	if(canTutorEdit()) {
            this.nav.navigate("editTrainingProgram");
    	}else {
    		final Alert alert = new Alert(AlertType.INFORMATION, "Non hai i permessi per modificare questa scheda d'allenamento", ButtonType.OK);
            alert.showAndWait();
    	}
    }

    private void uploadCustomerDataToView() {

        final Object customerObj = this.nav.getNavParams().getWithoutRemoving("customer");
        final Object durationObj = this.nav.getNavParams().getWithoutRemoving("duration");
        final Object typeObj = this.nav.getNavParams().getWithoutRemoving("type");


        if (Customer.class.isInstance(customerObj)) {
            final Customer customer = Customer.class.cast(customerObj);
            final String type = String.class.cast(typeObj);
            final Integer duration = Integer.class.cast(durationObj);

            /* check if the customer has a training program
             * upload the data or ask the instructor to compile a new one */
            if (!hasTrainingProgram(customer.getUsername())) {
                /* Check if user has the permission to compile a new training
                 * customer program.
                 * If user hasn't the permission, turn back to instructor interface.
                 */
                if (this.authService.userHasPermission(compileProgram)) {
                    askToCompileProgram();
                } else {
                    this.nav.navigate("page_instructor");
                }
            } else {
                 /* Customer has the training program. Going to upload all the data to the view */
                final TrainingProgram program = getTrainingProgramPlanOf(customer.getUsername()).get();
                final String goal = program.getGoal().toString();
                final String validFrom = DateUtils.parseDateToString(program.getValidFrom());
                final String validTo = DateUtils.parseDateToString(program.getValidTo());
                final String programDuration = String.valueOf(DateUtils.daysBetweenDates(program.getValidFrom(), program.getValidTo()));
                final String tutor = program.getTutor().getFirstname() + " " + program.getTutor().getLastname();

                this.customerName.setText(customer.getFirstname() + " " + customer.getLastname());
                this.contactLabel.setText(customer.getTelephoneNumber());
                this.cfLabel.setText(customer.getFiscalCode());
                this.typeLabel.setText(type);
                this.durationLabel.setText(programDuration + " giorni");
                this.goalLabel.setText(goal);
                this.validFromLabel.setText(validFrom);
                this.validToLabel.setText(validTo);
                this.instructorLabel.setText(tutor);

                /* Controll if the user loged in as Employee has the permission to
                 * modifiy this training program. */
                if(program.getTutor().getUsername() == authService.getLoggedInUser().getUsername()) {
                	this.canEdit = true;
                }else {
                	this.canEdit = false;
                }

            }

            /* Clean the exercise container if there is any exercise,
             * and upload the new ones.
             * */
            if (this.exContainer.getChildren().size() > 0) {
                this.exContainer.getChildren().removeAll(this.exContainer.getChildren());
            }
            getExercisesOf(customer.getUsername()).stream().forEach(exercise -> uploadExercise(exercise));
        }

    }

    private void askToCompileProgram() {
        final Optional<ButtonType> result = this.noProgramAlert.showAndWait();
        if (result.get() == ButtonType.YES) {
            compileTrainingProgram();
        } else {
            this.nav.getNavParams().getWithoutRemoving("customer");
            this.nav.getNavParams().getWithoutRemoving("type");
            this.nav.getNavParams().getWithoutRemoving("sinceDate");
            this.nav.getNavParams().getWithoutRemoving("duration");
            this.nav.back();
        }
    }

    private void compileTrainingProgram() {
        this.nav.navigate("page_compileTrainingProgram");
    }

    private void uploadExercise(final Exercise exercise) {
        final ExerciseController exerciseCtrl = new ExerciseController();
        this.exContainer.getChildren().add(exerciseCtrl.loadExercises(exercise));
    }

    private boolean hasTrainingProgram(final String username) {
        return this.programsRepo.getByUsername(username).isPresent();
    }

    private Optional<TrainingProgram> getTrainingProgramPlanOf(final String username) {
        return this.programsRepo.getByUsername(username);
    }

    private Set<Exercise> getExercisesOf(final String username) {
         if (hasTrainingProgram(username)) {
             return this.programsRepo.getByUsername(username).get().getExercises();
        } else {
            return new HashSet<>();
        }
    }

    private boolean canTutorEdit() {
    	return this.canEdit;
    }

}
