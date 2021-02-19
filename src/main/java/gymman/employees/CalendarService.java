package gymman.employees;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Manager for the calendar. Handles getting the formatted calendar and filtering.
 */
public class CalendarService {
    private WorkShiftRepository shiftRepo;
    
    /**
     * CalendarService constructor
     * 
     * @param shiftRepo ShiftRepository dependency
     */
    public CalendarService(WorkShiftRepository shiftRepo) {
        this.shiftRepo = shiftRepo;
    }

    public List<CalendarDay> getCalendar(LocalDate start, int days) {
        return this.getCalendarFiltered(start, days, (w) -> true);
    }

    public List<CalendarDay> getWeekCalendar(LocalDate start) {
        return this.getCalendarFiltered(start, 7, (w) -> true);
    }

    public List<CalendarDay> getCalendarForEmployee(Employee employee, LocalDate start, int days) {
        return this.getCalendarFiltered(start, days,
                w -> w.getEmployeeId().equals(employee.getId()));
    }

    private List<CalendarDay> getCalendarFiltered(LocalDate start, int days, Predicate<? super WorkShift> filter) {
        return IntStream.range(0, days)
            .mapToObj(e -> start.plusDays(e))
            .map(d ->
                CalendarDay.builder()
                    .date(d)
                    .shifts(
                        shiftRepo.getByDate(d).stream()
                            .filter(filter)
                            .collect(Collectors.toList())
                    )
                    .build()
            )
            .collect(Collectors.toList());
    }
}
