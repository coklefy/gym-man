package gymman.customers;

import gymman.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * The Class SubscriptionType implements the concept of subscription to which a customer can sign up.
 */
public final class SubscriptionType extends BaseEntity {

    @Getter private String name;

    @Getter private String description;

    @Getter private double unitPrice;



    /**
     * Instantiates a new subscription type.
     */
    private SubscriptionType() {
        super();
    }

    /**
     * To string.
     * @param id the id of subscription
     * @param name the name of subscription
     * @param description the description of subscription
     * @param unitPrice the monthly price of subscription
     *
     * @return the new subscription
     */
    @Builder
    private static SubscriptionType of(
        final String id,
        final String name,
        final String description,
        final double unitPrice
    ) {
        final SubscriptionType subscription = new SubscriptionType();

        if (id != null) {
            subscription.id = id;
        }

        subscription.name = name;
        subscription.description = description;
        subscription.unitPrice = unitPrice;

        return subscription;
    }

    @Override
    public String toString() {
        return String.format(this.name);
    }
}
