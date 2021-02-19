package gymman.common;

import java.util.List;
import java.util.Optional;

/**
 * Interface for a basic Repository
 * 
 * @param <T> Type of the entities that go in this repository
 */
public interface Repository<T extends Entity> {
	/**
	 * Add an entity
	 * 
	 * @param entity
	 */
	void add(T entity);

	/**
	 * Remove an entity
	 * 
	 * @param entity
	 */
	void remove(T entity);

	/**
	 * Check if the repository contains the entity
	 * 
	 * @param entity
	 * @return true if found, false otherwise
	 */
	boolean contains(T entity);

	/**
	 * Check if the repository contains an entity by its ID
	 * 
	 * @param id ID of the entity to look for
	 * @return true if found, false otherwise
	 */
	boolean containsId(final String id);

	/**
	 * Get an entity by its ID
	 * 
	 * @param id ID of the entity to look for
	 * @return Optional of Entity if found, empty Optional otherwise
	 */
	Optional<T> get(final String id);

	/**
	 * Get all the entities in the repository.
	 * 
	 * @return A List of Entity
	 */
	List<T> getAll();

	/**
	 * Get the entities count
	 * 
	 * @return Entities count
	 */
	int getCount();
}
