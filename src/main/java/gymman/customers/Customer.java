package gymman.customers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * The Class CostumerImpl implements the concept of customer who goes to gym.
 */
public final class Customer extends BaseEntity {

    /**
     * The Class Gender.
     */
    public enum Gender { M, F }

    @Getter private String username;

    @Getter private String firstname;

    @Getter private String lastname;

    @Getter private Gender gender;

    @Getter private String fiscalCode;

    @Getter private LocalDate birthdate;

    @Getter private String email;

    @Getter private String telephoneNumber;

    /**
     * Instantiates a new customer.
     */
    private Customer() {
        super();
    }

    /**
     * Build the registration using Lombok.
     *
     * @param id the id of customer
     * @param firstname the firstName of the customer
     * @param lastname the lastName  of the customer
     * @param gender the gender of the customer
     * @param birthdate the birth date of the customer
     * @param fiscalCode the fiscalCode of the customer
     * @param email the email of the customer
     * @param telephoneNumber the telephone number of the customer
     * @param username the userName of the customer
     *
     * @return the new customer
     */
    @Builder
    private static Customer of(
        final String id,
        final String firstname,
        final String lastname,
        final Gender gender,
        final LocalDate birthdate,
        final String fiscalCode,
        final String email,
        final String telephoneNumber,
        final String username
    ) {
        final Customer customer  = new Customer();

        if (id != null) {
            customer.id = id;
        }
        if (!isNameValid(firstname)) {
            throw new InvalidValueException("nome");
        }
        if (!isNameValid(lastname)) {
            throw new InvalidValueException("cognome");
        }

        if (!isBirthdateValid(birthdate)) {
            throw new InvalidValueException("data di nascita");
        }
        if (!isFiscalcodeValid(fiscalCode)) {
            throw new InvalidValueException("codice fiscale");
        }
        if (!isEmailValid(email)) {
            throw new InvalidValueException("email");
        }
        if (!isTelephoneNumberValid(telephoneNumber)) {
            throw new InvalidValueException("numero di telefono");
        }
        customer.firstname = Character.toString(firstname.charAt(0)).toUpperCase(Locale.getDefault()) + firstname.substring(1);
        customer.lastname = Character.toString(lastname.charAt(0)).toUpperCase(Locale.getDefault()) + lastname.substring(1);
        customer.username = username;
        customer.gender = gender;
        customer.birthdate = birthdate;
        customer.fiscalCode = fiscalCode;
        customer.email = email;
        customer.telephoneNumber = telephoneNumber;

        return customer;
    }

    /**
     * Checks if is name valid.
     *
     * @param name the name
     * @return true, if is name valid
     */
    public static boolean isNameValid(final String name) {
        return name.trim().matches("^[a-zA-Zèéòàùì' ]+$");
    }

    /**
     * Checks if birth date is properly formated.
     *
     * @param date the date
     * @return true, if birth date is properly formated
     */
    public static boolean isBirthdateValid(final String date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            final LocalDate parsed = LocalDate.parse(date.trim(), formatter);
            return isBirthdateValid(parsed);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if birth date is valid.
     *
     * @param date the date
     * @return true, if birth date is valid
     */
    public static boolean isBirthdateValid(final LocalDate date) {
    	final int lowerAgeLimit = 1900;
        return date.isBefore(LocalDate.now()) && date.getYear() >= lowerAgeLimit;
    }

    /**
     * Checks if is fiscal code valid.
     *
     * @param fiscalcode the fiscal code
     * @return true, if fiscal code is valid
     */
    public static boolean isFiscalcodeValid(final String fiscalcode) {
        return fiscalcode.trim().matches("[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]");
    }

    /**
     * Checks if email is valid.
     *
     * @param email the email
     * @return true, if email is valid
     */
    public static boolean isEmailValid(final String email) {
        return email.trim().matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
    }

    /**
     * Checks if telephone number is valid.
     *
     * @param telephoneNumber the telephone number
     * @return true, if telephone number is valid
     */
    public static boolean isTelephoneNumberValid(final String telephoneNumber) {
        return telephoneNumber.trim().matches("[0-9]{10}");
    }
}
