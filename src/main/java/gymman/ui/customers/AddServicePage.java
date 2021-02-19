package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class AddServicePage.
 */
public class AddServicePage extends AbstractPage {

    /**
     * Instantiates a new adds the service page.
     */
    public AddServicePage() {
        super(AddServicePage.class.getResource("creazioneServizioAggiuntivo.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_additionalService_creation";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Create additional service";
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
