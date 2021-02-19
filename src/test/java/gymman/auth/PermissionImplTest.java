package gymman.auth;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class PermissionImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testShouldBeEqualsWithSameName() {
		Permission perm1 = new PermissionImpl("perm1", "");
		Permission perm2 = new PermissionImpl("perm1", "");

		assertThat(perm1.equals(perm2), is(true));
		assertThat(perm1.hashCode() == perm2.hashCode(), is(true));
	}

	@Test
	public void testShouldNotBeEqualIfNameDiffers() {
		Permission perm1 = new PermissionImpl("perm1", "");
		Permission perm2 = new PermissionImpl("perm2", "");

		assertThat(perm1.equals(perm2), is(false));
		assertThat(perm1.hashCode() == perm2.hashCode(), is(false));
	}

	@Test
	public void testShouldNotBeEqualIfNotSameType() {
		Permission perm1 = new PermissionImpl("perm1", "");
		Object obj = new Object();

		assertThat(perm1.equals(obj), is(false));
		assertThat(perm1.hashCode() == obj.hashCode(), is(false));
	}
}
