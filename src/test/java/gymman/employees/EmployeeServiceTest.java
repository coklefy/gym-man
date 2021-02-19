package gymman.employees;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.*;
import java.util.Optional;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import gymman.auth.Role;
import gymman.auth.RoleRepository;
import gymman.auth.User;
import gymman.auth.UserRepository;

public class EmployeeServiceTest {

    private EmployeeService serv;
    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private Employee empl1;
    private Employee empl2;
    private User user2;
    private Role role1;

    @Before
    public void setUp() {
        this.empl1 = Employee.builder()
                .username("pippo")
                .firstname("pippo")
                .lastname("pippo")
                .fiscalCode("abcppp00a00a000a")
                .build();

        this.empl2 = Employee.builder()
                .username("pluto")
                .firstname("pluto")
                .lastname("pluto")
                .fiscalCode("defppp00a00a000a")
                .build();

        this.user2 = User.builder()
                .username("pluto")
                .password("foobar1234")
                .role("role1")
                .build();

        this.role1 = Role.builder()
                .name("role1")
                .build();

        this.userRepo = Mockito.mock(UserRepository.class);
        EmployeeRepository employeeRepo = Mockito.mock(EmployeeRepository.class);
        this.roleRepo = Mockito.mock(RoleRepository.class);

        when(employeeRepo.getByUsername(this.empl2.getUsername())).thenReturn(Optional.of(this.empl2));
        when(userRepo.getByUsername(this.user2.getUsername())).thenReturn(Optional.of(this.user2));
        when(roleRepo.getByName(role1.getName())).thenReturn(Optional.of(role1));

        this.serv = new EmployeeService(userRepo, employeeRepo, roleRepo);
    }

    @Test
    public void testShouldAddNewEmployee() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        this.serv.addEmployee(this.empl1, "foobar1234", this.role1);

        Mockito.verify(this.userRepo).add(captor.capture());
        assertThat(captor.getValue().getUsername(), is(this.empl1.getUsername()));
    }

    @Test
    public void testShouldUpdateExistingEmployee() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        this.serv.addEmployee(this.empl2, "foobar1234567", this.role1);

        Mockito.verify(this.userRepo).add(captor.capture());

        User savedUser = captor.getValue();

        assertThat(savedUser.getUsername(), is(this.user2.getUsername()));
        assertThat(savedUser.getId(), is(this.user2.getId()));
        assertThat(savedUser.getPassword(), is(User.getHash("foobar1234567")));
    }

    @Test
    public void testShouldUseExistingPasswordWhenEmpty() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        this.serv.addEmployee(this.empl2, "", this.role1);

        Mockito.verify(this.userRepo).add(captor.capture());

        assertThat(captor.getValue().getPassword(), is(User.getHash("foobar1234")));
    }

    @Test
    public void testShouldReturnRoleByEmployee() {
        Role role = this.serv.getRoleByEmployee(this.empl2);

        assertThat(role, is(this.role1));
    }
}
