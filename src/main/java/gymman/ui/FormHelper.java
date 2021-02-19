package gymman.ui;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class FormHelper {

    public static boolean isFilledOut(Node form) {
        return form.lookupAll("TextField").stream()
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .map(e -> !e.getText().trim().isEmpty())
                .reduce(true, (a, b) -> a = a && b);
    }

    public static void highlightWrongField(Control cont) {
        cont.setStyle("-fx-border-color: red");
    }

    public static boolean isBlank(final TextField text) {
        return text.getText().trim().isEmpty();
    }

    public static String getTextTrimmed(final TextField text) {
        return text.getText().trim();
    }
}
