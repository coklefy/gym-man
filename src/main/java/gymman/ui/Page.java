package gymman.ui;

import java.io.IOException;

import javafx.scene.layout.Pane;

public interface Page {
    /**
     * Get page ID
     * 
     * @return Page ID
     */
    String getId();
    
    /**
     * Get page title
     * 
     * @return Page title
     */
    String getTitle();

    /**
     * Page should have a menu entry
     * @return true if Page should have a menu entry, false otherwise
     */
    boolean hasMenuEntry();

    /**
     * It should be possible to navigate back to this page
     * 
     * @return true if should be possible to navigate back to this page, false otherwise
     */
    boolean canNavigateBackTo();

    /**
     * Sets the JavaFX controller for this page
     * 
     * @param controller
     */
    void setController(Controller controller);

    /**
     * Gets the controller for this page
     * 
     * @return Controller
     */
    Controller getController();

    /**
     * Gets the JavaFX page content
     * 
     * @return JavaFX Pane containing the page content
     * @throws IOException
     */
    Pane getContent() throws IOException;
}
