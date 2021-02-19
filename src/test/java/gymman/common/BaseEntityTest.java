package gymman.common;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class BaseEntityTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDefaultConstructorShouldSetId() {
		Entity e = new DummyEntity.Builder().name("pippo").build();
		// System.out.printf("UUID = %s", e.getId());
		assertTrue(e.getId().length() > 0);
	}

	@Test
	public void testCanOverrideId() {
		Entity e = new DummyEntity.Builder().withId("123").name("pippo").build();
		// System.out.printf("UUID = %s", e.getId());
		assertEquals("123", e.getId());
	}

	@Test
	public void testEquals() {
		Entity a = new DummyEntity.Builder().withId("123").name("pippo").build();
		Entity b = new DummyEntity.Builder().withId("456").name("pluto").build();
		Entity c = new DummyEntity.Builder().withId("123").name("pippo").build();
		Object foo = new Object();

		assertThat(a.equals(b), is(false));
		assertThat(a.equals(c), is(true));
		assertThat(a.equals(foo), is(false));
	}
}
