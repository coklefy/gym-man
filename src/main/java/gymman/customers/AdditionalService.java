package gymman.customers;

import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;


/**
 * The Class AdditionalService implements the concept of additional service.
 */

public final class AdditionalService extends BaseEntity {

    @Getter private String name;

    @Getter private String idClient;

    @Getter private String description;

    @Getter private double price;

    /**
     * Instantiates a new additional service.
     */
    private AdditionalService() {
        super();
    }

    /**
     * Build the registration using Lombok.
     *
     * @param id the id of the additional service
     * @param name the name of the additional service
     * @param description the description of the additional service
     * @param price the monthly price of the additional service
     *
     * @return the new additional service
     */
    @Builder
    private static AdditionalService of(
        final String id,
        final String name,
        final String description,
        final double price
    ) {
        final AdditionalService service = new AdditionalService();

        if (id != null) {
            service.id = id;
        }

        service.name = name;
        service.description = description;
        service.price = price;

        return service;
    }

    /**
     * To string.
     *
     * @return the string representation of the additional service
     */
    @Override
    public String toString() {
        return String.format("%s %s %s", this.name, this.description, String.valueOf(this.price));
    }

}
