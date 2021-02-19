package gymman.customers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;

/**
 * The Class RegistrationRepositoryImpl represent the repository for registrations.
 */
public final class RegistrationRepositoryImpl extends MemoryRepository<Registration> implements RegistrationRepository {

    /**
     * Instantiates a new registration repository.
     */
    public RegistrationRepositoryImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final Registration registration) throws DuplicateEntityException {
        final List<Registration> existing = this.items.stream()
                .filter(e -> e.getIdClient().equals(registration.getIdClient()) && !e.getId().equals(registration.getId()))
                .collect(Collectors.toList());
        final List<Boolean> activeRegistration = existing.stream()
                .filter(e -> e.getType().equals(registration.getType()))
                .map(e -> e.isActive(registration.getSigningDate()))
                .collect(Collectors.toList());
        if (!existing.isEmpty() && activeRegistration.contains(true)) {
            throw new DuplicateEntityException(registration.toString(), existing.toString());
        }
        super.add(registration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Registration> getList() {
        return this.items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Registration> getByIdClient(final String idClient) {
        return this.items.stream()
                .filter(e -> e.getIdClient().equals(idClient)).collect(Collectors.toList());
    }

}
