package gymman.auth;

import java.util.Optional;
import gymman.common.Repository;

/**
 * The Repository for User entities.
 */
public interface UserRepository extends Repository<User> {
	/**
	 * Get a User by its username
	 * 
	 * @param username
	 * @return Optional of User if found, empty Optional otherwise
	 */
	Optional<User> getByUsername(String username);

	/**
	 * Checks if the provided username is present in the repository
	 * 
	 * @param username
	 * @return true if found, false otherwise
	 */
	boolean hasUsername(String username);
}
