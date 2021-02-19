package gymman.ui.instructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import gymman.auth.AuthService;
import gymman.auth.NotLoggedInException;
import gymman.common.DuplicateEntityException;
import gymman.employees.Employee;
import gymman.employees.EmployeeRepository;
import gymman.utils.DateUtils;
import gymman.fitness.Exercise;
import gymman.fitness.TrainingProgram;
import gymman.fitness.TrainingProgramRepository;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.navigation.NavigationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Class used to edit anTrainingProgram.
 */
public class EditTrainingProgramController extends AbstractModifyProgramController {

	/** Authentication service */
    private final AuthService authService;
    /** Employees repository. */
    private final EmployeeRepository employeeRepo;

    /** Training program to be edited. **/
    @Getter @Setter private TrainingProgram  trainingProgram;

    /** Exercises deleted from instructor interface. **/
    @Getter private Set<Exercise> deletedEx = new HashSet<>();

    /** Exercises list table. */
    @FXML private TableView<Exercise> exercisesTable;
    /** Exercise denomination column. */
    @FXML private TableColumn<Exercise, String> nameCol = new TableColumn<>("Denominazine");
    /** Exercise category column. */
    @FXML private TableColumn<Exercise, String> categoryCol = new TableColumn<>("Categoria");
    /** Exercise execution metric column. */
    @FXML private TableColumn<Exercise, String> metricCol = new TableColumn<>("Metrica");
    /** Delete button column. */
    @FXML private TableColumn<Exercise, Void> deleteCol = new TableColumn<>("");
    /** Button to save the edits made to the training program. **/
    @FXML private Button saveButton;


    /**
     * Constructor of the edit page controller.
     * @param nav : navigation service to make possible
     *              the controllers communication
     * @param trainingProgramRepo : all training programs repository
     * @param auth : authentication service to controller the permissions
     */
    public EditTrainingProgramController(
            final NavigationService nav,
            final TrainingProgramRepository trainingProgramRepo,
            final EmployeeRepository employees,
            final AuthService auth
    ) {
        super(nav, trainingProgramRepo);
        this.authService = auth;
        this.employeeRepo = employees;
    }

    /**
     * Initialize the view of editing page.
     * @throws NotLoggedInException if the user isn't logged in
     */
    @FXML
    public void initialize() throws NotLoggedInException {

        saveButton.setVisible(false);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        metricCol.setCellValueFactory(new PropertyValueFactory<>("executionMetric"));

        ButtonsCellFactory<Exercise> buttonsFactory = new ButtonsCellFactory<>(0, 0);

        buttonsFactory.addButton("Delete", ex -> {
            saveButton.setVisible(true);
            deletedEx.add(ex);
            this.exSizeLabel.setText(String.valueOf(getAllExercises().size() - getDeletedEx().size()));
            exercisesTable.getItems().remove(ex);
        });

        this.deleteCol.setCellFactory(buttonsFactory.getFactory());

    }

    /**
     * Method to update the table of the exercises with the new exercise.
     */
    @FXML
    public void addExerciseToTable() {
        saveButton.setVisible(true);
        this.addExercise();
        this.exercisesTable.setItems(FXCollections.observableArrayList(getAllExercises()));
    }

    private ObservableList<Exercise> getExercises(final List<Exercise> exList) {
           return FXCollections.observableArrayList(exList);
    }



    @Override
    public final void onDisplay() {
        this.deletedEx = new HashSet<>();

        this.instructorLabel.setText(getTutor().getFirstname() + " " + getTutor().getLastname());
        updateView();
        if (this.getCustomer() != null) {
            this.setTrainingProgram(this.getProgramRepo().getByUsername(getCustomer().getUsername()).get());
        } else {
            this.getNav().navigate("page_instructor");
        }

        fromDateP.setValue(DateUtils.convertToLocalDateViaInstant(trainingProgram.getValidFrom()));
        toDateP.setValue(DateUtils.convertToLocalDateViaInstant(trainingProgram.getValidTo()));
        durationLabel.setText(String.valueOf(DateUtils.daysBetweenDates(trainingProgram.getValidFrom(), trainingProgram.getValidTo())) + " giorni");
        goalCombo.setValue(trainingProgram.getGoal().toString());

        if (this.getAllExercises().size() == 0) {
            addExerciesToList(trainingProgram.getExercises());
        }
        this.exercisesTable.setItems(getExercises(getAllExercises()));
        this.exSizeLabel.setText(String.valueOf(getAllExercises().size()));



    }

    /**
     * Method to save the edited training program.
     * @throws DuplicateEntityException if trying to add a new training
     * program that already existed.
     *
     */
    public void saveEditedTrainingProgram() throws DuplicateEntityException {
        this.getProgramRepo().remove(trainingProgram);

        removeExercises(deletedEx);
        saveButton.setVisible(true);

        if (!areInfoFieldsSelected()) {

        	// Information fields not completed correctly.

        } else {
        	final TrainingProgram program = new TrainingProgram.TrainingProgramBuilder()
                        .tutor(trainingProgram.getTutor())
                        .customer(getCustomer())
                        .goal(this.getGoal(this.goalCombo.getValue().toString()))
                        .periodValidationFrom(DateUtils.convertToDateViaInstant(this.fromDateP.getValue()))
                        .periodValidationTo(DateUtils.convertToDateViaInstant(this.toDateP.getValue()))
                        .buildTrainingProgramPlan();

                getAllExercises().forEach((exercise) -> program.addNewExercise(exercise));
                getAllExercises().removeAll(getAllExercises());
                this.getProgramRepo().add(program);

                resetExerciseFields();

        }

        resetExerciseFields();
        this.getNav().navigate("page_instructor");
    }

    private Employee getTutor() {
        return this.employeeRepo.getByUsername(this.authService.getLoggedInUser().getUsername()).get();
    }
}
