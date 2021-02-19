package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class AddCustomerPage.
 */
public class AddCustomerPage extends AbstractPage {

    /**
     * Instantiates a new adds the customer page.
     */
    public AddCustomerPage() {
        super(AddCustomerPage.class.getResource("creazioneCliente.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_customer_creation";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Create customer";
    }

    /**
     * Checks for menu entry.
     *
     * @return true, if successful
     */
    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}
