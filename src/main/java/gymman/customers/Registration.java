package gymman.customers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * The Class RegistrationImpl implements the concept of registration.
 */
public final class Registration extends BaseEntity {

    @Getter private String idClient;

    @Getter private SubscriptionType type;

    @Getter private LocalDate signingDate;

    @Getter private int duration;

    @Getter private double discount;

    @Getter private List<AdditionalService> additionalService = new ArrayList<>();

    /**
     * Instantiates a new registration.
     */
    private Registration() {
        super();
    }

    /**
     * Build the registration using Lombok.
     *
     * @param id the id of the registration
     * @param idClient the idClient of the registration
     * @param type the subscription type of the registration
     * @param signingDate the date when the registration starts
     * @param duration the duration of the registration
     * @param discount the discount of the registration
     * @param additionalService the additionalService of the registration
     *
     * @return the new registration
     */
    @Builder
    private static Registration of(
        final String id,
        final String idClient,
        final SubscriptionType type,
        final LocalDate signingDate,
        final int duration,
        final double discount,
        final List<AdditionalService> additionalService
    ) {
        final Registration registration  = new Registration();

        if (id != null) {
            registration.id = id;
        }
        if (!isSigningDateValid(signingDate)) {
            throw new InvalidValueException("giorno di inizio");
        }
        if (!isDurationValid(duration)) {
            throw new InvalidValueException("durata");
        }
        if (!isDiscountValid(discount)) {
            throw new InvalidValueException("sconto");
        }
        registration.idClient = idClient;
        registration.type = type;
        registration.signingDate = signingDate;
        registration.duration = duration;
        registration.discount = discount;
        if (additionalService != null) {
            registration.additionalService = additionalService;
        }

        return registration;
    }

    /**
     * Gets the price.
     *
     * @return the total price of registration including additional services
     */
    public double getPrice() {
        return this.duration
                * (this.additionalService.stream().mapToDouble(e -> e.getPrice()).sum() + this.type.getUnitPrice()
                        * (1 - this.getDiscount() / 100));
    }

    /**
     * Checks if duration is valid.
     *
     * @param duration the duration
     * @return true, if duration is a positive number
     */
    public static boolean isDurationValid(final int duration) {
        return duration > 0;
    }

    /**
     * Checks if discount is valid.
     *
     * @param discount the discount
     * @return true, if discount is valid
     */
    public static boolean isDiscountValid(final double discount) {
        return discount <= 100 || discount >= 0;
    }

    /**
     * Checks if is signing date valid.
     *
     * @param date the date
     * @return true, if signing date follows the current date
     */
    public static boolean isSigningDateValid(final LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    /**
     * Checks if is active.
     *
     * @param date the date
     * @return true, if the registration is still active
     */
    public boolean isActive(final LocalDate date) {
        final Calendar data = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        final Calendar d = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        data.setTime(Date.from(this.getSigningDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        d.setTime(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        data.add(Calendar.MONTH, duration);
        return data.after(d);
    }
}
