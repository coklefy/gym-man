package gymman.auth;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class RoleTest {
	public Role role;

	@Before
	public void setUp() throws Exception {
		this.role = Role.builder().name("foo").description("foobar").build();
	}

	@Test
	public void testShouldNotHavePermissionsOnCreation() {
		assertTrue(this.role.getPermissions().isEmpty());
	}

	@Test
	public void testCanAddPermissions() {
		Permission perm1 = new PermissionImpl("perm1", "description 1");
		Permission perm2 = new PermissionImpl("perm2", "description 2");

		this.role.addPermission(perm1);
		assertTrue(this.role.hasPermission(perm1));
		assertTrue(this.role.getPermissions().contains(perm1));
		assertThat(this.role.getPermissions().size(), is(1));

		this.role.addPermission(perm2);
		assertTrue(this.role.hasPermission(perm2));
		assertTrue(this.role.getPermissions().contains(perm2));
		assertThat(this.role.getPermissions().size(), is(2));
	}

	@Test
	public void testCanRemovePermission() {
		Permission perm1 = new PermissionImpl("perm1", "description 1");

		this.role.addPermission(perm1);
		assertTrue(this.role.hasPermission(perm1));

		this.role.removePermission(perm1);
		assertFalse(this.role.hasPermission(perm1));
		assertFalse(this.role.getPermissions().contains(perm1));
		assertThat(this.role.getPermissions().size(), is(0));
	}
}
