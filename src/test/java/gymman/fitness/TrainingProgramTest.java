package gymman.fitness;

import gymman.customers.Customer;
import gymman.customers.InvalidValueException;
import gymman.employees.Employee;
import gymman.utils.DateUtils;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.time.LocalDate;
import org.junit.*;


/**
 * Class for testing {@link TrainingProgram} functionalities.
 *
 */
public class TrainingProgramTest {
        /** training program field. */
        private TrainingProgram trainingProgram;
        /** declare customer. */
        private Customer customer;
        /** declare first exercise. */
        private Exercise ex1;
        /** declare second exercise. */
        private Exercise ex2;
        /** declare third exercise. */
        private Exercise ex3;
        /** declare fourth exercise. */
        private Exercise ex4;
        /** declare fifth exercise. */
        private Exercise ex5;


        @Before
        public void setUp() throws InvalidValueException, ParseException {
        ex1 = new Exercise.ExerciseBuilder().name("first")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        ex2 = new Exercise.ExerciseBuilder().name("second")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        ex3 = new Exercise.ExerciseBuilder().name("third")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        ex4 = new Exercise.ExerciseBuilder().name("fourth")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        ex5 = new Exercise.ExerciseBuilder().name("fifth")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        customer = Customer.builder().firstname("Matteo")
                    .lastname("Manzi")
                    .username("ad")
                    .fiscalCode("MNZMTT95E22C573O")
                    .birthdate(LocalDate.of(1995, 05, 22))
                    .email("ad@gmail.com")
                    .telephoneNumber("3317426961")
                    .build();


        trainingProgram = new TrainingProgram.TrainingProgramBuilder()
                                        .customer(customer)
                                        .goal(Goal.BUILD_MUSCLE)
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

        trainingProgram.addNewExercise(ex1);
        trainingProgram.addNewExercise(ex2);
        trainingProgram.addNewExercise(ex3);
        trainingProgram.addNewExercise(ex4);
        trainingProgram.addNewExercise(ex5);

    }

    /**
     *  Method to test adding a new exercise to the training program.
     */
    @Test
    public void trainingProgramExercisesTest() {

        assertEquals(trainingProgram.getExercises().size(), 5);

        trainingProgram.removeExercise("fifth");
        assertEquals(trainingProgram.getExercises().size(), 4);

        trainingProgram.addNewExercise(new Exercise.ExerciseBuilder().name("fifth")
                .executionMetric(new MetricWithRepetitions(10, 10))
                .category(Category.FULL_BODY).buildExercise());
        assertEquals(trainingProgram.getExercises().size(), 5);

    }

        /**
         * Method to test the training program validation period.
         *
         * @throws ParseException
         * @throws {@link         IllegalStateException}
         */
        @Test(expected = IllegalStateException.class)
        public void validationPeriodErrorTest() throws ParseException {
        new TrainingProgram.TrainingProgramBuilder()
                .customer(customer)
                .goal(Goal.BUILD_MUSCLE)
                .periodValidationFrom(DateUtils.parseDateFromString("2019-04-04"))
                .periodValidationTo(DateUtils.parseDateFromString("2019-01-05"))
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
    }

    /**
     *  Method to test adding a the same exercise twice.
     *  @throws {@link IllegalStateException}
     */
    @Test(expected = IllegalStateException.class)
    public void addingSameExerciseToProgramTest() {

       final Exercise newEx = new Exercise.ExerciseBuilder().name("first")
                .executionMetric(new MetricWithTimer(10, 10))
                .category(Category.FULL_BODY).buildExercise();

        trainingProgram.addNewExercise(newEx);
    }


}
