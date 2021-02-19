package gymman.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public abstract class AbstractPage implements Page {

    private FXMLLoader loader;
    private Optional<Pane> root = Optional.empty();

    @Override
    abstract public String getId();

    @Override
    abstract public String getTitle();

    @Override
    abstract public boolean hasMenuEntry();

    public AbstractPage(URL fxml) {
        this.loader = new FXMLLoader(fxml);
    }

    @Override
    public void setController(Controller controller) {
        this.loader.setController(controller);
    }

    @Override
    public Controller getController() {
        return this.loader.getController();
    }

    @Override
    public Pane getContent() throws IOException {
        if (!this.root.isPresent()) {
            this.root = Optional.of(this.loader.load());
        }
        return this.root.get();
    }

    @Override
    public boolean canNavigateBackTo() {
    	// returning true by default prevents breaking existing Pages
    	return true;
    }
}