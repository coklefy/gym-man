package gymman.auth;

/**
 * A Permission consists of a name which acts as an ID and a description.
 * It is used in conjunction of the Authentication Service.
 */
public interface Permission {
	/**
	 * Get the permission's name
	 * @return permission name
	 */
	String getName();

	/**
	 * Get the permission's description
	 * @return permission description
	 */
	String getDescription();
}
