package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class SubscriptionPage.
 */
public class SubscriptionPage extends AbstractPage {

    /**
     * Instantiates a new subscription page.
     */
    public SubscriptionPage() {
        super(SubscriptionPage.class.getResource("elencoAbbonamenti.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_subscription_list";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Abbonamenti";
    }

    /**
     * Checks for menu entry.
     *
     * @return true, if successful
     */
    @Override
    public boolean hasMenuEntry() {
        return true;
    }

}
