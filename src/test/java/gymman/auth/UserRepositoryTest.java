package gymman.auth;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;

public class UserRepositoryTest {
	public UserRepository repo;

	@Before
	public void setUp() throws Exception {
		this.repo = new UserRepositoryImpl();
	}

	@Test
	public void testHasUsernameReturnsFalseWhenUsernameNotFound() {
		assertFalse(this.repo.hasUsername("No One"));
	}

	@Test
	public void testHasUsernameReturnsTrueWhenUsernameFound() {
		User user = User.builder().username("pippo").build();

		try {
			this.repo.add(user);
		} catch (DuplicateEntityException e) {
			fail();
		}

		assertTrue(this.repo.hasUsername("pippo"));
	}

	@Test
	public void testEmptyOptionalOnUsernameNotFound() {
		Optional<User> user = this.repo.getByUsername("No One");
		assertThat(user.isPresent(), is(false));
	}

	@Test
	public void testCanGetUserByUsername() {
		User user = User.builder().username("pippo").build();

		try {
			this.repo.add(user);
		} catch (DuplicateEntityException e) {
			fail();
		}

		assertThat(this.repo.getByUsername("pippo").get().getUsername(), is("pippo"));
	}

	@Test
	public void testPreventAddingUserWithSameUsernameButDifferentId() {
		User user1 = User.builder().username("pippo").build();
		User user2 = User.builder().username("pippo").build();

		try {
			this.repo.add(user1);
			this.repo.add(user2);
			fail();
		} catch (DuplicateEntityException e) {
			String message = String.format("Tried to add '%s' but '%s' already exists", user2.toString(), user1.toString());
			assertThat(e.getMessage(), is(message));
		}

	}
}
