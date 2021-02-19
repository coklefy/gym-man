package gymman.common;

import static org.junit.Assert.*;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class MemoryRepositoryTest {
	public DummyEntity dummy1 = new DummyEntity.Builder().withId("123").name("foo").build();
	public DummyEntity dummy1_modified = new DummyEntity.Builder().withId("123").name("bar").build();
	public DummyEntity dummy2 = new DummyEntity.Builder().withId("456").name("foobar").build();
	
	public MemoryRepository<DummyEntity> repo;
	
	@Before
	public void setUp() throws Exception {
		this.repo = new MemoryRepository<DummyEntity>();
	}

	@Test
	public void testCanSaveEntity() {
		assertEquals(0, this.repo.getCount());
		
		try {
			this.repo.add(dummy1);
		} catch (DuplicateEntityException e) {
			fail();
		}
		assertThat(this.repo.get("123").get().getId(), is("123"));
		assertTrue(this.repo.contains(dummy1));
		assertTrue(this.repo.containsId("123"));
		assertThat(this.repo.getCount(), is(1));
	}
	
	@Test
	public void testCanRemoveEntity() {
		try {
			this.repo.add(dummy1);
		} catch (DuplicateEntityException e) {
			fail();
		}
		assertTrue(this.repo.contains(dummy1));
		assertThat(this.repo.getCount(), is(1));
		
		this.repo.remove(dummy1);
		assertFalse(this.repo.contains(dummy1));
		assertThat(this.repo.getCount(), is(0));
	}
	
	@Test
	public void testEmptyOptionalOnItemNotFound() {
		Optional<DummyEntity> entity = this.repo.get("123");
		assertThat(entity.isPresent(), is(false));
	}
	
	@Test
	public void testSavingExistingEntityDoesUpdate() {
		try {
			this.repo.add(dummy1);
		} catch (DuplicateEntityException e) {
			fail();
		}
		
		assertEquals("foo", this.repo.get("123").get().getName());
		assertThat(this.repo.getCount(), is(1));
		
		try {
			this.repo.add(dummy1_modified);
		} catch (DuplicateEntityException e) {
			fail();
		}
		
		assertEquals("bar", this.repo.get("123").get().getName());
		assertThat(this.repo.getCount(), is(1));
	}

	@Test
	public void testCanGetAllItems() throws DuplicateEntityException {
		this.repo.add(dummy1);
		this.repo.add(dummy2);

		assertThat(this.repo.getAll().size(), is(2));
	}
}
