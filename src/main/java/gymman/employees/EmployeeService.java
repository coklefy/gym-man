package gymman.employees;

import java.util.Optional;
import gymman.auth.Role;
import gymman.auth.RoleRepository;
import gymman.auth.User;
import gymman.auth.UserRepository;

public class EmployeeService {
    private final UserRepository userRepo;
    private final EmployeeRepository employeeRepo;
    private final RoleRepository roleRepo;

    public EmployeeService(UserRepository userRepo, EmployeeRepository employeeRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
    }

    public void addEmployee(Employee employee, String password, Role role) {
        Optional<User> existingUser = this.userRepo.getByUsername(employee.getUsername());

        if (!existingUser.isPresent()) {
            handleNewEmployee(employee, password, role);
            return;
        }

        this.handleExistingEmployee(employee, password, role, existingUser.get());
    }

    public Role getRoleByEmployee(Employee employee) {
        User user = this.userRepo.getByUsername(employee.getUsername()).get();
        return this.roleRepo.getByName(user.getRole()).get();
    }

    private void handleNewEmployee(Employee employee, String password, Role role) {
        User user = User.builder()
                .username(employee.getUsername())
                .password(password)
                .role(role.getName())
                .build();

        this.userRepo.add(user);
        this.employeeRepo.add(employee);
    }

    private void handleExistingEmployee(Employee employee, String password, Role role, User existingUser) {
        User.UserBuilder userBuilder = User.builder()
                .id(existingUser.getId())
                .username(employee.getUsername())
                .role(role.getName());

        User user;

        if (password.isEmpty()) {
            user = userBuilder.passwordHash(existingUser.getPassword()).build();
        } else {
            user = userBuilder.password(password).build();
        }

        this.userRepo.add(user);
        this.employeeRepo.add(employee);
    }
}
