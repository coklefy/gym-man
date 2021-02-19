package gymman.customers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;


/**
 * The Class AdditionalServiceRepositoryImpl represent the repository for additional services.
 */
public class AdditionalServiceRepositoryImpl extends MemoryRepository<AdditionalService> implements AdditionalServiceRepository {

    /**
     * Instantiates a new additional service repository.
     */
    public AdditionalServiceRepositoryImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final AdditionalService addService) throws DuplicateEntityException {
        final Optional<AdditionalService> existing = this.items.stream()
            .filter(e -> e.getName().equals(addService.getName()) && !e.getId().equals(addService.getId()))
            .findFirst();
        if (existing.isPresent()) {
            throw new DuplicateEntityException(addService.toString(), existing.toString());
        }
        super.add(addService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AdditionalService> getByName(final String name) {
        return this.items.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdditionalService> getList() {
        return this.items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasName(final String name) {
        return this.items.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst().isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdditionalService> searchByName(final String name) {
        return this.items.stream()
                .filter(e -> e.getName().toLowerCase(Locale.getDefault()).contains(name.toLowerCase(Locale.getDefault())))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdditionalService> getByIdClient(String idClient) {
        return this.items.stream()
                .filter(e -> e.getIdClient().equals(idClient)).collect(Collectors.toList());
    }

}
