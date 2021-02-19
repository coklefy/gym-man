package gymman.ui.customers;


import gymman.ui.AbstractPage;

/**
 * The Class AdditionalServicePage.
 */
public class AdditionalServicePage extends AbstractPage {

    /**
     * Instantiates a new additional service page.
     */
    public AdditionalServicePage() {
        super(AdditionalServicePage.class.getResource("elencoServiziAggiuntivi.fxml"));
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return "page_additionalService_list";
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return "Servizi aggiuntivi";
    }

    /**
     * Checks for menu entry.
     *
     * @return true, if successful
     */
    @Override
    public boolean hasMenuEntry() {
        return true;
    }

}
