package gymman.gymdata;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gymman.auth.AuthService;
import gymman.auth.Permission;
import gymman.auth.PermissionImpl;
import gymman.auth.Role;
import gymman.auth.RoleRepository;
import gymman.auth.User;
import gymman.auth.UserRepository;
import gymman.common.DuplicateEntityException;
import gymman.common.Repository;
import gymman.customers.AdditionalService;
import gymman.customers.Customer;
import gymman.customers.Customer.Gender;
import gymman.customers.InvalidValueException;
import gymman.customers.Registration;
import gymman.customers.SubscriptionType;
import gymman.employees.Employee;
import gymman.fitness.Category;
import gymman.fitness.Exercise;
import gymman.fitness.Goal;
import gymman.fitness.MetricWithRepetitions;
import gymman.fitness.MetricWithTimer;
import gymman.fitness.TrainingProgram;
import gymman.utils.DateUtils;

/**
 *
 * DataProvider is an class used to load data into software
 * without bloating main method.
 *
 */

public final class DataProvider {

	private final  Map<String, Customer> customerMap = new HashMap<>();
	private final  Map<String, Employee> employeeMap = new HashMap<>();


	public void uploadData(Repository<Customer> customers, Repository<TrainingProgram> trainingPrograms,
			Repository<Employee> employees, Repository<SubscriptionType> subscriptions, Repository<AdditionalService> serviceRepo,
			Repository<Registration> registrations) throws InvalidValueException, ParseException {
		provideEmployeeData(employees);
		provideCustomerData(customers);
		provideTraingProgramData(trainingPrograms);
		provideAdditionalServiceData(serviceRepo);
		provideSubscriptionData(subscriptions, registrations);
	}

	private void provideSubscriptionData(Repository<SubscriptionType> subscriptionRepo, Repository<Registration> registrationRepo) throws DuplicateEntityException, InvalidValueException {

		SubscriptionType salaPesi = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(10).build();
        SubscriptionType calisthenics = SubscriptionType.builder().name("calisthenics").description("calisthenics").unitPrice(20).build();
        SubscriptionType corpoLibero = SubscriptionType.builder().name("corpo libero").description("corpo libero").unitPrice(5).build();

        subscriptionRepo.add(salaPesi);
        subscriptionRepo.add(calisthenics);
        subscriptionRepo.add(corpoLibero);

        registrationRepo.add(Registration.builder()
                .idClient("matteo")
                .type(salaPesi)
                .signingDate(LocalDate.of(2019, 11, 15))
                .duration(30)
                .discount(10)
                .additionalService(null)
                .build());

        registrationRepo.add(Registration.builder()
                .idClient("daniele")
                .type(calisthenics)
                .signingDate(LocalDate.of(2019, 11, 15))
                .duration(60)
                .discount(20)
                .additionalService(null)
                .build());

        registrationRepo.add(Registration.builder()
                .idClient("mario")
                .type(corpoLibero)
                .signingDate(LocalDate.of(2019, 11, 15))
                .duration(45)
                .discount(15)
                .additionalService(null)
                .build());

        registrationRepo.add(Registration.builder()
                .idClient("sokol")
                .type(corpoLibero)
                .signingDate(LocalDate.of(2019, 11, 15))
                .duration(45)
                .discount(15)
                .additionalService(null)
                .build());

        registrationRepo.add(Registration.builder()
                .idClient("alberto")
                .type(corpoLibero)
                .signingDate(LocalDate.of(2019, 11, 15))
                .duration(45)
                .discount(15)
                .additionalService(null)
                .build());

	}

	private void provideCustomerData(Repository<Customer> customerRepo) throws InvalidValueException{
		final Customer matteo = Customer.builder()
                .firstname("Matteo")
                .lastname("Manzi")
                .fiscalCode("MNZMTT95E22C573O")
                .gender(Gender.M)
                .birthdate(LocalDate.of(1995, 05, 22))
                .email("matteo@gmail.com")
                .telephoneNumber("3317426961")
                .username("matteo").build();
        customerMap.put("matteo", matteo);

		final Customer daniele = Customer.builder()
                .firstname("daniele")
                .lastname("Manfredonia")
                .gender(Gender.M)
                .fiscalCode("MNZMTT84E22C573O")
                .birthdate(LocalDate.of(1995, 05, 22))
                .email("daniele@gmail.com")
                .telephoneNumber("3355526966")
                .username("daniele").build();
		customerMap.put("daniele", daniele);

		final Customer mario = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mario")
                .gender(Gender.M)
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 1, 1))
                .email("mario@gmail.com")
                .telephoneNumber("3312468247").build();
		customerMap.put("mario", mario);

		final Customer sokol = Customer.builder()
                .firstname("Sokol")
                .lastname("Guri")
                .username("sokol")
                .gender(Gender.M)
                .fiscalCode("SSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 1, 1))
                .email("sokol@gmail.com")
                .telephoneNumber("3812468247").build();
		customerMap.put("sokol", sokol);


		final Customer alberto = Customer.builder()
                .firstname("Alberto")
                .lastname("Mori")
                .fiscalCode("ALZMTT95E22C573O")
                .gender(Gender.M)
                .birthdate(LocalDate.of(1995, 05, 22))
                .email("matteo@gmail.com")
                .telephoneNumber("3317426961")
                .username("alberto").build();
        customerMap.put("alberto", alberto);

        customerRepo.add(matteo);
        customerRepo.add(daniele);
        customerRepo.add(mario);
        customerRepo.add(sokol);
        customerRepo.add(alberto);
	}

	private void provideTraingProgramData(Repository<TrainingProgram> trainingRepo ) throws InvalidValueException, ParseException {

		final TrainingProgram matteosProgram = new TrainingProgram.TrainingProgramBuilder()
				.customer(getCustomerByUsername("matteo", customerMap))
				.tutor(getEmployeeByUsername("totti", employeeMap))
				.goal(Goal.BUILD_MUSCLE)
				.periodValidationFrom(DateUtils.parseDateFromString("2019-11-20"))
				.periodValidationTo(DateUtils.parseDateFromString("2019-11-25"))
				.buildTrainingProgramPlan();

		final TrainingProgram danielesProgram = new TrainingProgram.TrainingProgramBuilder()
				.customer(getCustomerByUsername("daniele", customerMap))
				.tutor(getEmployeeByUsername("totti", employeeMap))
				.goal(Goal.BUILD_MUSCLE)
				.periodValidationFrom(DateUtils.parseDateFromString("2019-11-21"))
				.periodValidationTo(DateUtils.parseDateFromString("2019-11-26"))
				.buildTrainingProgramPlan();

		final TrainingProgram mariosProgram = new TrainingProgram.TrainingProgramBuilder()
				.customer(getCustomerByUsername("mario", customerMap))
				.tutor(getEmployeeByUsername("totti", employeeMap))
				.goal(Goal.BUILD_MUSCLE)
				.periodValidationFrom(DateUtils.parseDateFromString("2019-11-22"))
				.periodValidationTo(DateUtils.parseDateFromString("2019-11-27"))
				.buildTrainingProgramPlan();

        matteosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.CARDIO)
				.executionMetric(new MetricWithTimer(4,20)).name("enuta a cucchiaino ").buildExercise());
        matteosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.CARDIO)
				.executionMetric(new MetricWithRepetitions(3,30)).name("tenuta laterale ").buildExercise());
        matteosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(3,8)).name("lat machine ").buildExercise());
        matteosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(10,10)).name("push ups").buildExercise());
        matteosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(10,10)).name("pulley ").buildExercise());

        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.CARDIO)
				.executionMetric(new MetricWithTimer(5,20)).name("bicicletta").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("attrazioni").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("pressa").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,20)).name("squat").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.CARDIO)
				.executionMetric(new MetricWithTimer(5,20)).name("corsa").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("pull up").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("manubri").buildExercise());
        danielesProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,20)).name("bulgarian squat").buildExercise());


        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.CARDIO)
				.executionMetric(new MetricWithTimer(5,20)).name("bicicletta").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("attrazioni").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("pressa").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,20)).name("Squat").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.CARDIO)
				.executionMetric(new MetricWithTimer(5,20)).name("corsa").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("pull up").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,10)).name("manubri").buildExercise());
        mariosProgram.addNewExercise(new Exercise.ExerciseBuilder().category(Category.FULL_BODY)
				.executionMetric(new MetricWithRepetitions(5,20)).name("bulgarian squat").buildExercise());


        trainingRepo.add(matteosProgram);
        trainingRepo.add(danielesProgram);
        trainingRepo.add(mariosProgram);
	}

	private void provideEmployeeData(Repository<Employee> employeeRepo) {
        Employee instructorT = Employee.builder()
        		.firstname("Totti")
				.lastname("Merti")
				.username("tutor")
				.fiscalCode("ABCDEF00A67E123K")
				.birthdate(LocalDate.of(1990, 1, 1))
				.address("via Boscone")
				.phone("+39 123 456 7890")
				.build();
        employeeMap.put("totti", instructorT);

        Employee administrator = Employee.builder()
        		.firstname("Admin")
				.lastname("Admin")
				.username("admin")
				.fiscalCode("ABCDEF99A67E123K")
				.birthdate(LocalDate.of(1990, 1, 1))
				.address("via Manzzi")
				.phone("+39 123 456 7890")
				.build();
        employeeMap.put("admin", instructorT);

		employeeRepo.add(administrator);
		employeeRepo.add(instructorT);
	}

	private void provideAdditionalServiceData(Repository<AdditionalService> serviceRepo) {
		AdditionalService saunaService   = AdditionalService.builder().id("01").name("Sauna").description("Sauna").price(100).build();
		AdditionalService piscinaService = AdditionalService.builder().id("02").name("Piscina").description("Piscina").price(20).build();
		AdditionalService freeBarService = AdditionalService.builder().id("03").name("Free bar").description("Free bar").price(100).build();

		serviceRepo.add(saunaService);
		serviceRepo.add(piscinaService);
		serviceRepo.add(freeBarService);
	}

	public void uploadAuthenticationData(AuthService authService, UserRepository userRepo, RoleRepository roleRepo) {
		Set<Permission> tutorPermissions = new HashSet<Permission>();
		tutorPermissions.add(new PermissionImpl("view_info_box", "Vedi le view card dei clienti."));
		tutorPermissions.add(new PermissionImpl("page_dashboard", "Vedi la pagina dashboard."));
		tutorPermissions.add(new PermissionImpl("page_trainingProgram", "Vedi le schede d'allenamento."));
		tutorPermissions.add(new PermissionImpl("editTrainingProgram", "Modifica training program."));
		tutorPermissions.add(new PermissionImpl("page_shift_calendar", "Vedi calendar."));
		tutorPermissions.add(new PermissionImpl("page_instructor", "Pagina instruttore."));
		tutorPermissions.add(new PermissionImpl("compile_program", "Compila nuova scheda d'allenamento."));
		tutorPermissions.add(new PermissionImpl("editProgram", "Modifica training program"));
		tutorPermissions.add(new PermissionImpl("view_info_box", "Vedi detagli cliente"));
		tutorPermissions.add(new PermissionImpl("page_bmi", "Vedi BMI cliente"));
		tutorPermissions.add(new PermissionImpl("page_compileTrainingProgram", "Training program page."));
		tutorPermissions.add(new PermissionImpl("page_workshift_edit", "Modifica workshift"));

		roleRepo.add(Role.builder()
                .name("super_admin")
                .permissions(new HashSet<>(authService.getRegisteredPermissions()))
                .build());

		roleRepo.add(Role.builder()
                .name("instructor")
                .permissions(tutorPermissions)
                .build());

         userRepo.add(User.builder()
				.username("admin")
                .password("admin123")
                .role("super_admin")
				.build());

         userRepo.add(User.builder()
					.username("tutor")
	                .password("tutor123")
	                .role("instructor")
					.build());
	}



	private  Customer getCustomerByUsername(String username, Map<String, Customer> customerMap) {
		return  customerMap.entrySet().stream().filter(c -> c.getKey().equals(username)).findAny().get().getValue();
	}

	private  Employee getEmployeeByUsername(String username, Map<String, Employee> employeeMap) {
		return  employeeMap.entrySet().stream().filter(e -> e.getKey().equals(username)).findAny().get().getValue();
	}

}
