package gymman.ui.dashboard;

import gymman.ui.AbstractPage;

public class DashboardPage extends AbstractPage {
    public DashboardPage() {
        super(DashboardPage.class.getResource("Dashboard.fxml"));
    }

    @Override
    public String getId() {
        return "page_dashboard";
    }

    @Override
    public String getTitle() {
        return "Dashboard";
    }

    @Override
    public boolean hasMenuEntry() {
        return true;
    }
}
