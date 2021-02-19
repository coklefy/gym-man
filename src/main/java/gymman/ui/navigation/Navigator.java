package gymman.ui.navigation;

import gymman.ui.Page;

public interface Navigator {
    void navigate(String pageId);
    void navigate(Page page);
    void back();
    void backOr(String pageId);
}
