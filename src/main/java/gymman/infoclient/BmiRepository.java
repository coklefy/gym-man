package gymman.infoclient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;


/**
 * The Class BmiRepository is Repository for BMI.
 */
public class BmiRepository extends MemoryRepository<Bmi>{

    /*
     * Control for Duplicate Insert at the same times
     */
    @Override
    public void add(final Bmi bmi) throws DuplicateEntityException {
        final Optional<Bmi> existing = this.items.stream()
                .filter(e -> e.getDate().equals(bmi.getDate()) && !e.getId().equals(bmi.getId())).findFirst();
        if (existing.isPresent()) {
            throw new DuplicateEntityException(bmi.toString(), existing.toString());
        }
        super.add(bmi);
    }

    /**
     * Gets the by customer id.
     *
     * @param customerID the customer ID
     * @return the by customer id
     */
    public List<Bmi> getByCustomerId(final String customerID) {
        return this.items.stream()
                .filter(e -> e.getCustomerid().equals(customerID))
                .collect(Collectors.toList());
    }

}
