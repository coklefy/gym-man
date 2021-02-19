package gymman.ui.app;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gymman.auth.AuthService;
import gymman.auth.PermissionImpl;
import gymman.ui.Controller;
import gymman.ui.Page;
import gymman.ui.navigation.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AppController implements Controller {
    private static final Logger logger = LogManager.getLogger("gymman");

    private final NavigationService navService;
    private final AuthService authService;

	@FXML private Label pageTitle;
	@FXML private VBox sideMenu;
    @FXML private Pane content;

    private final List<String> menuEntries = Arrays.asList(
        "page_dashboard",
        "page_employee_list",
        "page_shift_calendar",
        "page_instructor",
        "page_customers_list",
        "page_subscription_list",
        "page_additionalService_list",
        "page_tool_list",
        "page_role_list"
    );

	public AppController(
        final NavigationService navService,
        final AuthService authService
    ) {
        this.navService = navService;
        this.authService = authService;
        this.navService.addOnPageChangeHandler(page -> this.display(page));
        this.generatePagePermissions();
	}

	public void initialize() {
        this.navService.navigate("page_login");
        this.authService.addOnLoginHandler(user -> this.populateMenu());
        this.authService.addOnLogoutHandler(user -> this.clearMenu());
	}

    @Override
    public void onDisplay() {

    }

    @FXML
    public void onBackClicked() {
        this.navService.back();
    }

    private void display(final Page page) {
        logger.debug("Displaying page {}", page.getId());

        this.content.getChildren().clear();
        this.pageTitle.setText(page.getTitle());

        try {
            this.content.getChildren().add(page.getContent());
            page.getController().onDisplay();
        } catch (IOException e) {
            logger.catching(e);

            final Pane errorPane = new StackPane();
            final Label errorLabel = new Label("Couldn't load page");
            errorPane.getChildren().add(errorLabel);
            this.content.getChildren().add(errorPane);
        }
    }

    private void populateMenu() {
        this.sideMenu.getChildren().clear();
        
        this.menuEntries.stream()
            .filter(e -> this.authService.userHasPermission(e))
            .map(e -> this.navService.findById(e))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(page -> this.createMenuButton(page))
            .forEach(btn -> this.sideMenu.getChildren().add(btn));

        Button btnLogout = new Button("Esci");
        btnLogout.getStyleClass().add("menu-button");
        btnLogout.setOnAction(event -> {
            this.authService.logout();
            this.navService.navigate("page_login");
        });
        this.sideMenu.getChildren().add(btnLogout);
    }

    private void clearMenu() {
        this.sideMenu.getChildren().clear();
    }

    private Button createMenuButton(Page page) {
        Button btn = new Button(page.getTitle());
        btn.getStyleClass().add("menu-button");
        btn.setOnAction(event -> this.navService.navigate(page));
        return btn;
    }

    private void generatePagePermissions() {
        this.navService.getPages().stream()
            .map(page -> new PermissionImpl(page.getId(), String.format("Visualizza la pagina '%s'", page.getTitle())))
            .forEach(perm -> this.authService.registerPermission(perm));
    }
}
