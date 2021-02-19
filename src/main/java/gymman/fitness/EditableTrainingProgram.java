package gymman.fitness;

/**
 * Interface to offer the basic functionalities to edit a TrainingProgram.
 *
 */
public interface EditableTrainingProgram {


    /**
     * Method to add a new Exercise to the set.
     *
     * @param newExercise : exercise
     * @throws  IllegalStateException if the exercise name already exists
     *
     */
     void addNewExercise(Exercise newExercise);


    /**
     * Method to remove by the exercise name an Exercise from the set.
     *
     * @param name : exercise name
     *
     */
     void removeExercise(String name);

}
