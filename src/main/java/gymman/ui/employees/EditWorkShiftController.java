package gymman.ui.employees;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import gymman.auth.AuthService;
import gymman.common.DuplicateEntityException;
import gymman.employees.Employee;
import gymman.employees.EmployeeRepository;
import gymman.employees.WorkShift;
import gymman.employees.WorkShiftRepository;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditWorkShiftController implements Controller {
    private final AuthService authService;
    private final NavigationService nav;
    private final WorkShiftRepository shiftRepo;
    private final EmployeeRepository employeeRepo;
    private Optional<WorkShift> shift = Optional.empty();

    @FXML private TextField txtSearchEmployee;
    @FXML private ListView<Employee> lstEmployee;
    @FXML private CheckBox chkMonday;
    @FXML private CheckBox chkTuesday;
    @FXML private CheckBox chkWednesday;
    @FXML private CheckBox chkThursday;
    @FXML private CheckBox chkFriday;
    @FXML private CheckBox chkSaturday;
    @FXML private CheckBox chkSunday;
    @FXML private RadioButton radRecurring;
    @FXML private RadioButton radOneTime;
    @FXML private DatePicker dateOneTime;
    @FXML private TextField txtTimeStart;
    @FXML private TextField txtTimeEnd;
    @FXML private TextArea txtComment;
    @FXML private Button btnSave;

    private Map<DayOfWeek, CheckBox> days = new HashMap<>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public EditWorkShiftController(
        AuthService auth,
        NavigationService nav,
        WorkShiftRepository shiftRepo,
        EmployeeRepository employeeRepo
    ) {
        this.authService = auth;
        this.nav = nav;
        this.shiftRepo = shiftRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public void onDisplay() {
        this.clearForm();

        final Object shiftObj = this.nav.getNavParams().get("workshift");
        if (shiftObj != null && WorkShift.class.isInstance(shiftObj)) {
            final WorkShift shift = WorkShift.class.cast(shiftObj);
            this.shift = Optional.of(shift);

            Employee employee = this.employeeRepo.get(shift.getEmployeeId()).get();

            this.lstEmployee.getItems().add(employee);
            this.lstEmployee.getSelectionModel().select(employee);

            this.radRecurring.setSelected(shift.isRecurring());
            this.radOneTime.setSelected(!shift.isRecurring());
            
            this.txtTimeStart.setText(timeFormatter.format(shift.getTimeStart()));
            this.txtTimeEnd.setText(timeFormatter.format(shift.getTimeEnd()));
            
            this.txtComment.setText(shift.getComment());

            if (!shift.isRecurring()) {
                this.dateOneTime.setValue(shift.getDate().get());
            } else {
                this.setSelectedDays(shift.getWeekDays());
            }
            
        }
    }

    @FXML
    public void initialize() {
        this.days.put(DayOfWeek.MONDAY, chkMonday);
        this.days.put(DayOfWeek.TUESDAY, chkTuesday);
        this.days.put(DayOfWeek.WEDNESDAY, chkWednesday);
        this.days.put(DayOfWeek.THURSDAY, chkThursday);
        this.days.put(DayOfWeek.FRIDAY, chkFriday);
        this.days.put(DayOfWeek.SATURDAY, chkSaturday);
        this.days.put(DayOfWeek.SUNDAY, chkSunday);

        this.lstEmployee.setCellFactory(listView -> new ListCell<Employee>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(String.format("%s %s (%s)", item.getFirstname(),
                        item.getLastname(), dateFormatter.format(item.getBirthdate())));
            }
        });
    }

    @FXML
    public void onSaveClicked() {
        WorkShift.WorkShiftBuilder workShiftBuilder = WorkShift.builder()
                .employeeId(this.lstEmployee.getSelectionModel().getSelectedItem().getId())
                .comment(this.txtComment.getText());
        
        if (this.lstEmployee.getSelectionModel().isEmpty()) {
            new Alert(AlertType.WARNING, "E' necessario selezionare un impiegato").showAndWait();
        }

        if (this.radRecurring.isSelected()) {
            workShiftBuilder.weekDays(this.getSelectedDays());
        } else if (this.radOneTime.isSelected()) {
            workShiftBuilder.date(this.dateOneTime.getValue());
        }

        try {
            workShiftBuilder.timeStart(LocalTime.from(timeFormatter.parse(this.txtTimeStart.getText())));
        } catch (DateTimeParseException e) {
            new Alert(AlertType.WARNING, "L'orario di inizio è errato").showAndWait();
        }

        try {
            workShiftBuilder.timeEnd(LocalTime.from(timeFormatter.parse(this.txtTimeEnd.getText())));
        } catch (DateTimeParseException e) {
            new Alert(AlertType.WARNING, "L'orario di fine è errato").showAndWait();
        }

        if (this.shift.isPresent()) {
            workShiftBuilder.id(this.shift.get().getId());
        }

        try {
            this.shiftRepo.add(workShiftBuilder.build());
            this.nav.backOr("page_shift_calendar");
        } catch (DuplicateEntityException e) {
            
        }
        
    }

    public void onFormInputChange() {
        boolean filledOut = lstEmployee.getSelectionModel().getSelectedItem() != null
                && !isEmpty(txtTimeStart)
                && !isEmpty(txtTimeEnd);
        
        if (this.radOneTime.isSelected()) {
            filledOut = filledOut && dateOneTime.getValue() != null;
        }

        if (this.radRecurring.isSelected()) {
            filledOut = filledOut && !this.getSelectedDays().isEmpty();
        }

        this.btnSave.setDisable(!filledOut);
    }

    private void clearForm() {
        txtSearchEmployee.clear();
        lstEmployee.getItems().clear();
        radRecurring.setSelected(true);
        radOneTime.setSelected(false);
        dateOneTime.getEditor().clear();
        txtTimeStart.clear();
        txtTimeEnd.clear();
        txtComment.clear();

        this.days.values().stream()
                .forEach(e -> e.setSelected(false));
        
        this.btnSave.setDisable(true);
    }

    public void onSearchTextChanged() {
        String query = this.txtSearchEmployee.getText();
        query = query.trim();

        if (query.isEmpty()) {
            this.lstEmployee.getItems().clear();
            return;
        }

        List<Employee> result = this.employeeRepo.searchByName(query);
        this.lstEmployee.getItems().setAll(result);
    }

    private List<DayOfWeek> getSelectedDays() {
        return this.days.entrySet().stream()
                .filter(e -> e.getValue().isSelected())
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }

    private void setSelectedDays(Set<DayOfWeek> days) {
        this.days.entrySet().stream()
                .filter(e -> days.contains(e.getKey()))
                .map(e -> e.getValue())
                .forEach(e -> e.setSelected(true));
    }

    private static boolean isEmpty(TextField field) {
        return field.getText().trim().isEmpty();
    }
}
