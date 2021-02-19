package gymman.customers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import gymman.common.Repository;

/**
 * The Interface SubscriptionTypeRepository.
 */
public interface SubscriptionTypeRepository extends Repository<SubscriptionType> {

    /**
     * Gets the first occurrence of subscription by name.
     *
     * @param name the name to look for
     * @return the first occurrence of subscription that it is found.
     */
    Optional<SubscriptionType> getByName(String name);

    /**
     * Checks for name.
     *
     * @param name the name
     * @return true, if repository contains a subscription with this user name
     */
    boolean hasName(String name);

    /**
     * Gets the items.
     *
     * @return the items
     */
    Collection<SubscriptionType> getItems();

    /**
     * Search by name.
     *
     * @param name the name
     * @return the list of subscriptions that match by this name
     */
    List<SubscriptionType> searchByName(String name);
}
