package gymman.gymdata;

import gymman.auth.AuthService;
import gymman.auth.RoleRepository;
import gymman.customers.AdditionalServiceRepository;
import gymman.customers.CustomerRepository;
import gymman.customers.RegistrationRepository;
import gymman.customers.SubscriptionTypeRepository;
import gymman.employees.CalendarService;
import gymman.employees.EmployeeRepository;
import gymman.employees.EmployeeService;
import gymman.employees.WorkShiftRepository;
import gymman.fitness.TrainingProgramRepository;
import gymman.infoclient.BmiRepository;
import gymman.tool.ToolRepository;
import gymman.ui.Controller;
import gymman.ui.Page;
import gymman.ui.customers.AddCustomerPage;
import gymman.ui.customers.AddRegistrationPage;
import gymman.ui.customers.AddServicePage;
import gymman.ui.customers.AddSubscriptionPage;
import gymman.ui.customers.AdditionalServicePage;
import gymman.ui.customers.CreateCustomerController;
import gymman.ui.customers.CreateRegistrationController;
import gymman.ui.customers.CreateServiceController;
import gymman.ui.customers.CreateSubscriptionController;
import gymman.ui.customers.CustomerController;
import gymman.ui.customers.CustomerPage;
import gymman.ui.customers.RegistrationController;
import gymman.ui.customers.RegistrationPage;
import gymman.ui.customers.ServiceController;
import gymman.ui.customers.SubscriptionController;
import gymman.ui.customers.SubscriptionPage;
import gymman.ui.dashboard.DashboardController;
import gymman.ui.dashboard.DashboardPage;
import gymman.ui.employees.*;
import gymman.ui.employees.EditEmployeePage;
import gymman.ui.employees.EditWorkShiftController;
import gymman.ui.employees.EditWorkShiftPage;
import gymman.ui.employees.EmployeesController;
import gymman.ui.employees.EmployeesPage;
import gymman.ui.employees.ShiftCalendarController;
import gymman.ui.infoclient.BMIController;
import gymman.ui.infoclient.BMIPage;
import gymman.ui.infoclient.InfoClientController;
import gymman.ui.infoclient.InfoClientPage;
import gymman.ui.infoclient.LoginClientController;
import gymman.ui.infoclient.LoginClientPage;
import gymman.ui.instructor.CompileTrainingProgramController;
import gymman.ui.instructor.CompileTrainingProgramPage;
import gymman.ui.instructor.EditTrainingProgramController;
import gymman.ui.instructor.EditTrainingProgramPage;
import gymman.ui.instructor.InstructorController;
import gymman.ui.instructor.InstructorPage;
import gymman.ui.instructor.TrainingProgramController;
import gymman.ui.instructor.TrainingProgramPage;
import gymman.ui.login.LoginController;
import gymman.ui.login.LoginPage;
import gymman.ui.navigation.NavigationService;
import gymman.ui.roles.NewRoleController;
import gymman.ui.roles.NewRolePage;
import gymman.ui.roles.RolesController;
import gymman.ui.roles.RolesPage;
import gymman.ui.tool.NewToolController;
import gymman.ui.tool.NewToolPage;
import gymman.ui.tool.ToolController;
import gymman.ui.tool.ToolPage;

public final class ViewProvider {

	private ViewProvider() {

	}

	public static void uploadViews(RoleRepository roleRepo, EmployeeRepository employeeRepo,
			EmployeeService employeeService, CustomerRepository customerRepo,
			TrainingProgramRepository trainingProgramRepo, RegistrationRepository registrationRepo,
			SubscriptionTypeRepository subscriptionTypeRepo, NavigationService navService,
			AdditionalServiceRepository serviceRepo, BmiRepository bmiRepo, ToolRepository toolRepo,
			WorkShiftRepository workshiftRepo, CalendarService calendarService,
			AuthService authService) {
		// Login
		final Page login = new LoginPage();
		final Controller loginCtrl = new LoginController(authService, navService);
		login.setController(loginCtrl);
		navService.registerPage(login);

		// Dashboard
		final Page dashboard = new DashboardPage();
		final Controller dashboardCtrl = new DashboardController(authService, employeeRepo);
		dashboard.setController(dashboardCtrl);
		navService.registerPage(dashboard);

		// Roles
		final Page roles = new RolesPage();
		final Controller rolesCtrl = new RolesController(roleRepo, navService, authService);
		roles.setController(rolesCtrl);
		navService.registerPage(roles);

		// New Role
		final Page newRole = new NewRolePage();
		final Controller newRoleCtrl = new NewRoleController(authService, roleRepo, navService);
		newRole.setController(newRoleCtrl);
		navService.registerPage(newRole);

		// Employees
		final Page employees = new EmployeesPage();
		final Controller employeesCtrl = new EmployeesController(employeeRepo, navService, authService);
		employees.setController(employeesCtrl);
		navService.registerPage(employees);

		// Edit employee
		final Page editEmployee = new EditEmployeePage();
		final Controller editEmployeeCtrl = new EditEmployeeController(authService, employeeService, navService,
				roleRepo);
		editEmployee.setController(editEmployeeCtrl);
		navService.registerPage(editEmployee);

	    // Shifts Calendar
		final Page shiftCalendar = new ShiftCalendarPage();
		final Controller shiftCalendarCtrl = new ShiftCalendarController(calendarService, navService, employeeRepo);
		shiftCalendar.setController(shiftCalendarCtrl);
		navService.registerPage(shiftCalendar);

		 // Edit work shift
		 final Page editWorkShift = new EditWorkShiftPage();
		 final Controller editWorkShiftCtrl = new EditWorkShiftController(authService, navService, workshiftRepo, employeeRepo);
		 editWorkShift.setController(editWorkShiftCtrl);
		 navService.registerPage(editWorkShift);

		// Customer
		final Page customer = new CustomerPage();
		final Controller customerCtrl = new CustomerController(customerRepo, navService, authService, registrationRepo);
		customer.setController(customerCtrl);
		navService.registerPage(customer);

		// New Customer
		final Page addCustomer = new AddCustomerPage();
		final Controller createCustomerCtrl = new CreateCustomerController(customerRepo, navService);
		addCustomer.setController(createCustomerCtrl);
		navService.registerPage(addCustomer);

		// Registration
		final Page registration = new RegistrationPage();
		final Controller registrationCtrl = new RegistrationController(registrationRepo, navService, authService);
		registration.setController(registrationCtrl);
		navService.registerPage(registration);

		// New Registration
		final Page addRegistration = new AddRegistrationPage();
		final Controller createRegistrationCtrl = new CreateRegistrationController(customerRepo, subscriptionTypeRepo,
				registrationRepo, serviceRepo, navService);
		addRegistration.setController(createRegistrationCtrl);
		navService.registerPage(addRegistration);

		// Subscription
		final Page subscription = new SubscriptionPage();
		final Controller subscriptionCtrl = new SubscriptionController(subscriptionTypeRepo, navService, authService);
		subscription.setController(subscriptionCtrl);
		navService.registerPage(subscription);

		// New Subscription
		final Page addSubscription = new AddSubscriptionPage();
		final Controller createSubscriptionCtrl = new CreateSubscriptionController(subscriptionTypeRepo, navService);
		addSubscription.setController(createSubscriptionCtrl);
		navService.registerPage(addSubscription);

		// AdditionalService
		final Page additionalService = new AdditionalServicePage();
		final Controller addServiceCtrl = new ServiceController(serviceRepo, navService, authService);
		additionalService.setController(addServiceCtrl);
		navService.registerPage(additionalService);

		// New AdditionalService
		final Page addService = new AddServicePage();
		final Controller createServiceCtrl = new CreateServiceController(serviceRepo, navService);
		addService.setController(createServiceCtrl);
		navService.registerPage(addService);

		// Instructor form
		final Page instructor = new InstructorPage();
		final Controller instructorCtrl = new InstructorController(navService, customerRepo, registrationRepo, authService);
		instructor.setController(instructorCtrl);
		navService.registerPage(instructor);

		// TrainingProgram form
		final Page trainingProgram = new TrainingProgramPage();
		final Controller trainingProgramCtrl = new TrainingProgramController(navService, trainingProgramRepo, authService);
		trainingProgram.setController(trainingProgramCtrl);
		navService.registerPage(trainingProgram);

		// Tool
		final Page tool = new NewToolPage();
		final Controller toolCtrl = new NewToolController(toolRepo, navService);
		tool.setController(toolCtrl);
		navService.registerPage(tool);

		// Compile TrainingProgram form
		final Page compileTrainingProgram = new CompileTrainingProgramPage();
		final Controller compileTrainingCtrl = new CompileTrainingProgramController(navService, trainingProgramRepo, authService, employeeRepo);
		compileTrainingProgram.setController(compileTrainingCtrl);
		navService.registerPage(compileTrainingProgram);

		// Edit TrainingProgram form
		final Page editTrainingProgram = new EditTrainingProgramPage();
		final Controller editTrainingCtrl = new EditTrainingProgramController(navService, trainingProgramRepo, employeeRepo, authService);
		editTrainingProgram.setController(editTrainingCtrl);
		navService.registerPage(editTrainingProgram);

		// SearchTools
		final Page searchtool = new ToolPage();
		final Controller searchtoolCtrl = new ToolController(toolRepo, navService, authService);
		searchtool.setController(searchtoolCtrl);
		navService.registerPage(searchtool);

		// InfoClient
		final Page infoclient = new InfoClientPage();
		final Controller infoclientCtrl = new InfoClientController(serviceRepo, registrationRepo, bmiRepo, navService);
		infoclient.setController(infoclientCtrl);
		navService.registerPage(infoclient);

		// InfoClient
		final Page loginClient = new LoginClientPage();
		final Controller loginClientCtrl = new LoginClientController(customerRepo, navService);
		loginClient.setController(loginClientCtrl);
		navService.registerPage(loginClient);

		// BMIClient
		final Page BMIClient = new BMIPage();
		final Controller BMICtrl = new BMIController(bmiRepo, navService);
		BMIClient.setController(BMICtrl);
		navService.registerPage(BMIClient);


	}
}
