package gymman.customers;

import java.util.Collection;
import java.util.List;
import gymman.common.Repository;

/**
 * The Interface RegistrationRepository.
 */
public interface RegistrationRepository extends Repository<Registration> {

    /**
     * Gets the first occurrence of customer by id client.
     *
     * @param idclient the id client to look for.
     * @return the first occurrence of registration that it is found.
     */
    List<Registration> getByIdClient(String idclient);

    /**
     * Gets the list.
     *
     * @return the list
     */
    Collection<Registration> getList();
}
