package gymman.customers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import gymman.common.Repository;
/**
 * The Interface CustomerRepository.
 */
public interface CustomerRepository extends Repository<Customer> {

    /**
     * Gets the first occurrence of customer by user name.
     *
     * @param username the user name to look for
     * @return the first occurrence of customer that it is found.
     */
    Optional<Customer> getByUserName(String username);

    /**
     * Gets customer by fiscal code.
     *
     * @param fiscalCode the fiscal code
     * @return the by fiscal code
     */
    Optional<Customer> getByFiscalCode(String fiscalCode);

    /**
     * Checks if repository contains a customer with this user name.
     *
     * @param username the user name
     * @return true, if repository contains a customer with this user name
     */
    boolean hasUsername(String username);

    /**
     * Gets the items of repository.
     *
     * @return the items
     */
    Collection<Customer> getItems();

    /**
     * Search by user name.
     *
     * @param userName the user name
     * @return the list of customers that match by this user name
     */
    List<Customer> searchByUserName(String userName);

    /**
     * Search by name.
     *
     * @param name the name
     * @return the list of customers that match by this name
     */
    List<Customer> searchByName(String name);
}
