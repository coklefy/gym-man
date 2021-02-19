package gymman.tool;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


import org.junit.Before;
import org.junit.Test;

import gymman.common.DuplicateEntityException;
import gymman.customers.InvalidValueException;

public class ToolRepositoryTest {

    ToolRepository repo;

    @Before
    public void setUp() throws Exception{
        this.repo = new ToolRepository();
    }

    @Test
    public void testCanAddTool() throws InvalidValueException{
        Tool tool = Tool.builder()
                .name("Tapirulan")
                .desc("Serve Per correre e smaltire peso")
                .maintenance(2)
                .numseriale("ASDASD123ASD2")
                .build();
        try {
            this.repo.add(tool);
        } catch (DuplicateEntityException e) {
            fail();
        }
        assertThat(this.repo.contains(tool), is(true));
    }

    @Test(expected = DuplicateEntityException.class)
    public void testPreventAddingDuplicateNameSerial() throws DuplicateEntityException, InvalidValueException {
        Tool a = Tool.builder()
            .name("Bilanciere")
            .desc("Serve per posizionarci i pesi e sollevarlo")
            .maintenance(2)
            .numseriale("ASDASD123ASD2")
            .build();
        Tool b = Tool.builder()
            .name("Bilanciere")
            .desc("Serve per posizionarci i pesi e sollevarlo")
            .maintenance(2)
            .numseriale("ASDASD123ASD2")
            .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
        this.repo.add(b);
    }

    @Test(expected = InvalidValueException.class)
    public void testPreventAddingWrongMaintenance() throws InvalidValueException {
        Tool a = Tool.builder()
            .name("Bilanciere")
            .desc("Serve per posizionarci i pesi e sollevarlo")
            .maintenance(15)
            .numseriale("ASDASD123ASD2")
            .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testPreventAddingWrongName() throws DuplicateEntityException, InvalidValueException {
        Tool a = Tool.builder()
            .name("Vasd22")
            .desc("Serve per posizionarci i pesi e sollevarlo")
            .maintenance(2)
            .numseriale("ASDASD123ASD2")
            .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testPreventAddingNullName() throws InvalidValueException {
        Tool a = Tool.builder()
            .name(" ")
            .desc(" ")
            .maintenance(2)
            .numseriale(" ")
            .build();
        try {
            this.repo.add(a);
        } catch (DuplicateEntityException e) {
            fail();
        }
    }
}
