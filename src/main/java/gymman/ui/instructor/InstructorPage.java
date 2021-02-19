package gymman.ui.instructor;

import gymman.ui.AbstractPage;

/**
 * Class used to create the page of the instructor.
 *
 */
public class InstructorPage extends AbstractPage {

    /**
     * Constructor to create the  Instructor interface.
     * Get resources from the FXML file.
     */
    public InstructorPage() {
        super(InstructorPage.class.getResource("instructor_form.fxml"));
    }

    @Override
    public final String getId() {
        return "page_instructor";
    }

    @Override
    public final String getTitle() {
        return "Istruttore";
    }

    @Override
    public final boolean hasMenuEntry() {
        return true;
    }
}
