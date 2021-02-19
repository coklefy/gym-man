package gymman.ui.roles;

import gymman.ui.AbstractPage;

public class NewRolePage extends AbstractPage {
    public NewRolePage() {
        super(NewRolePage.class.getResource("NewRole.fxml"));
    }

    @Override
    public String getId() {
        return "page_role_edit";
    }

    @Override
    public String getTitle() {
        return "New Role";
    }

    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}
