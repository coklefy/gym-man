package gymman.customers;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;
import gymman.common.SearchUtils;

/**
 * The Class CustomerRepositoryImpl represent the repository for customers.
 */
public final class CustomerRepositoryImpl extends MemoryRepository<Customer> implements CustomerRepository {

    /**
     * Instantiates a new customer repository.
     */
    public CustomerRepositoryImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final Customer customer) throws DuplicateEntityException {
        final Optional<Customer> existing = this.items.stream()
                .filter(e -> e.getFiscalCode().equals(customer.getFiscalCode())
                || e.getUsername().equals(customer.getUsername())
                && !e.getId().equals(customer.getId()))
                .findFirst();
        if (existing.isPresent()) {
            throw new DuplicateEntityException(customer.toString(), existing.toString());
        }
        super.add(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> getByFiscalCode(final String fiscalCode) {
        return this.items.stream()
                .filter(e -> e.getFiscalCode().equals(fiscalCode))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasUsername(final String username) {
        return this.items.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst().isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Customer> getItems() {
        return this.items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> searchByUserName(final String userName) {
        return this.items.stream()
                .filter(e -> e.getUsername().toLowerCase(Locale.getDefault()).contains(userName.toLowerCase(Locale.getDefault())))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> searchByName(final String name) {
        return this.items.stream()
            .filter(e -> SearchUtils.containsAllWordsCaseInsensitive(e.getFirstname() + " " + e.getLastname(), name))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> getByUserName(final String username) {
        return this.items.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst();
    }

}
