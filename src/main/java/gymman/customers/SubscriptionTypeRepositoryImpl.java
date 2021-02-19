package gymman.customers;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;

/**
 * The Class SubscriptionTypeRepositoryImpl represent the repository for subscriptions.
 */
public final class SubscriptionTypeRepositoryImpl extends MemoryRepository<SubscriptionType> implements SubscriptionTypeRepository {

    /**
     * Instantiates a new SubscriptionTypeRepositoryImpl.
     */
    public SubscriptionTypeRepositoryImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final SubscriptionType subscription) throws DuplicateEntityException {
        final Optional<SubscriptionType> existing = this.items.stream()
                .filter(e -> e.getName().equals(subscription.getName()) && !e.getId().equals(subscription.getId()))
                .findFirst();
        if (existing.isPresent()) {
            throw new DuplicateEntityException(subscription.toString(), existing.get().toString());
        }
        super.add(subscription);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SubscriptionType> getByName(final String name) {
         return this.items.stream()
                 .filter(e -> e.getName().equals(name))
                 .findFirst();
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
    public Collection<SubscriptionType> getItems() {
        return this.items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubscriptionType> searchByName(final String name) {
        return this.items.stream()
                .filter(e -> e.getName().toLowerCase(Locale.getDefault()).contains(name.toLowerCase(Locale.getDefault())))
                .collect(Collectors.toList());
    }
}
