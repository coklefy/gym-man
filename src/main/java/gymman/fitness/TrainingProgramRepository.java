package gymman.fitness;

import java.util.Optional;

import gymman.common.Repository;

/**
 *
 * Interface to offer query operations on TrainingProgram repository.
 *
 */
public interface TrainingProgramRepository extends Repository<TrainingProgram> {

    /**
     * Search a training program by the key.
     *
     * @param username :  the key
     * @return the by training Program
     */
    Optional<TrainingProgram> getByUsername(String username);

    /**
     * Search in TrainingProgramPlanReposity if the
     * TrainingProgram's key exists.
     *
     * @param username : the key
     * @return true, if key exists
     */
    boolean thereIsKey(String username);

}
