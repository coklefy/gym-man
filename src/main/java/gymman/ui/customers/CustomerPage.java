package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class CustomerPage.
 */
public class CustomerPage extends AbstractPage {

    /**
     * Instantiates a new customer page.
     */
    public CustomerPage() {
        super(CustomerPage.class.getResource("elencoClienti.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_customers_list";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Clienti";
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
