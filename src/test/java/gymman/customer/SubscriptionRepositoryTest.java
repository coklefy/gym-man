package gymman.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.SubscriptionType;
import gymman.customers.SubscriptionTypeRepository;
import gymman.customers.SubscriptionTypeRepositoryImpl;

public class SubscriptionRepositoryTest {

    SubscriptionTypeRepository repo;

    @Before
    public void setUp() throws Exception {
        this.repo = new SubscriptionTypeRepositoryImpl();
    }

    @Test
    public void testCanAddSubscription() {
        SubscriptionType subscription = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").build();
        try {
            this.repo.add(subscription);
        } catch (DuplicateEntityException e) {
            fail();
        }

        assertThat(this.repo.contains(subscription), is(true));
    }

    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingDuplicate() throws DuplicateEntityException {
        SubscriptionType a = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").build();
        SubscriptionType b = SubscriptionType.builder().name("sala pesi").description("tirare su molti pesi").build();

        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }

        this.repo.add(b);
    }

    @Test
    public void testCanGetExistingSubscriptionByName() throws DuplicateEntityException {
        SubscriptionType subscription = SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(10).build();
        this.repo.add(subscription);
        assertThat(this.repo.getByName(subscription.getName()).isPresent(), is(true));
    }

    @Test(expected = NumberFormatException.class)
    public void testPreventAddingInvalidValuePrice() throws NumberFormatException{
        SubscriptionType.builder().name("sala pesi").description("tirare su pesi").unitPrice(Double.valueOf("val")).build();
    }

}
