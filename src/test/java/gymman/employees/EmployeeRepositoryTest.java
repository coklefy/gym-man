package gymman.employees;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import gymman.common.DuplicateEntityException;

public class EmployeeRepositoryTest {

    private EmployeeRepository repo;

    @Before
    public void setUp() {
        this.repo = new EmployeeRepositoryImpl();
    }

    @Test(expected = DuplicateEntityException.class)
    public void testErrorWhenAddingEntityWithSameFiscalCode()
            throws DuplicateEntityException {
        Employee empl = Employee.builder().username("pippo").firstname("pippo").lastname("pippo").fiscalCode("abcppp00a00a000a").build();
        Employee empl2 = Employee.builder().username("pluto").firstname("pluto").lastname("pluto").fiscalCode("abcppp00a00a000a").build();

        this.repo.add(empl);
        this.repo.add(empl2);
    }

    @Test(expected = DuplicateEntityException.class)
    public void testErrorWhenAddingEntityWithSameUsername()
            throws DuplicateEntityException {
        Employee empl = Employee.builder().firstname("pippo").lastname("pippo").fiscalCode("pppppp00a00a000a").username("abc").build();
        Employee empl2 = Employee.builder().firstname("pluto").lastname("pluto").fiscalCode("pltppp00a00a000a").username("abc").build();

        this.repo.add(empl);
        this.repo.add(empl2);
    }

    @Test
    public void testSearchByName() throws DuplicateEntityException {
        Employee empl = Employee.builder().username("pippo").firstname("pippo").lastname("foo").fiscalCode("abcppp00a00a000a").build();
        Employee empl2 = Employee.builder().username("pluto").firstname("pluto").lastname("bar").fiscalCode("defppp00a00a000a").build();

        this.repo.add(empl);
        this.repo.add(empl2);

        assertThat(this.repo.searchByName("pip"), hasItem(empl));
        assertThat(this.repo.searchByName("fo"), hasItem(empl));
        assertThat(this.repo.searchByName("ar"), hasItem(empl2));
        assertThat(this.repo.searchByName("pluto b"), hasItem(empl2));
        assertThat(this.repo.searchByName("foo pi"), hasItem(empl));
        assertThat(this.repo.searchByName("p"), hasItems(empl, empl2));
        assertThat(this.repo.searchByName("pippo pluto").isEmpty(), is(true));
        assertThat(
            this.repo.searchByName("p").contains(empl)
            && this.repo.searchByName("p").contains(empl2),
            is(true)
        );
    }

    @Test
    public void testGetByUsernameReturnsEmptyOptionalWhenNotFound() {
        Optional<Employee> employee = this.repo.getByUsername("pippo");

        assertThat(employee.isPresent(), is(false));
    }

    @Test
    public void testCanGetEmployeeByUsername() {
        Employee empl = Employee.builder().username("pippo").firstname("pippo").lastname("foo").fiscalCode("abcppp00a00a000a").build();
        this.repo.add(empl);

        Optional<Employee> res = this.repo.getByUsername(empl.getUsername());

        assertThat(res.isPresent(), is(true));
        assertThat(res.get(), is(empl));
    }
}
