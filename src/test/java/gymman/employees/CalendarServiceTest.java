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

public class CalendarServiceTest {

    WorkShiftRepository repo;
    CalendarService cal;
    WorkShift w1;
    WorkShift w2;
    WorkShift w3;
    WorkShift w4;

    @Before
    public void setUp() throws DuplicateEntityException {
        repo = new WorkShiftRepositoryImpl();

        this.w1 = WorkShift.builder()
            .employeeId("abc")
            .weekDay(DayOfWeek.MONDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();
        this.w2 = WorkShift.builder()
            .employeeId("def")
            .weekDay(DayOfWeek.TUESDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();
        this.w3 = WorkShift.builder()
            .employeeId("abc")
            .date(LocalDate.of(2019, 9, 16)) // monday
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();
        this.w4 = WorkShift.builder()
            .employeeId("def")
            .weekDay(DayOfWeek.MONDAY)
            .timeStart(LocalTime.of(8, 0))
            .timeEnd(LocalTime.of(13, 0))
            .build();

        repo.add(w1);
        repo.add(w2);
        repo.add(w3);
        repo.add(w4);

        this.cal = new CalendarService(repo);
    }

    @Test
    public void testCanGetCalendar() {
        List<CalendarDay> days = this.cal.getCalendar(LocalDate.of(2019, 9, 10), 10);

        CalendarDay testDay = days.stream().filter(e -> e.getDate().equals(w3.getDate().get())).findFirst().get();

        assertThat(testDay.getShifts(), hasItems(w1, w3, w4));
    }

    @Test
    public void testCanGetCalendarForEmployee() {
        Employee empl = Employee.builder()
                .id("abc")
                .username("pippo")
                .firstname("pippo")
                .lastname("foo")
                .fiscalCode("aaaaaa00a00a000a")
                .build();

        List<CalendarDay> days = this.cal.getCalendarForEmployee(empl, LocalDate.of(2019, 9, 10), 10);

        CalendarDay testDay = days.stream().filter(e -> e.getDate().equals(w3.getDate().get())).findFirst().get();

        assertThat(testDay.getShifts(), hasItems(w1, w3));
        assertThat(testDay.getShifts(), not(hasItems(w4)));
    }
}
