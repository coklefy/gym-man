package gymman.customers;

import java.util.List;
import java.util.Optional;

import gymman.common.Repository;

/**
 * The Interface AdditionalServiceRepository.
 */
public interface AdditionalServiceRepository extends Repository<AdditionalService> {

    /**
     * Gets the first occurrence of additional service by name.
     *
     * @param name the name to look for
     * @return the first occurrence of additional service that it is found.
     */
    Optional<AdditionalService> getByName(String name);

    /**
     * Gets the list.
     *
     * @return the list of the additional services
     */
    List<AdditionalService> getList();

    /**
     * Checks for name.
     *
     * @param name the name
     * @return true, if there is an additional service by this name
     */
    boolean hasName(String name);

    /**
     * Search by name.
     *
     * @param name the name to look for
     * @return the list of additional services that match by this name.
     */
    List<AdditionalService> searchByName(String name);


    /**
     * Search by id.
     *
     * @param idClient the client id to look for
     * @return the list of additional services that match by this id.
     */
    List<AdditionalService> getByIdClient(String idClient);
}
