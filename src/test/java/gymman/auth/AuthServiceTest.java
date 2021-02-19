package gymman.auth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthServiceTest {

	private AuthService auth;
	private User user1;

	@Before
	public void setUp() throws Exception {
		user1 = User.builder().username("pippo")
			.id("123")
			.password("passpass")
			.role("ROLE_1")
			.build();

		User user2 = User.builder().username("pluto")
			.id("456")
			.password("passpass")
			.role("ROLE_2")
			.build();

		Set<Permission> permissions1 = new HashSet<>(Arrays.asList(
			new PermissionImpl("PERM_1", "foobar 1"),
			new PermissionImpl("PERM_2", "foobar 2")
		));

		Set<Permission> permissions2 = new HashSet<>(Arrays.asList(
			new PermissionImpl("PERM_1", "foobar 1")
		));

		Role role1 = Role.builder().name("ROLE_1")
			.permissions(permissions1)
			.build();

		Role role2 = Role.builder().name("ROLE_2")
			.permissions(permissions2)
			.build();

		UserRepository userRepo = Mockito.mock(UserRepository.class);
		Mockito.when(userRepo.get("123")).thenReturn(Optional.of(user1));
		Mockito.when(userRepo.hasUsername("pippo")).thenReturn(true);
		Mockito.when(userRepo.getByUsername("pippo")).thenReturn(Optional.of(user1));

		Mockito.when(userRepo.get("456")).thenReturn(Optional.of(user2));
		Mockito.when(userRepo.hasUsername("pluto")).thenReturn(true);
		Mockito.when(userRepo.getByUsername("pluto")).thenReturn(Optional.of(user2));

		RoleRepository roleRepo = Mockito.mock(RoleRepository.class);
		Mockito.when(roleRepo.getByName("ROLE_1")).thenReturn(Optional.of(role1));
		Mockito.when(roleRepo.getByName("ROLE_2")).thenReturn(Optional.of(role2));

		this.auth = new AuthServiceImpl(userRepo, roleRepo);
	}

	@Test
	public void testCanLogin() {
		this.auth.login("pippo", "passpass");
		assertTrue(this.auth.isLoggedIn());
		assertThat(this.auth.getLoggedInUser().getUsername(), is("pippo"));
	}

	@Test
	public void testCanLogout() {
		this.auth.login("pippo", "passpass");
		assertTrue(this.auth.isLoggedIn());

		this.auth.logout();
		assertFalse(this.auth.isLoggedIn());
	}

	@Test
	public void testLogoutWhenNotLoggedInDoesNothing() {
		this.auth.logout();
		assertFalse(this.auth.isLoggedIn());
	}

	@Test
	public void testLoggedInIsFalseOnStartup() {
		assertFalse(this.auth.isLoggedIn());
	}

	@Test
	public void testCanGetLoggedInUser() {
		this.auth.login("pippo", "passpass");
		assertTrue(this.auth.getLoggedInUser() instanceof User);
	}

	@Test(expected = NotLoggedInException.class)
	public void testErrorWhenGettingUserWithoutLoggingIn() {
		assertFalse(this.auth.isLoggedIn());
		this.auth.getLoggedInUser();
	}

	@Test(expected = LoginException.class)
	public void testErrorWhenLoginWithUnknownUser() {
		this.auth.login("IDontExist", "foobar");
	}

	@Test(expected = LoginException.class)
	public void testErrorWhenWrongPassword() {
		this.auth.login("pippo", "wrongpassword");
	}

	@Test
	public void testCanCheckForPermission() {
		this.auth.login("pippo", "passpass");
		Permission perm1 = new PermissionImpl("PERM_1", "foobar");
		Permission perm2 = new PermissionImpl("PERM_2", "foobar");
		Permission perm3 = new PermissionImpl("PERM_3", "foobar");

		assertThat(this.auth.userHasPermission(perm1), is(true));
		assertThat(this.auth.userHasPermission(perm2), is(true));
		assertThat(this.auth.userHasPermission(perm3), is(false));
	}

	@Test
	public void testCanRegisterPermission() throws DuplicatePermissionException {
		Permission perm = new PermissionImpl("perm1", "perm1");
		this.auth.registerPermission(perm);

		assertThat(this.auth.getRegisteredPermissions().contains(perm), is(true));
	}

	@Test
	public void testOnLoginHandlerIsCalledOnSuccessfulLogin() {
		@SuppressWarnings("unchecked")
		Consumer<User> handler = Mockito.mock(Consumer.class);

		this.auth.addOnLoginHandler(handler);

		this.auth.login("pippo", "passpass");

		Mockito.verify(handler, Mockito.times(1)).accept(Mockito.any(User.class));
	}

	@Test
	public void testOnLoginHandlerIsNotCalledOnFailedLogin() {
		@SuppressWarnings("unchecked")
		Consumer<User> handler = Mockito.mock(Consumer.class);

		this.auth.addOnLoginHandler(handler);

		try {
			this.auth.login("pippo", "wrongpass");
		} catch (LoginException e) {
			// noop
		}

		Mockito.verify(handler, Mockito.never()).accept(Mockito.any(User.class));
	}

	@Test
	public void testOnLogoutHandlerIsCalledOnLogout() {
		@SuppressWarnings("unchecked")
		Consumer<User> handler = Mockito.mock(Consumer.class);

		this.auth.login("pippo", "passpass");

		this.auth.addOnLogoutHandler(handler);

		this.auth.logout();

		Mockito.verify(handler, Mockito.times(1)).accept(this.user1);
	}

	@Test
	public void testOnLogoutHandlerIsNeverCalledWhenAlreadyLoggedOut() {
		@SuppressWarnings("unchecked")
		Consumer<User> handler = Mockito.mock(Consumer.class);

		this.auth.addOnLogoutHandler(handler);

		this.auth.logout();

		Mockito.verify(handler, Mockito.times(0)).accept(this.user1);
	}
}
