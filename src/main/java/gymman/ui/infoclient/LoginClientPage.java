package gymman.ui.infoclient;

import gymman.ui.AbstractPage;

/**
 * The Class LoginClientPage.
 */
public class LoginClientPage extends AbstractPage {

    /**
     * Instantiates a new login client page.
     */
    public LoginClientPage() {
        super(LoginClientPage.class.getResource("loginUtente.fxml"));
    }

   @Override
    public String getId() {
        return "page_customer_login";
    }

   @Override
    public String getTitle() {
        return "Login Cliente";
    }

   @Override
    public boolean hasMenuEntry() {
        return true;
    }
}
