package gymman.ui.login;

import gymman.ui.AbstractPage;

public class LoginPage extends AbstractPage {
    public LoginPage() {
        super(LoginPage.class.getResource("Login.fxml"));
    }

    @Override
    public String getId() {
        return "page_login";
    }

    @Override
    public String getTitle() {
        return "Login";
    }

    @Override
    public boolean hasMenuEntry() {
        return false;
    }

    @Override
    public boolean canNavigateBackTo() {
    	return false;
    }
}
