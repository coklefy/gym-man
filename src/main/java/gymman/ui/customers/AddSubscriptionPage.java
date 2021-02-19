package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class AddSubscriptionPage.
 */
public class AddSubscriptionPage extends AbstractPage {

    /**
     * Instantiates a new adds the subscription page.
     */
    public AddSubscriptionPage() {
        super(AddSubscriptionPage.class.getResource("creazioneAbbonamento.fxml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "page_subscription_creation";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Create subscription";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}
