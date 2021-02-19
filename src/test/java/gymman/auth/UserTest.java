package gymman.auth;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCanVerifyPassword() {
		User user = User.builder().username("pippo").password("plutopluto").build();

		assertThat(user.verifyPassword("plutopluto"), is(true));
		assertThat(user.verifyPassword("paperino"), is(false));
	}

	@Test
	public void testCanGetRole() {
		User user = User.builder().username("pippo")
			.password("plutopluto")
			.role("role1")
			.build();

		assertThat(user.getRole(), is("role1"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testErrorOnInvalidPassword() {
		User.builder()
			.username("pippo")
			.password("abc") // password is not long enough
			.build();
	}
}
