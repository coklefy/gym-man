package gymman.ui.roles;

import gymman.ui.AbstractPage;

public class RolesPage extends AbstractPage {
    public RolesPage() {
        super(RolesPage.class.getResource("Roles.fxml"));
    }

    @Override
    public String getId() {
        return "page_role_list";
    }

    @Override
    public String getTitle() {
        return "Ruoli utente";
    }

    @Override
    public boolean hasMenuEntry() {
        return true;
    }
}
