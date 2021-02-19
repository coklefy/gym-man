package gymman.fitness;

/**
 *
 * Interface to offer the methods to be implemented by builder class.
 * Offers the basic functionalities to build a new Exercise.
 *
 */
public interface WorkoutBuilder {

    /**
     * Method to set the name of the new exercise.
     * @param name is the exercise denomination
     * @return the builder
     */
     WorkoutBuilder name(String name);

    /**
     * Method to set the category of the new exercise.
     * @param category is the exercise category
     * @return the builder
     */
     WorkoutBuilder category(Category category);


    /**
     * Method to set the execution metric of the new exercise.
     * @param executionMetric is the exercise metric. It can be with timer or with repetitions.
     * @return the builder
     */
     WorkoutBuilder executionMetric(Metric executionMetric);

    /**
     * Method to build the new Exercise after verifying all the information.
     * @return the created Exercise
     */
     Exercise buildExercise();
}
