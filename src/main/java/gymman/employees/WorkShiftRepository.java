package gymman.employees;

import java.time.LocalDate;
import java.util.List;

import gymman.common.Repository;

public interface WorkShiftRepository extends Repository<WorkShift> {
    List<WorkShift> getByEmployee(String employeeId);
    List<WorkShift> getByDate(LocalDate date);
}
