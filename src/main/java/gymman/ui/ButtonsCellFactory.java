package gymman.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ButtonsCellFactory<T> {

    private int padding;
    private int spacing;
    private List<ButtonFactory> buttonFactories = new LinkedList<>();

    public ButtonsCellFactory() {
        this(0, 0);
    }

    public ButtonsCellFactory(int padding, int spacing) {
        this.padding = padding;
        this.spacing = spacing;
    }

    public void addButton(String label, Consumer<T> onClick) {
        this.addButton(label, true, onClick);
    }

    public void addButton(String label, boolean enabled, Consumer<T> onClick) {
        this.buttonFactories.add(new ButtonFactory(label, enabled, onClick));
    }

    public Callback<TableColumn<T,Void>, TableCell<T, Void>> getFactory() {
        return col -> new TableCell<T, Void>() {
            @Override
            public void updateItem(Void item, boolean empty) {
                if (empty) {
                    this.setGraphic(null);
                } else {
                    final T tableItem = this.getTableView().getItems().get(this.getIndex());
                    this.setGraphic(getButtonsForItem(tableItem));
                }
            }
        };
    }

    private HBox getButtonsForItem(T item) {
        final HBox buttons = new HBox();
        buttons.setPadding(new Insets(padding));
        buttons.setSpacing(spacing);

        buttons.getChildren().setAll(
            this.buttonFactories.stream()
                .map(e -> e.getButtonForItem(item))
                .collect(Collectors.toList())
        );

        return buttons;
    }

    private class ButtonFactory {
        private String label;
        private boolean enabled;
        private Consumer<T> onClick;

        public ButtonFactory(String label, boolean enabled, Consumer<T> onClick) {
            this.label = label;
            this.enabled = enabled;
            this.onClick = onClick;
        }

        public Button getButtonForItem(T item) {
            final Button btn = new Button(this.label);
            btn.setDisable(!enabled);
            btn.setOnMouseClicked(event -> this.onClick.accept(item));
            return btn;
        }
    }

}
