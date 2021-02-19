package gymman.employees;

import org.junit.Test;

public class EmployeeTest {
    @Test(expected=IllegalArgumentException.class)
    public void testUsernameIsMandatory() {
        Employee.builder()
            .firstname("pippo")
            .lastname("pippo")
            .fiscalCode("abc")
            .build();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNameIsMandatory() {
        Employee.builder()
            .username("pippo")
            .fiscalCode("abc")
            .build();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFiscalCodeIsMandatory() {
        Employee.builder()
            .username("pippo")
            .firstname("pippo")
            .lastname("pippo")
            .build();
    }
}