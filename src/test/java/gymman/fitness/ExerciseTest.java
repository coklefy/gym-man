package gymman.fitness;

import gymman.fitness.Exercise.ExerciseBuilder;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * Class to test functionalities from {@link Exercise}.
 *
 */
public class ExerciseTest {
    /** Uninitialized exercise. Will be initialized on setUp **/
    private  Exercise exercise;
    /** Exercise builder to build {@link Exercise}.**/
    private final ExerciseBuilder exerciseBuilder = new Exercise.ExerciseBuilder();


    /**
     * Method to set a  new {@link Exercise} before executing the tests.
     */
    @Before
    public void setUp() {
        exercise = this.exerciseBuilder
                .name("HelloExercise")
                .category(Category.CARDIO)
                .executionMetric(new MetricWithTimer(10, 10))
                .buildExercise();
    }

    /**
     * Method to test building a new {@link Exercise}.
     * It will fail because informations are missing.
     */
    @Test(expected = IllegalStateException.class)
    public void failBuildingNewExerciseTest() {
    @SuppressWarnings("unused")
    final  Exercise abdominal = new Exercise.ExerciseBuilder()
                       .name("abs")
                       .executionMetric(new MetricWithTimer(10, 10))
                       .buildExercise();
    }

    /**
     * Method to test building a new {@link Exercise}.
     */
    @Test
    public void successfullyBuildingNewExerciseTest() {
        assertNotNull("Exercise initalized", exercise);
        assertEquals("Exercise category is cardio", exercise.getCategory(), Category.CARDIO);
        assertEquals("Exercise name is HelloExercise", exercise.getName(), "HelloExercise");
    }

}
