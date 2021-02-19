package gymman.fitness;

import java.util.Optional;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;

/**
 * Class to create a TrainingProgram repository.
 * Offer basic functionalities to search the training program and
 * to add the new ones if they hadn't been already added to the
 * repository.
 *
 */
public class TrainingProgramRepositoryImpl extends MemoryRepository<TrainingProgram> implements TrainingProgramRepository {

   @Override
   public final Optional<TrainingProgram> getByUsername(final String username) {
        return  this.items.stream()
                    .filter(e -> e.getCustomer().getUsername().equals(username))
                    .findFirst();
       }

    @Override
    public final boolean thereIsKey(final String key) {
        return this.items.stream()
                .filter(e -> e.getKey().equals(key))
                .findFirst().isPresent();
    }

    /* (non-Javadoc)
     * @see model.MemoryRepository#add(model.Entity)
     */
    @Override
    public void add(final TrainingProgram program) throws DuplicateEntityException {
        final Optional<TrainingProgram> existing = this.items.stream()
            .filter(e -> e.getKey().equals(program.getKey()))
            .findFirst();
        if (existing.isPresent()) {
            throw new DuplicateEntityException(program.toString(), existing.toString());
        }
        super.add(program);
    }


}
