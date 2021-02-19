package gymman.ui.tool;

import gymman.ui.AbstractPage;

/**
 * The Class NewToolPage.
 */
public class NewToolPage extends AbstractPage  {

    /**
     * Instantiates a new new tool page.
     */
    public NewToolPage() {
        super(NewToolPage.class.getResource("attrezzi.fxml"));
    }

    @Override
    public String getId() {
        return "page_tool_edit";
    }

    @Override
    public String getTitle() {
        return "Crea Attrezzo";
    }

    @Override
    public boolean hasMenuEntry() {
        return false;
    }
}