package gymman.ui.instructor;

import gymman.ui.AbstractPage;

/**
 * Class used to create the page to upload an existing TrainingProgram.
 *
 */
public class TrainingProgramPage extends AbstractPage {

    /**
     * Constructor to create the  Training Program interface.
     * Get resources from the FXML file.
     */
    public TrainingProgramPage() {
        super(TrainingProgramPage.class.getResource("trainingProgram_form.fxml"));
    }

    @Override
    public final String getId() {
        return "page_trainingProgram";

    }

    @Override
    public final String getTitle() {
        return "Training Program";
    }

    @Override
    public final boolean hasMenuEntry() {
        return false;
    }

}
