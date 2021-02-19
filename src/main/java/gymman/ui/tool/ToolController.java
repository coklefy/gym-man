package gymman.ui.tool;

import java.util.Locale;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.tool.Tool;
import gymman.tool.ToolRepository;
import gymman.ui.ButtonsCellFactory;
import gymman.ui.Controller;
import gymman.ui.navigation.NavigationService;
import javafx.beans.property.ReadOnlyStringWrapper;
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

/**
 * The Class ToolController.
 */
public class ToolController implements Controller {

    /** The btn new tool. */
    @FXML private Button btnNewTool;

    /** The txt search. */
    @FXML private TextField txtSearch;

    /** The lst tools. */
    @FXML private TableView<Tool> lstTools;

    /** The col maintenance. */
    @FXML private TableColumn<Tool, String> colName, colDescription, colSerial, colMaintenance;

    /** The col buttons. */
    @FXML private TableColumn<Tool, Void> colButtons;

    /** The tool repo. */
    private final ToolRepository toolRepo;

    /** The nav. */
    private final NavigationService nav;

    /** The auth service. */
    private final AuthService authService;

    /** The create tool. */
    private final Permission createTool = new PermissionImpl("create_tool", "Creazione Attrezzo");

    /** The edit tool. */
    private final Permission editTool = new PermissionImpl("edit_tool", "Edit Attrezzo");

    /** The delete tool. */
    private final Permission deleteTool = new PermissionImpl("delete_tool", "Delete Attrezzo");

    /** The list tool. */
    private final ObservableList<Tool> listTool = FXCollections.observableArrayList();

    /**
     * Instantiates a new tool controller.
     *
     * @param toolRepo the tool repo
     * @param nav the nav
     * @param authService the auth service
     */
    public ToolController(final ToolRepository toolRepo,final  NavigationService nav,
            final AuthService authService) {
        this.toolRepo = toolRepo;
        this.nav = nav;
        this.authService = authService;
        this.authService.registerPermission(createTool);
        this.authService.registerPermission(editTool);
        this.authService.registerPermission(deleteTool);

    }

    /**
     * Initialize.
     */
    public void initialize() {
        this.colName.setCellValueFactory(
                cellTool -> new ReadOnlyStringWrapper(cellTool.getValue().getName()));
        this.colDescription.setCellValueFactory(
                cellTool -> new ReadOnlyStringWrapper(cellTool.getValue().getDesc()));
        this.colSerial.setCellValueFactory(cellTool -> new ReadOnlyStringWrapper(
                cellTool.getValue().getNumseriale()));
        this.colMaintenance.setCellValueFactory(cellTool -> new ReadOnlyStringWrapper(
                cellTool.getValue().getMaintenance().toString()));

        final ButtonsCellFactory<Tool> factory = new ButtonsCellFactory<>(0,0);
        factory.addButton("Modifica", this.authService.userHasPermission(editTool),
                tool -> {
            this.nav.getNavParams().set("tool", tool);
            this.nav.navigate("page_tool_edit");
        });
        factory.addButton("Elimina", this.authService.userHasPermission(deleteTool),
                tool -> {
            final Alert alert = new Alert(AlertType.CONFIRMATION, "Sicuro di Cancellare?", ButtonType.YES,ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult()==ButtonType.YES) {
            toolRepo.remove(tool);
            lstTools.getItems().remove(tool);
            }
        });
        this.colButtons.setCellFactory(factory.getFactory());

        this.lstTools.setItems(this.listTool);
    }

    @Override
    public void onDisplay() {
        this.listTool.setAll(this.toolRepo.getAll());
        if(!this.authService.userHasPermission(createTool)) {
            this.btnNewTool.setDisable(true);
            }
    }

    /**
     * On search text changed.
     */
    @FXML
    public void onSearchTextChanged() {
        if (this.txtSearch.getText().toLowerCase(Locale.getDefault()).trim().isEmpty()) {
            this.listTool.setAll(this.toolRepo.getAll());
            return;
        }
        this.listTool.setAll(this.toolRepo.searchByName(this.txtSearch.getText().toLowerCase(Locale.getDefault())));
    }

    /**
     * On new tools clicked.
     */
    @FXML
    public void onNewToolsClicked() {
        this.nav.navigate("page_tool_edit");
    }

}
