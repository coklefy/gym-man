package gymman.auth;

import java.util.HashSet;
import java.util.Set;

import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * A Role is basically a collection of Permission. Roles can be assigned to users.
 */
public class Role extends BaseEntity {
	@Getter private String name;
	@Getter private String description;
	@Singular private Set<Permission> permissions;

	private Role() { }

	/**
	 * Checks if the role contains the specified permission
	 * 
	 * @param permission
	 * @return true if the role contains the specified permission, false otherwise
	 */
	public boolean hasPermission(final Permission permission) {
		return this.permissions.contains(permission);
	}

	/**
	 * Add a permission to the role
	 * 
	 * @param permission
	 */
	public void addPermission(final Permission permission) {
		this.permissions.add(permission);
	}

	/**
	 * Remove a permission from the role
	 * 
	 * @param permission
	 */
	public void removePermission(final Permission permission) {
		this.permissions.remove(permission);
	}

	/**
	 * Get all permissions of this role
	 * 
	 * @return A Set of Permission
	 */
	public Set<Permission> getPermissions() {
		return new HashSet<Permission>(this.permissions);
	}

	@Override
	public String toString() {
		return String.format("%s: %s", this.name, this.description);
	}

	@Builder
	private static Role of(
		String id,
		String name,
		String description,
		Set<Permission> permissions
	) {
		final Role role = new Role();

		if (id != null) {
			role.id = id;
		}

		role.name = name;
		role.description = description != null ? description : "";
		role.permissions = new HashSet<>();

		if (permissions != null) {
			role.permissions.addAll(permissions);
		}

		return role;
	}
}
