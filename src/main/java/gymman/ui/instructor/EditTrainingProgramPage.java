package gymman.ui.instructor;

import gymman.ui.AbstractPage;

/**
 *
 * Class used to create the page to edit an existing TrainingProgram.
 *
 */
public class EditTrainingProgramPage extends AbstractPage {

    /**
     * Constructor to create the Editing Training Program interface.
     * Get resources from the FXML file.
     */
    public EditTrainingProgramPage() {
        super(EditTrainingProgramPage.class.getResource("editTrainingProgram.fxml"));
    }

    @Override
    public final String getId() {
        return "editTrainingProgram";
    }

    @Override
    public final String getTitle() {
        return "Edit Training Program";
    }

    @Override
    public final boolean hasMenuEntry() {
        return false;
    }
}

