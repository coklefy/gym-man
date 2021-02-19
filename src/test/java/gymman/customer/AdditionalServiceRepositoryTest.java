package gymman.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.AdditionalService;
import gymman.customers.AdditionalServiceRepository;
import gymman.customers.AdditionalServiceRepositoryImpl;

public class AdditionalServiceRepositoryTest {

    AdditionalServiceRepository repo;

    @Before
    public void setUp() throws Exception {
        this.repo = new AdditionalServiceRepositoryImpl();
    }

    @Test
    public void testCanAddAdditionalService() {
        AdditionalService addService=AdditionalService.builder().name("sauna").description("rilassarsi col vapore caldo").price(10).build();
        try {
            this.repo.add(addService);
        } catch (DuplicateEntityException e) {
            fail();
        }

        assertThat(this.repo.contains(addService), is(true));
    }

    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingDuplicate() throws DuplicateEntityException {
        AdditionalService addService1 = AdditionalService.builder().name("sauna").description("rilassarci col vapore caldo").price(10).build();
        AdditionalService addService2 = AdditionalService.builder().name("sauna").description("rilassarsi").price(10).build();

        try {
            this.repo.add(addService1);
        } catch (DuplicateEntityException e) {
            fail();
        }

        this.repo.add(addService2);
    }

    @Test
    public void testCanGetExistingSubscriptionByName() throws DuplicateEntityException {
        AdditionalService addService=AdditionalService.builder().name("sauna").description("rilassarsi col vapore caldo").price(10).build();
        this.repo.add(addService);
        assertThat(this.repo.getByName(addService.getName()).isPresent(), is(true));
    }

    @Test(expected = NumberFormatException.class)
    public void testPreventAddingInvalidValuePrice() throws NumberFormatException{
        AdditionalService.builder().name("sauna").description("rilassarsi col vapore caldo").price(Double.valueOf("fdfsd")).build();
    }
}
