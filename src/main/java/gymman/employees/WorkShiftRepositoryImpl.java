package gymman.employees;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import gymman.common.MemoryRepository;

public class WorkShiftRepositoryImpl extends MemoryRepository<WorkShift>
        implements WorkShiftRepository {

    @Override
    public List<WorkShift> getByEmployee(String employeeId) {
        return this.items.stream()
            .filter(e -> e.getEmployeeId().equals(employeeId))
            .collect(Collectors.toList());
    }

    @Override
    public List<WorkShift> getByDate(LocalDate date) {
        return this.items.stream()
            .filter(e ->
                (!e.isRecurring() && e.getDate().get().equals(date))
                || (e.isRecurring() && e.getWeekDays().contains(date.getDayOfWeek()))
            )
            .collect(Collectors.toList());
    }

}
