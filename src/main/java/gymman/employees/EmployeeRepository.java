package gymman.employees;

import java.util.List;
import java.util.Optional;

import gymman.common.Repository;

public interface EmployeeRepository extends Repository<Employee> {
    List<Employee> searchByName(String name);
    Optional<Employee> getByUsername(String username);
}
