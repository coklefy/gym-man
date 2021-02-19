package gymman.ui.instructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import gymman.customers.Customer;
import gymman.fitness.Category;
import gymman.fitness.Exercise;
import gymman.fitness.Goal;
import gymman.fitness.Metric;
import gymman.fitness.MetricType;
import gymman.fitness.MetricWithRepetitions;
import gymman.fitness.MetricWithTimer;
import gymman.fitness.TrainingProgramRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import gymman.utils.DateUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class to offer the basic functionalities
 * to add, remove or edit a training program.
 * Loads the view components and updates the view.
 *
 */
public abstract class AbstractModifyProgramController implements Controller {
    /** navigation service to make possible the controllers communication. */
    @Getter @Setter private NavigationService nav;
    /** repository of all training programs. */
    @Getter @Setter private TrainingProgramRepository programRepo;

    /** minimum days to consider a training program valid. */
    private static final int MIN_VALIDATION_DAYS = 1;
    /** maximum secods to execute a set. */
    private static final int MAX_SEC_ALLOWED = 300;
    /** minimum secods to execute a set. */
    private static final int MIN_SEC_ALLOWED = 3;
    /** minimum sets for an exercise. */
    private static final int MIN_SETS = 1;
    /** maximun sets for an exercise. */
    private static final int MAX_SETS = 50;
    /** minimum repetitions for  a set. */
    private static final int MAX_REPETITIONS = 50;
    /** minimum repetitions for  a set. */
    private static final int MIN_REPETITIONS = 1;
    /** subscription duration. */
    private int subscriptionDuration = 0;

    /** Empty string to set label style to default. */
    private static final String EMPTY_STRING = "";
    /** Combo was not selected. */
    private static final String NOT_SELECTED = "none";
    /** The metric type selected (with timer). **/
    private static final String WITH_TIMER = "with timer";
    /** The metric type selected (with repetitions). **/
    private static final String WITH_REPETITIONS = "with repetitions";

    /** Local date is the beginning of the subscription */
    @Getter @Setter private LocalDate sinceDate;
    /** Customer that has or will have the training program compiled. **/
    @Getter @Setter private Customer customer;
    /** Exercise list after adding or removing from the instructor interface.  **/
    @Getter private List<Exercise> allExercises = new ArrayList<>();

    /** view Combo boxes. */
    @FXML protected ComboBox<String> goalCombo, categoryCombo, typeCombo, metricCombo;
    /** view labels. */
    @FXML protected Label nameLabel, contactLabel, typeLabel, instructorLabel, durationLabel, secondsLabel, repetitionsLabel, exSizeLabel;
    /** view text fields. */
    @FXML protected TextField exerciseTextField, secondsField, repetitionsField, setsField;
    /** view date pickers. */
    @FXML protected DatePicker fromDateP, toDateP;

    /**
     * Constructor setting navigation service and the training programs repository.
     * @param nav : navigation service
     * @param trainingProgramRepo : training programs repository
     */
    public AbstractModifyProgramController(final NavigationService nav, final TrainingProgramRepository programRepository) {
        this.nav = nav;
        this.programRepo = programRepository;
    }


    /**
     * Method to update the view any time a controller is called.
     * Sets and uploads the view fields to default values.
     */
    protected void updateView() {
        this.setCustomer(Customer.class.cast(this.nav.getNavParams().getWithoutRemoving("customer")));

        sinceDate = LocalDate.class.cast(this.nav.getNavParams().getWithoutRemoving("sinceDate"));
        subscriptionDuration = Integer.class.cast(this.nav.getNavParams().getWithoutRemoving("duration"));

        String type = String.class.cast(this.nav.getNavParams().getWithoutRemoving("type"));


        this.nameLabel.setText(customer.getFirstname() + " " + customer.getLastname());
        this.contactLabel.setText(customer.getTelephoneNumber());
        this.typeLabel.setText(type);

        this.goalCombo.setItems(FXCollections.observableArrayList(Arrays.asList(Goal.values())
                      .stream().map(Goal::toString).collect(Collectors.toList())));

        this.categoryCombo.setItems(FXCollections.observableArrayList(Arrays.asList(Category.values())
                      .stream().map(Category::toString).collect(Collectors.toList())));

        this.typeCombo.setItems(FXCollections.observableArrayList(Arrays.asList(Category.Type.values())
                      .stream().map(Category.Type::toString).collect(Collectors.toList())));

        this.metricCombo.setItems(FXCollections.observableArrayList(Arrays.asList(MetricType.values())
                      .stream().map(MetricType::toString).collect(Collectors.toList())));

        this.fromDateP.setValue(sinceDate);

        this.secondsField.setText(EMPTY_STRING);

        this.repetitionsField.setText(EMPTY_STRING);

        this.setsField.setText(EMPTY_STRING);

        this.durationLabel.setText(EMPTY_STRING);
        this.exSizeLabel.setText("0");
    }

    /**
     * Method to add a new exercise to the list of exercises. It will be
     * added to the list after verifying that the fields have been
     * completed correctly. If user does't click save, it will not be
     * added to the training program.
     */
    @FXML
    public void addExercise() {
        setDefaultStyle();
        if (!areExerciseFieldsSelected()) {
            /* Show alert. View fields not completed correctly */
        } else {
            Metric exerciseMetric;
            if (getSelectedMetric().equals(WITH_TIMER)) {
                exerciseMetric = new MetricWithTimer(
                        Integer.valueOf(this.setsField.getText()),
                        Integer.valueOf(this.secondsField.getText()));
            } else {
                exerciseMetric = new MetricWithRepetitions(
                        Integer.valueOf(this.setsField.getText()),
                        Integer.valueOf(this.repetitionsField.getText()));
            }
            final Exercise exercise = new Exercise.ExerciseBuilder()
                    .name(this.exerciseTextField.getText())
                    .executionMetric(exerciseMetric)
                    .category(getCategory(this.categoryCombo.getValue()))
                    .buildExercise();
            this.allExercises.add(exercise);
            this.exSizeLabel.setText(Integer.toString(this.allExercises.size()));
            resetExerciseFields();
        }
    }

    /**
     * If the user click cancel button, it will turn the view
     * to the instructor interface without saving the edits.
     */
    @FXML
    public void backToInstructorPage() {
        resetAllFields();
        this.nav.navigate("page_instructor");
    }

    /**
     * Method to check if the training program period is correct with... :
     * 1- membership period
     * 2- minimum days required to be valid
     * 3- the start and the finish day are specified correctly
     */
    @FXML
    public void isPeriodValidationCorrect() {
       if (this.fromDateP.getValue() == null || this.toDateP.getValue() == null ||                                                      /* no dates selected */
            DateUtils.daysBetweenLocalDates(getSinceDate(), this.fromDateP.getValue()) < 0 ||                                           /* training program starts before membership */
            DateUtils.daysBetweenLocalDates(this.fromDateP.getValue(), this.toDateP.getValue()) < MIN_VALIDATION_DAYS ||                /* program validation period lower than minimum */
            DateUtils.daysBetweenLocalDates(this.fromDateP.getValue(), this.toDateP.getValue()) > Long.valueOf(subscriptionDuration)) { /* program validation period longer than membership */

            this.highlight(this.fromDateP);
            this.highlight(this.toDateP);
            Alert alert = new Alert(AlertType.WARNING, "Il periodo di validità della scheda non specificato o specificato non corretamente.", ButtonType.OK);
            alert.showAndWait();
        } else {
            this.fromDateP.setStyle(EMPTY_STRING);
            this.toDateP.setStyle(EMPTY_STRING);
            this.durationLabel.setText(DateUtils.daysBetweenLocalDates(this.fromDateP.getValue(), this.toDateP.getValue()) + " giorni");
        }
    }

    /**
     * Method to hide from the user the other metric.
     * For example if user selects metric with timer, metric with
     * repetitions with be hidden.
     */
    @FXML
    public void hideOnSelectedMetric() {
        if (this.metricCombo.getValue() != null && this.metricCombo.getValue().equals(WITH_TIMER)) {
            this.secondsLabel.setVisible(true);
            this.secondsField.setDisable(false);
            this.repetitionsLabel.setVisible(false);
            this.repetitionsField.setDisable(true);
        } else {
            this.secondsLabel.setVisible(false);
            this.secondsField.setDisable(true);
            this.repetitionsLabel.setVisible(true);
            this.repetitionsField.setDisable(false);
        }
    }

    /**
     * Method to add to the set of new exercises to the old list.
     * @param exercises to be added
     */
    protected void addExerciesToList(final Set<Exercise> exercises) {
        this.allExercises.addAll(exercises);
    }

    /**
     * Method to remove to the set of new exercises from the old list.
     * @param exercises to be removed
     */
    protected void removeExercises(final Set<Exercise> exercises) {
        this.allExercises.removeAll(exercises);
    }

    /**
     * Method to return the denomination of the selected metric.
     * User to hide the other metric on user selection.
     * @return a string with the metric denomination
     */
    protected String getSelectedMetric() {
        if (this.metricCombo.getValue() != null) {
            return this.metricCombo.getValue();
        } else {
            return NOT_SELECTED;
        }
    }

    /**
     * Underline the view component that wasn't completed
     * correctly.
     * @param cont : view component
     */
    public void highlight(final Control cont) {
        cont.setStyle("-fx-border-color: red");
    }

    /**
     * Method to control if the user and training program
     * view fields have been completed correctly.
     * @return true if the fields have been completed correctly
     */
    public boolean areInfoFieldsSelected() {
        boolean tmp = true;
        String error = "Alcuni campi non compilati : \n";

        if (this.fromDateP.getValue() == null) {
            this.highlight(this.fromDateP);
            error += "- data inizio \n";
            tmp = false;
        }
        if (this.toDateP.getValue() == null) {
            this.highlight(this.toDateP);
            error += "- data fine \n";
            tmp = false;
        }
        if (this.goalCombo.getValue() == null) {
            this.highlight(this.goalCombo);
            error += "- goal \n";
            tmp = false;
        }
        if (!tmp) {
            Alert alert = new Alert(AlertType.WARNING, error, ButtonType.OK);
            alert.showAndWait();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Method to control if the new exercise
     * view fields have been completed correctly.
     * @return true if the fields have been completed correctly
     */
    public boolean areExerciseFieldsSelected() {
        boolean tmp = true;
        String error = "Alcuni campi non compilati corretamente : \n";

        if (this.exerciseTextField.getText().equals(EMPTY_STRING) || thereIsExercise(getAllExercises(), this.exerciseTextField.getText())) {
            this.highlight(this.exerciseTextField);
            error += " denominazione dell'esercizio \n";
            tmp = false;
        }
        if (this.categoryCombo.getValue() == null) {
            this.highlight(this.categoryCombo);
            error += " categoria \n";
            tmp = false;
        }
        if (this.typeCombo.getValue() == null) {
            this.highlight(this.typeCombo);
            error += " tipo \n";
            tmp = false;
        }
        if (this.metricCombo.getValue() == null) {
            this.highlight(this.metricCombo);
            error += " metrica dell'esecuzione \n";
            tmp = false;
        }

        if (getSelectedMetric().equals(WITH_TIMER) && !isNumeric(this.secondsField.getText())) {
            this.highlight(this.secondsField);
            error += " timer \n";
            tmp = false;
        } else {
            if (getSelectedMetric().equals(WITH_TIMER) && (Integer.valueOf(this.secondsField.getText()) < MIN_SEC_ALLOWED
            	   || Integer.valueOf(this.secondsField.getText()) > MAX_SEC_ALLOWED)) {
                this.highlight(this.secondsField);
                error += " secondi non corretti \n";
                tmp = false;
            }
        }

        if (getSelectedMetric().equals(WITH_REPETITIONS) && !isNumeric(this.repetitionsField.getText())) {
            this.highlight(this.repetitionsField);
            error += " repitizioni \n";
            tmp = false;
        } else {
            if (getSelectedMetric().equals(WITH_REPETITIONS) && (Integer.valueOf(this.repetitionsField.getText()) < MIN_REPETITIONS
            		|| Integer.valueOf(this.repetitionsField.getText()) > MAX_REPETITIONS)) {
                this.highlight(this.repetitionsField);
                error += " repitizioni non corretti \n";
                tmp = false;
            }
        }


        if (!isNumeric(this.setsField.getText())) {
            this.highlight(this.setsField);
            error += " set \n";
            tmp = false;
        } else {
            if (Integer.valueOf(this.setsField.getText()) < MIN_SETS
            		|| Integer.valueOf(this.setsField.getText()) > MAX_SETS) {
                this.highlight(this.setsField);
                error += " sets non corretti \n";
                tmp = false;
            }
        }


        if (!tmp) {
            Alert alert = new Alert(AlertType.WARNING, error, ButtonType.OK);
            alert.showAndWait();
            return false;
        } else {
            return true;
        }

    }

    private boolean thereIsExercise(final List<Exercise> exList, final String name) {
        return exList.stream().anyMatch(ex -> ex.getName().equals(name));
    }

    /**
     * Return the category from the user
     * category box selection.
     * @param category denomination
     * @return {@link Category}
     */
    protected Category getCategory(final String category) {
        return Arrays.asList(Category.values()).stream().filter(c -> c.toString().equals(category)).findFirst().get();
    }

    /**
     * Return the goal from the user
     * goal box selection.
     * @param goal denomination
     * @return  Goal
     */
    protected Goal getGoal(final String goal) {
        return Arrays.asList(Goal.values()).stream().filter(g -> g.toString().equals(goal)).findFirst().get();
    }

    private void setDefaultStyle() {
    	this.nameLabel.setStyle(EMPTY_STRING);
        this.fromDateP.setStyle(EMPTY_STRING);
        this.toDateP.setStyle(EMPTY_STRING);
        this.goalCombo.setStyle(EMPTY_STRING);
        this.categoryCombo.setStyle(EMPTY_STRING);
        this.typeCombo.setStyle(EMPTY_STRING);
        this.metricCombo.setStyle(EMPTY_STRING);
        this.secondsField.setStyle(EMPTY_STRING);
        this.setsField.setStyle(EMPTY_STRING);
        this.repetitionsField.setStyle(EMPTY_STRING);
    }

    private void resetAllFields() {
        /* Info fields */
        this.goalCombo.setValue(null);
        this.fromDateP.getEditor().clear();
        this.toDateP.getEditor().clear();
        resetExerciseFields();
    }

    /**
     * Clean the exercise view fields.
     */
    protected void resetExerciseFields() {
        /* Exercise fields */
        this.exerciseTextField.setText(EMPTY_STRING);
        this.categoryCombo.setValue(null);
        this.metricCombo.setValue(null);
        this.typeCombo.setValue(null);
        this.setsField.setText(EMPTY_STRING);

        this.secondsField.setText(EMPTY_STRING);
        this.secondsField.setDisable(false);
        this.secondsLabel.setVisible(true);

        this.repetitionsField.setText(EMPTY_STRING);
        this.repetitionsField.setDisable(false);
        this.repetitionsLabel.setVisible(true);
    }

    private static boolean isNumeric(final String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

}
