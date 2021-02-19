package gymman.ui.customers;

import gymman.ui.AbstractPage;

/**
 * The Class AddRegistrationPage.
 */
public class AddRegistrationPage extends AbstractPage {

    /**
     * Instantiates a new adds the registration page.
     */
    public AddRegistrationPage() {
        super(AddRegistrationPage.class.getResource("creazioneIscrizione.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_registration_creation";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Create registration";
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
