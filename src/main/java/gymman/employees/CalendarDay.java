package gymman.employees;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * Represents a work day, containing multiple shifts.
 */
@Builder
public class CalendarDay {
    @Getter private LocalDate date;
    @Getter @Singular private List<WorkShift> shifts;
}
