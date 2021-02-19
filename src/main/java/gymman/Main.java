package gymman;

import gymman.auth.AuthService;
import gymman.auth.AuthServiceImpl;
import gymman.auth.RoleRepository;
import gymman.auth.RoleRepositoryImpl;
import gymman.auth.UserRepository;
import gymman.auth.UserRepositoryImpl;
import gymman.customers.AdditionalServiceRepository;
import gymman.customers.AdditionalServiceRepositoryImpl;
import gymman.customers.CustomerRepository;
import gymman.customers.CustomerRepositoryImpl;
import gymman.customers.RegistrationRepository;
import gymman.customers.RegistrationRepositoryImpl;
import gymman.customers.SubscriptionTypeRepository;
import gymman.customers.SubscriptionTypeRepositoryImpl;
import gymman.employees.CalendarService;
import gymman.employees.EmployeeRepository;
import gymman.employees.EmployeeRepositoryImpl;
import gymman.employees.EmployeeService;
import gymman.employees.WorkShiftRepository;
import gymman.employees.WorkShiftRepositoryImpl;
import gymman.fitness.TrainingProgramRepository;
import gymman.fitness.TrainingProgramRepositoryImpl;
import gymman.gymdata.DataProvider;
import gymman.gymdata.ViewProvider;
import gymman.infoclient.BmiRepository;
import gymman.tool.ToolRepository;
import gymman.ui.app.AppController;
import gymman.ui.navigation.NavigationService;
import gymman.ui.navigation.NavigationServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WINDOW_WIDTH_MIN = 1200;
    public static final int WINDOW_HEIGHT_MIN = 800;

    @Override
    public void start(final Stage primaryStage) {
        try {
            // Repositories
            final UserRepository userRepo = new UserRepositoryImpl();
            final RoleRepository roleRepo = new RoleRepositoryImpl();
            final EmployeeRepository employeeRepo = new EmployeeRepositoryImpl();
            final CustomerRepository customerRepo = new CustomerRepositoryImpl();
            final WorkShiftRepository workshiftRepo = new WorkShiftRepositoryImpl();
            final RegistrationRepository registrationRepo = new RegistrationRepositoryImpl();
            final SubscriptionTypeRepository subscriptionTypeRepo = new SubscriptionTypeRepositoryImpl();
            final ToolRepository toolRepo = new ToolRepository();
            final AdditionalServiceRepository serviceRepo = new AdditionalServiceRepositoryImpl();
            final TrainingProgramRepository trainingProgramRepo = new TrainingProgramRepositoryImpl();
            final BmiRepository bmiRepo = new BmiRepository();


		    final NavigationService navService = new NavigationServiceImpl();
			AuthService authService = new AuthServiceImpl(userRepo, roleRepo);
			final EmployeeService employeeService = new EmployeeService(userRepo, employeeRepo, roleRepo);
			final CalendarService calendarService = new CalendarService(workshiftRepo);

            new DataProvider().uploadData(customerRepo, trainingProgramRepo, employeeRepo,subscriptionTypeRepo,
            		                       serviceRepo, registrationRepo);

			ViewProvider.uploadViews(roleRepo, employeeRepo, employeeService, customerRepo, trainingProgramRepo,
					                 registrationRepo, subscriptionTypeRepo, navService, serviceRepo, bmiRepo,
					                 toolRepo, workshiftRepo, calendarService,authService);

            final FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/app/App.fxml"));
			final AppController appController = new AppController(navService, authService);

            loader.setController(appController);

            final Pane root = loader.load();
            final Scene scene = new Scene(root, WINDOW_WIDTH_MIN, WINDOW_HEIGHT_MIN);

            primaryStage.setMinWidth(WINDOW_WIDTH_MIN);
            primaryStage.setMinHeight(WINDOW_HEIGHT_MIN);
            primaryStage.setScene(scene);
            primaryStage.setTitle("GymMan");

            new DataProvider().uploadAuthenticationData(authService, userRepo, roleRepo);

            primaryStage.show();
            appController.onDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
