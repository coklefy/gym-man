package gymman.auth;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;

public class RoleRepositoryTest {
	RoleRepository repo;

	@Before
	public void setUp() throws Exception {
		this.repo = new RoleRepositoryImpl();
	}

	@Test
	public void testCanAddRole() {
		Role role = Role.builder().name("foo").id("123").build();
		try {
			this.repo.add(role);
		} catch (DuplicateEntityException e) {
			fail();
		}

		assertThat(this.repo.containsId("123"), is(true));
	}

	@Test(expected = DuplicateEntityException.class)
	public void testPreventAddingDuplicate() throws DuplicateEntityException {
		Role a = Role.builder().name("foo").id("123").build();
		Role b = Role.builder().name("foo").id("456").build();

		try {
			this.repo.add(a);
		} catch (DuplicateEntityException e) {
			fail();
		}

		this.repo.add(b);
	}

	@Test
	public void testCanGetExistingRoleByName() throws DuplicateEntityException {
		Role role = Role.builder().name("foo").build();
		this.repo.add(role);
		assertThat(this.repo.getByName(role.getName()).isPresent(), is(true));
	}

	@Test
	public void testEmptyOptionalOnNonexistingName() {
		Optional<Role> role = this.repo.getByName("foo");
		assertThat(role.isPresent(), is(false));
	}

}
