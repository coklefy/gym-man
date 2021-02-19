package gymman.infoclient;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.InvalidValueException;

public class BmiRepositoryTest {

    BmiRepository repo;

    @Before
    public void setUp() throws Exception{
        this.repo = new BmiRepository();
    }

    @Test
    public void testCanAddBmi() throws InvalidValueException {
        Bmi bmi = Bmi.builder()
                .weight(80.2)
                .height(1.80)
                .date(LocalDateTime.of(2019, 1, 1, 10, 10, 10))
                .build();
        try {
            this.repo.add(bmi);
        } catch (DuplicateEntityException e) {
            fail();
        }
        assertThat(this.repo.contains(bmi), is(true));
    }

    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingDuplicateDate() throws DuplicateEntityException, InvalidValueException {
        Bmi a = Bmi.builder()
                .weight(80.2)
                .height(1.80)
                .date(LocalDateTime.of(2019, 10, 14, 19, 30, 40))
                .build();
        Bmi b = Bmi.builder()
                .weight(80.2)
                .height(1.80)
                .date(LocalDateTime.of(2019, 10, 14, 19, 30, 40))
                .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
        this.repo.add(b);
    }

    @Test(expected = InvalidValueException.class)
    public void testPreventAddingInvalidWeight() throws InvalidValueException {
        Bmi a = Bmi.builder()
                .weight((double) 400)
                .height(1.80)
                .date(LocalDateTime.of(2019, 10, 14, 19, 30, 40))
                .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testPreventAddingInvalidHeight() throws InvalidValueException {
        Bmi a = Bmi.builder()
                .weight(80.5)
                .height((double)3)
                .date(LocalDateTime.of(2019, 10, 14, 19, 30, 40))
                .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testPreventAddingFutureDate() throws InvalidValueException {
        Bmi a = Bmi.builder()
                .weight(80.0)
                .height(1.80)
                .date(LocalDateTime.of(2029, 10, 14, 19, 30, 40))
                .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
    }
}

