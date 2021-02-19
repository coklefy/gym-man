package gymman.employees;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import gymman.common.DuplicateEntityException;
import gymman.common.MemoryRepository;
import gymman.common.SearchUtils;

public class EmployeeRepositoryImpl extends MemoryRepository<Employee> implements EmployeeRepository {

    @Override
    public void add(Employee employee) throws DuplicateEntityException {
        Optional<Employee> existing = this.items.stream()
            .filter(e -> (e.getFiscalCode().equals(employee.getFiscalCode())
                || e.getUsername().equals(employee.getUsername()))
                && !e.getId().equals(employee.getId())
            )
			.findFirst();

		if (existing.isPresent()) {
			throw new DuplicateEntityException(employee.toString(), existing.get().toString());
		}

		super.add(employee);
    }

    @Override
    public List<Employee> searchByName(String name) {
        return this.items.stream()
            .filter(e -> SearchUtils.containsAllWordsCaseInsensitive(e.getFirstname() + " " + e.getLastname(), name))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Employee> getByUsername(String username) {
        return this.items.stream()
            .filter(e -> e.getUsername().equals(username))
            .findFirst();
    }
}
