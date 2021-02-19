package gymman.ui.instructor;

import gymman.ui.AbstractPage;

/**
 * Class to create the interface to compile a new Training Program.
 *
 */
public class CompileTrainingProgramPage extends AbstractPage {

    /**
     * Constructor to create the  Compiling new TrainingProgram interface.
     * Get resources from the FXML file.
     */
    public CompileTrainingProgramPage() {
        super(CompileTrainingProgramPage.class.getResource("compileTrainingProgram.fxml"));
    }

    @Override
    public final String getId() {
        return "page_compileTrainingProgram";

    }

    @Override
    public final String getTitle() {
        return "Compile new training program";
    }

    @Override
    public final boolean hasMenuEntry() {
        return false;
    }
}
