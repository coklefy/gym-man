package gymman.employees;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;

import static org.hamcrest.CoreMatchers.*;

public class WorkShiftRepositoryTest {

    WorkShiftRepository repo;

    @Before
    public void setUp() {
        this.repo = new WorkShiftRepositoryImpl();
    }

    @Test
    public void testCanGetByEmployee() throws DuplicateEntityException {
        WorkShift w1 = WorkShift.builder()
            .employeeId("abc")
            .weekDay(DayOfWeek.MONDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();
        WorkShift w2 = WorkShift.builder()
            .employeeId("def")
            .weekDay(DayOfWeek.TUESDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();

        this.repo.add(w1);
        this.repo.add(w2);

        assertThat(this.repo.getByEmployee("abc"), hasItem(w1));
        assertThat(this.repo.getByEmployee("def"), hasItem(w2));
        assertThat(this.repo.getByEmployee("ghi"), not(hasItem(w1)));
    }

    @Test
    public void testCanGetByDate() throws DuplicateEntityException {
        WorkShift w1 = WorkShift.builder()
            .employeeId("abc")
            .weekDay(DayOfWeek.MONDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();
        WorkShift w2 = WorkShift.builder()
            .employeeId("def")
            .weekDay(DayOfWeek.TUESDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();
        WorkShift w3 = WorkShift.builder()
            .employeeId("abc")
            .date(LocalDate.of(2019, 9, 16)) // monday
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();

        this.repo.add(w1);
        this.repo.add(w2);
        this.repo.add(w3);

        List<WorkShift> shifts = this.repo.getByDate(w3.getDate().get());

        assertThat(shifts, hasItem(w3));
        assertThat(shifts, hasItem(w1)); // because it's recurring on monday
        assertThat(shifts, not(hasItem(w2)));
    }
}
