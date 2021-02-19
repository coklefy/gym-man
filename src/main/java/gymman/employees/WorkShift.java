package gymman.employees;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public class WorkShift extends BaseEntity {
    @Getter private String employeeId;
    @Getter private Set<DayOfWeek> weekDays;
    @Getter private Optional<LocalDate> date;
    @Getter private LocalTime timeStart;
    @Getter private LocalTime timeEnd;
    @Getter private String comment;

    private WorkShift() {}

    public boolean isRecurring() {
        return this.weekDays.size() > 0;
    }

    @Builder
    private static WorkShift of(
        String id,
        @NonNull String employeeId,
        @Singular Set<DayOfWeek> weekDays,
        LocalDate date,
        @NonNull LocalTime timeStart,
        @NonNull LocalTime timeEnd,
        String comment
    ) {
        WorkShift shift = new WorkShift();

        if (id != null) {
            shift.id = id;
        }

        shift.employeeId = employeeId;
        shift.weekDays = new HashSet<>();
        shift.date = Optional.empty();
        shift.comment = "";

        if (!weekDays.isEmpty() && date != null) {
            throw new InvalidTimeException("Cannot set both weekDay and date");
        }

        if (weekDays.isEmpty() && date == null) {
            throw new InvalidTimeException("One of weekDay or date must be set");
        }

        if (!weekDays.isEmpty()) {
            shift.weekDays = weekDays;
        }

        if (date != null) {
            shift.date = Optional.of(date);
        }

        if (timeEnd.isBefore(timeStart)) {
            throw new InvalidTimeException("End time cannot be before Start time");
        }

        shift.timeStart = timeStart;
        shift.timeEnd = timeEnd;

        if (comment != null) {
            shift.comment = comment;
        }
        
        return shift;
    }
}
