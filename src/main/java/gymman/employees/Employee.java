package gymman.employees;

import java.time.LocalDate;
import java.util.Locale;
import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

public class Employee extends BaseEntity {
    @Getter private String username;
    @Getter private String firstname;
    @Getter private String lastname;
    @Getter private LocalDate birthdate;
    @Getter private String fiscalCode;
    @Getter private String address;
    @Getter private String phone;

    private Employee() { }

    public String getName() {
        return this.firstname + " " + this.lastname;
    }

    public static boolean isValidUsername(String username) {
        return username != null
            && !username.trim().isEmpty();
    }

    public static boolean isValidName(String name) {
        return name != null
            && !name.trim().isEmpty()
            && name.matches("^[a-zA-Zèéòàùì' ]+$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null
            && phone.matches("^\\+{0,1}([0-9]+ *)+$");
    }

    public static boolean isValidFiscalCode(String code) {
        return code != null
            && code.toUpperCase(Locale.ITALIAN).matches("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$");
    }

    @Builder
    private static Employee of(
        String id,
        String username,
        String firstname,
        String lastname,
        LocalDate birthdate,
        String fiscalCode,
        String address,
        String phone
    ) {
        Employee employee = new Employee();

        if (id != null) {
            employee.id = id;
        }

        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("username");
        }

        if (!isValidName(firstname)) {
            throw new IllegalArgumentException("firstname");
        }

        if (!isValidName(lastname)) {
            throw new IllegalArgumentException("lastname");
        }

        if (phone != null && !isValidPhone(phone)) {
            throw new IllegalArgumentException("phone");
        }

        if (!isValidFiscalCode(fiscalCode)) {
            throw new IllegalArgumentException("fiscalCode");
        }

        employee.username = username;
        employee.firstname = firstname;
        employee.lastname = lastname;
        employee.birthdate = birthdate;
        employee.fiscalCode = fiscalCode;
        employee.address = address;
        employee.phone = phone;

        return employee;
    }
}
