package gymman.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.Customer;
import gymman.customers.InvalidValueException;
import gymman.customers.Registration;
import gymman.customers.RegistrationRepository;
import gymman.customers.RegistrationRepositoryImpl;
import gymman.customers.SubscriptionType;

public class RegistrationRepositoryTest {

    private RegistrationRepository repo;


    @Before
    public void setUp() throws Exception {
        this.repo = new RegistrationRepositoryImpl();
    }

    @Test
    public void testCanAddRegistration() throws InvalidValueException {
        Customer customer = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 1, 1))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        SubscriptionType subscription = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(10).build();
        Registration registration = Registration.builder()
                .idClient(customer.getId())
                .type(subscription)
                .signingDate(LocalDate.of(2030, 07, 10))
                .duration(4)
                .discount(10)
                .additionalService(null).build();
        try {
            this.repo.add(registration);
        } catch (DuplicateEntityException e) {
            fail();
        }

        assertThat(this.repo.contains(registration), is(true));
    }

    @Test
    public void testCanAddRegistrationWithDifferentSubscriptionInTheSamePeriod() throws InvalidValueException {
        Customer customer = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 1, 1))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        SubscriptionType subscriptionA = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(10).build();
        SubscriptionType subscriptionB = SubscriptionType.builder().name("zumba").description("balla e salta").unitPrice(8).build();
        Registration registrationA = Registration.builder()
                .idClient(customer.getId())
                .type(subscriptionA)
                .signingDate(LocalDate.of(2020, 7, 10))
                .duration(4)
                .discount(10)
                .additionalService(null).build();
        Registration registrationB = Registration.builder()
                .idClient(customer.getId())
                .type(subscriptionB)
                .signingDate(LocalDate.of(2020, 7, 10))
                .duration(4)
                .discount(10)
                .additionalService(null).build();
        try {
            this.repo.add(registrationA);
            this.repo.add(registrationB);
        } catch (DuplicateEntityException e) {
            fail();
        }

        assertThat(this.repo.contains(registrationB), is(true));
    }


    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingRegistrationWithSameSubscriptionTypeInTheSamePeriod() throws DuplicateEntityException, InvalidValueException {
        Customer customer = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 1, 1))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        SubscriptionType subscription = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(10).build();
        Registration registrationA = Registration.builder()
                .idClient(customer.getId())
                .type(subscription)
                .signingDate(LocalDate.of(2020, 1, 1))
                .duration(4)
                .discount(10)
                .additionalService(null).build();
        Registration registrationB = Registration.builder()
                .idClient(customer.getId())
                .type(subscription)
                .signingDate(LocalDate.of(2020, 2, 2))
                .duration(4)
                .discount(10)
                .additionalService(null).build();
        this.repo.add(registrationA);
        this.repo.add(registrationB);
    }

    @Test (expected = InvalidValueException.class)
    public void testPreventAddRegistrationWithSigningDatePriorToTheCurrentDay() throws InvalidValueException, DuplicateEntityException {
        Customer customer = Customer.builder()
                .firstname("Mario")
                .lastname("Rossi")
                .username("mRed")
                .fiscalCode("RSSMRA80A01H199L")
                .birthdate(LocalDate.of(1980, 1, 1))
                .email("mariorossi@gmail.com")
                .telephoneNumber("3312468247")
                .build();
        SubscriptionType subscription = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(10).build();
        Registration.builder()
                .idClient(customer.getId())
                .type(subscription)
                .signingDate(LocalDate.of(2000, 1, 1))
                .duration(4)
                .discount(10)
                .additionalService(null).build();
    }

}
