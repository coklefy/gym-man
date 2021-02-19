package gymman.auth;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * A User has a username, password and Role. It can login and logout from the system.
 */
public class User extends BaseEntity {
	@Getter private String username;
	@Getter private HashCode password;
	@Getter private String role;

	private User() { }

	/**
	 * Check if the provided plaintext password is correct
	 * 
	 * @param password Plaintext password
	 * @return true if the password matches, false otherwise
	 */
	public boolean verifyPassword(final String password) {
		return getHash(password).equals(this.password);
	}

	@Override
	public String toString() {
		return String.format("User<id='%s' username='%s'>", this.id, this.username);
	}

	/**
	 * Utility method that checks if a password is formally valid, that is if a certain
	 * length is reached.
	 * 
	 * @param password Plaintext password
	 * @return true if the password is valid, false otherwise
	 */
	public static boolean isValidPassword(String password) {
		return password != null
			&& password.trim().length() > 0
			&& password.length() >= 8;
	}

	/**
	 * Utility method that hashes an input string
	 * 
	 * @param password
	 * @return Hashed version of the input
	 */
	public static HashCode getHash(String password) {
		return Hashing.sha256().hashString(password, Charsets.UTF_8);
	}

	private void setPassword(final String password) {
		this.password = getHash(password);
	}

	@Builder
	private static User of(
		String id,
		String username,
		String password,
		HashCode passwordHash,
		String role
	) {
		final User user = new User();

		if (id != null) {
			user.id = id;
		}

		if (password != null) {
			if (!isValidPassword(password)) {
				throw new IllegalArgumentException("La password deve contenere almeno 8 caratteri");
			}
			user.setPassword(password);
		}

		if (passwordHash != null) {
			user.password = HashCode.fromBytes(passwordHash.asBytes());
		}

		user.username = username;
		user.role = role;

		return user;
	}
}
