package gymman.ui.instructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import gymman.fitness.Exercise;
import gymman.ui.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Class to create the Exercise controller. It will upload the exercises.
 * to the training program interface. Offers methods to be called by
 * TrainingProgramContoller for uploading all the exercises to the view.
 *
 */
public class ExerciseController implements Controller {
    /** exercise container. **/
    @FXML private AnchorPane exerciseCard;
    /** exercise denomination label. **/
    @FXML private Label nameLabel;
    /** exercise category label. **/
    @FXML private Label categoryLabel;
    /** exercise type label. **/
    @FXML private Label typeLabel;
    /** exercise execution metric label. **/
    @FXML private Label metricLabel;
    /** exercise demo image.  **/
    @FXML private ImageView exerciseImage;


    /**
     * Method to be called by TrainingProgramController.
     * It is used to upload the Exercise info into the container.
     * The container will be loaded by reading an FXML file.
     *
     * @param exercise : exercise that have to be uploaded to the view
     * @return AnchorPane that is the container of the exercise.
     */
    public AnchorPane loadExercises(final Exercise exercise) {
        try {
            this.exerciseCard = (AnchorPane) FXMLLoader.load(
                    ExerciseController.class.getResource("exercise_container.fxml"));
        } catch (IOException e) {
            //e.printStackTrace();
        }

        initialiseExercisePane(getContainerNodes(this.exerciseCard));
        uploadDataToExercisePane(exercise);

        return exerciseCard;
    }


    private void initialiseExercisePane(final Map<String, Node> nodesMap) {
        this.exerciseImage = (ImageView) nodesMap.entrySet().stream().filter(node -> node.getKey().equals("exerciseImage")).map(Map.Entry::getValue).findFirst().orElse(null);
        this.nameLabel = (Label) nodesMap.entrySet().stream().filter(node -> node.getKey().equals("nameLabel")).map(Map.Entry::getValue).findFirst().orElse(null);
        this.categoryLabel = (Label) nodesMap.entrySet().stream().filter(node -> node.getKey().equals("categoryLabel")).map(Map.Entry::getValue).findFirst().orElse(null);
        this.typeLabel = (Label) nodesMap.entrySet().stream().filter(node -> node.getKey().equals("typeLabel")).map(Map.Entry::getValue).findFirst().orElse(null);
        this.metricLabel = (Label) nodesMap.entrySet().stream().filter(node -> node.getKey().equals("metricLabel")).map(Map.Entry::getValue).findFirst().orElse(null);
    }


    private Map<String, Node> getContainerNodes(final Pane container) {
        final Map<String, Node> components = new HashMap<>();

        container.getChildren().forEach((component) -> {
            if (component.getId() != null) {
                components.put(component.getId(), component);
            }
        });
        return components;
    }

    private void uploadDataToExercisePane(final Exercise exercise) {
        this.nameLabel.setText(exercise.getName());
        this.categoryLabel.setText(exercise.getCategory().toString());
        this.typeLabel.setText(exercise.getCategory().getType().toString());
        this.metricLabel.setText(exercise.getExecutionMetric().getMetricType().toString());
    }


	@Override
	public void onDisplay() {
		// TODO Auto-generated method stub

	}

}
