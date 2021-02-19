package gymman.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.Customer;
import gymman.customers.CustomerRepository;
import gymman.customers.CustomerRepositoryImpl;
import gymman.customers.InvalidValueException;

public class CustomerRepositoryTest {

    CustomerRepository repo;

    @Before
    public void setUp() throws Exception {
        this.repo = new CustomerRepositoryImpl();
    }

    @Test
    public void testCanAddCustomer() throws InvalidValueException {
        Customer customer = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 01, 01))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        try {
            this.repo.add(customer);
        } catch (DuplicateEntityException e) {
            fail();
        }

        assertThat(this.repo.contains(customer), is(true));
    }

    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingDuplicateFiscalCode() throws DuplicateEntityException, InvalidValueException {
        Customer a = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 01, 01))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        Customer b = Customer.builder()
                .firstname("Luca")
                .lastname("Bianchi")
                .username("lwhite")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1985, 05, 22))
                .email("lucabianchi@gmail.com")
                .telephoneNumber("3474853784")
                .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }

        this.repo.add(b);
    }

    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingDuplicateUserName() throws DuplicateEntityException, InvalidValueException {
        Customer a = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 01, 01))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        Customer b = Customer.builder()
                .firstname("Luca")
                .lastname("Bianchi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1985, 05, 22))
                .email("lucabianchi@gmail.com")
                .telephoneNumber("3474853784")
                .build();

        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }

        this.repo.add(b);
    }

    @Test
    public void testCanGetExistingCustomerByUserName() throws DuplicateEntityException, InvalidValueException {
        Customer customer=Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 01, 01))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .username("mRed").build();
        this.repo.add(customer);
        assertThat(this.repo.getByUserName(customer.getUsername()).isPresent(), is(true));
    }

    @Test (expected = InvalidValueException.class)
    public void testPreventAddingCustomerBornAfterTheCurrentDay() throws InvalidValueException {
        Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(2025, 01, 01))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
    }

    @Test (expected = InvalidValueException.class)
    public void testPreventAddingCustomerWithInvalidFiscalCode() throws InvalidValueException {
        Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("ABC")
                .birthdate(LocalDate.of(1980, 01, 01))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
    }

}
