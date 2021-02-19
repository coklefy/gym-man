package gymman.auth;

import java.util.List;
import java.util.Optional;

import gymman.common.Repository;

/**
 * The Repository for Role entities.
 */
public interface RoleRepository extends Repository<Role> {
	/**
	 * Gets the Role by name
	 * @param name
	 * @return Optional of Role
	 */
	Optional<Role> getByName(String name);

	/**
	 * Find roles with partially matching name
	 * @param name
	 * @return A List of found Roles
	 */
	List<Role> searchByName(String name);
}
