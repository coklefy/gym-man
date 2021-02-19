package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class RegistrationPage.
 */
public class RegistrationPage extends AbstractPage {
    /**
     * Instantiates a new registration page.
     */
    public RegistrationPage() {
        super(RegistrationPage.class.getResource("elencoIscrizioni.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_registrations_list";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Iscrizioni";
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
