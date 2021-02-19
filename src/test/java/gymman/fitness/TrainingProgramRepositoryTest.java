package gymman.fitness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import java.text.ParseException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.Customer;
import gymman.customers.InvalidValueException;
import gymman.employees.Employee;
import gymman.utils.DateUtils;

/**
 * Class to test the {@link TrainingProgram} repository.
 *
 */
public class TrainingProgramRepositoryTest {
        /** All training program repository. */
        TrainingProgramRepository programsRepo = new TrainingProgramRepositoryImpl();
        /** Training programs. */
        TrainingProgram manziPlan, ricardoPlan;

        /**
         * Set up the necessary variables that have to be tested.
         *
         * @throws InvalidValueException    invalid parameters
         * @throws DuplicateEntityException duplicate entity
         * @throws ParseException
         */
        @Before
        public void setUp() throws InvalidValueException, DuplicateEntityException, ParseException {

        final Customer customerI = Customer.builder().firstname("Matteo").lastname("Manzi")
                .username("manzi").fiscalCode("MNZMTT95E22C573O")
                .birthdate(LocalDate.of(1995, 05, 22)).email("ad@gmail.com")
                .telephoneNumber("3317426961").build();

        final Customer customerII = Customer.builder().firstname("Ricardo").lastname("Manzi")
                .username("rica").fiscalCode("MNZdeT95E22C573O")
                .birthdate(LocalDate.of(1995, 05, 22)).email("ad@gmail.com")
                .telephoneNumber("3317426961").build();

        final Exercise ex1 = new Exercise.ExerciseBuilder().name("first")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        final Exercise ex2 = new Exercise.ExerciseBuilder().name("second")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        final Exercise ex3 = new Exercise.ExerciseBuilder().name("third")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();



        manziPlan = new TrainingProgram.TrainingProgramBuilder()
                .customer(customerI).goal(Goal.BUILD_MUSCLE)
                .periodValidationFrom(DateUtils.parseDateFromString("2019-04-04"))
                .periodValidationTo(DateUtils.parseDateFromString("2019-05-05"))
                .tutor(Employee.builder()
                		.firstname("Admin")
                		.lastname("Admin")
                		.username("superadmin")
                		.fiscalCode("ABCDEF99A67E123K")
                		.birthdate(LocalDate.of(1990, 1, 1))
                		.address("abc")
                		.phone("+39 123 456 7890")
                		.build())
                .buildTrainingProgramPlan();
        manziPlan.addNewExercise(ex1);
        manziPlan.addNewExercise(ex2);

        ricardoPlan = new TrainingProgram.TrainingProgramBuilder()
                .customer(customerII).goal(Goal.BUILD_MUSCLE)
                .periodValidationFrom(DateUtils.parseDateFromString("2019-04-04"))
                .periodValidationTo(DateUtils.parseDateFromString("2019-05-05"))
                .tutor(Employee.builder()
                		.firstname("Admin")
                		.lastname("Admin")
                		.username("superadmin")
                		.fiscalCode("ABCDEF99A67E123K")
                		.birthdate(LocalDate.of(1990, 1, 1))
                		.address("abc")
                		.phone("+39 123 456 7890")
                		.build())
                .buildTrainingProgramPlan();
         ricardoPlan.addNewExercise(ex2);
         ricardoPlan.addNewExercise(ex3);

         programsRepo.add(ricardoPlan);
         programsRepo.add(manziPlan);

   }

    /**
     * Test to check if query search by user name is correct. Test the training
     * program informations from this query.
     */
    @Test
    public void getCustomerInfoTest() {
        final TrainingProgram manziDemo = this.programsRepo.getByUsername("manzi").get();
       assertNotNull("Training program is not null", manziDemo);

       final TrainingProgram ricardoDemo = this.programsRepo.getByUsername("rica").get();
       assertNotNull(ricardoDemo);

       assertThat("username is manzi", manziDemo.getCustomer().getUsername(), is("manzi"));
       assertThat("first name is Matteo", manziDemo.getCustomer().getFirstname(), is("Matteo"));
       assertThat("last name is Manzi", manziDemo.getCustomer().getLastname(), is("Manzi"));

       assertThat("username is rica", ricardoDemo.getCustomer().getUsername(), is("rica"));
       assertThat("first name is Ricardo", ricardoDemo.getCustomer().getFirstname(), is("Ricardo"));
       assertThat("last name is Manzi", ricardoDemo.getCustomer().getLastname(), is("Manzi"));

    }

    /**
     * Test adding and removing exercises from a training program.
     */
    @Test
    public void addAndRemoveExercisesTest() {

       assertEquals("manziPlan has 2 exercises", this.manziPlan.getExercises().size(), 2);
       assertEquals("manziPlan has 2 exercises", this.programsRepo.getByUsername("manzi").get().getExercises().size(), 2);

       this.manziPlan.removeExercise("first");
       assertEquals("manziPlan has 1 exercise", this.manziPlan.getExercises().size(), 1);
       assertEquals("manziPlan has 1 exercise", this.programsRepo.getByUsername("manzi").get().getExercises().size(), 1);

       this.ricardoPlan.removeExercise("second");
       assertEquals("ricardoPlan has 1 exercise", this.ricardoPlan.getExercises().size(), 1);
       assertEquals("ricardoPlan has 1 exercise", this.programsRepo.getByUsername("rica").get().getExercises().size(), 1);

       this.manziPlan.removeExercise("third");
       assertEquals("manziPlan has 1 exercise", this.manziPlan.getExercises().size(), 1);
       assertEquals("manziPlan has 1 exercise", this.programsRepo.getByUsername("manzi").get().getExercises().size(), 1);

       this.ricardoPlan.removeExercise("third");
       assertEquals("ricardoPlan has 0 exercise", this.ricardoPlan.getExercises().size(), 0);
       assertEquals("ricardoPlan has 0 exercise", this.programsRepo.getByUsername("rica").get().getExercises().size(), 0);


       this.manziPlan.addNewExercise(new Exercise.ExerciseBuilder().name("third")
               .executionMetric(new MetricWithTimer(10, 10))
               .category(Category.FULL_BODY).buildExercise());

       this.ricardoPlan.addNewExercise(new Exercise.ExerciseBuilder().name("third")
                                   .executionMetric(new MetricWithTimer(10, 10))
                                   .category(Category.FULL_BODY).buildExercise());

       assertEquals("manziPlan has 2 exercise", this.manziPlan.getExercises().size(), 2);
       assertEquals("manziPlan has 2 exercise", this.programsRepo.getByUsername("manzi").get().getExercises().size(), 2);

       assertEquals("ricardoPlan has 1 exercise", this.ricardoPlan.getExercises().size(), 1);
       assertEquals("ricardoPlan has 1 exercise", this.programsRepo.getByUsername("rica").get().getExercises().size(), 1);

    }

    /**
     * Test to successfully modifie an existing training program.
     */
    @Test
    public void modifyTrainingProgramInfoTest() {
      assertEquals("manziPlan goal is Build Muscle", this.manziPlan.getGoal(), Goal.BUILD_MUSCLE);
      assertEquals("manziPlan goal is Build Muscle", this.programsRepo.getByUsername("manzi").get().getGoal(), Goal.BUILD_MUSCLE);

      this.manziPlan.setGoal(Goal.DEFENITION);
      assertEquals("manziPlan goal is Definition", this.manziPlan.getGoal(), Goal.DEFENITION);
      assertEquals("manziPlan goal is Definition", this.programsRepo.getByUsername("manzi").get().getGoal(), Goal.DEFENITION);

      assertEquals("ricardoPlan goal is Build Muscle", this.ricardoPlan.getGoal(), Goal.BUILD_MUSCLE);
      assertEquals("ricardoPlan goal is Build Muscle", this.programsRepo.getByUsername("rica").get().getGoal(), Goal.BUILD_MUSCLE);

      this.ricardoPlan.setGoal(Goal.INCREASE_MUSCLE_SIZE);
      assertEquals("ricardoPlan goal is Increase Muscle", this.ricardoPlan.getGoal(), Goal.INCREASE_MUSCLE_SIZE);
      assertEquals("ricardoPlan goal is Increase Muscle", this.programsRepo.getByUsername("rica").get().getGoal(), Goal.INCREASE_MUSCLE_SIZE);

    }

        /**
         * Test adding two training programs for the same customer.
         *
         * @throws DuplicateEntityException duplicate entity
         * @throws InvalidValueException    invalid parameters
         * @throws ParseException
         */
        @Test(expected = DuplicateEntityException.class)
        public void addTwoProgramsForSameCustomerTest()
                        throws InvalidValueException, DuplicateEntityException, ParseException {
       final Customer customer1 = Customer.builder().firstname("Matteo").lastname("Manzi")
               .username("manzi").fiscalCode("MNZMTT95E22C573O")
               .birthdate(LocalDate.of(1995, 05, 22)).email("ad@gmail.com")
               .telephoneNumber("3317426961").build();

       this.programsRepo.add(new TrainingProgram.TrainingProgramBuilder()
               .customer(customer1).goal(Goal.BUILD_MUSCLE)
               .periodValidationFrom(DateUtils.parseDateFromString("2019-04-04"))
               .periodValidationTo(DateUtils.parseDateFromString("2019-05-05"))
               .tutor(Employee.builder()
               		.firstname("Admin")
               		.lastname("Admin")
               		.username("superadmin")
               		.fiscalCode("ABCDEF99A67E123K")
               		.birthdate(LocalDate.of(1990, 1, 1))
               		.address("abc")
               		.phone("+39 123 456 7890")
               		.build())
               .buildTrainingProgramPlan());

    }

    /**
     * Test to delete a training program. Test also adding a new one to the
     * repository.
     * @throws DuplicateEntityException duplicate entity
     */
    @Test
    public void removeAndAddTrainingProgramTest() throws DuplicateEntityException {
       assertEquals("repo has 2 training programs", this.programsRepo.getAll().size(), 2);

       this.programsRepo.remove(manziPlan);
       assertEquals("repo has 1 training programs", this.programsRepo.getAll().size(), 1);

       this.programsRepo.add(manziPlan);
       assertEquals("repo has 2 training programs", this.programsRepo.getAll().size(), 2);

    }

    /**
     * Test adding the same exercise for the same customer in the same
     * training program.
     * @throws IllegalStateException
     */
    @Test(expected = IllegalStateException.class)
    public void addingSameExerciseTest() {
        this.manziPlan.addNewExercise(new Exercise.ExerciseBuilder().name("first")
               .executionMetric(new MetricWithTimer(10, 10))
               .category(Category.FULL_BODY).buildExercise());
    }

}
