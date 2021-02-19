package gymman.ui.navigation;

import java.util.Optional;
import java.util.Set;
import gymman.ui.Page;

public interface NavigationService extends Navigator {
    void registerPage(Page page);
    Set<Page> getPages();
    Optional<Page> findById(String id);
    Page getCurrentPage();
    void addOnPageChangeHandler(PageChangeHandler handler);
    int getHistorySize();
    NavigationParams getNavParams();
}
