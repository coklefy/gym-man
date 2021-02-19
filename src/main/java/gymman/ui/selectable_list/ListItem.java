package gymman.ui.selectable_list;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ListItem<T> extends AnchorPane {

    public enum State {
        DESELECTED, SELECTED
    }

    private Label label;
    private Pane btnSelect;
    private Pane btnRemove;

    private State state;
    private T item;
    private SelectionChangeHandler<T> onSelectHandler;
    private SelectionChangeHandler<T> onRemoveHandler;

    public ListItem(final T item, final State state) {
        this.item = item;
        this.state = state;

        getStyleClass().add("item");

        this.label = new Label(item.toString());
        
        this.label.setMaxWidth(360);
        this.label.setEllipsisString("...");
        Tooltip tooltip = new Tooltip(item.toString());
        this.label.setTooltip(tooltip);
        setTopAnchor(this.label, 0.0);
        setBottomAnchor(this.label, 0.0);
        setLeftAnchor(this.label, 0.0);

        this.btnSelect = new StackPane(new FontAwesomeIconView(FontAwesomeIcon.PLUS));
        this.btnSelect.getStyleClass().add("item__button");
        this.btnSelect.getStyleClass().add("item__button--select");
        this.btnSelect.setPrefWidth(20.0);
        this.btnSelect.setPrefHeight(20.0);
        this.btnSelect.setOnMouseClicked(e -> this.onItemSelected());
        setTopAnchor(this.btnSelect, 0.0);
        setBottomAnchor(this.btnSelect, 0.0);
        setRightAnchor(this.btnSelect, 0.0);

        this.btnRemove = new StackPane(new FontAwesomeIconView(FontAwesomeIcon.MINUS));
        this.btnRemove.getStyleClass().add("item__button");
        this.btnRemove.getStyleClass().add("item__button--remove");
        this.btnRemove.setPrefWidth(20.0);
        this.btnRemove.setPrefHeight(20.0);
        this.btnRemove.setOnMouseClicked(e -> this.onItemRemoved());
        setTopAnchor(this.btnRemove, 0.0);
        setBottomAnchor(this.btnRemove, 0.0);
        setRightAnchor(this.btnRemove, 0.0);
        
        getChildren().add(this.label);
        getChildren().add(this.btnSelect);
        getChildren().add(this.btnRemove);

        switch (state) {
            case DESELECTED:
                this.remove();
                break;
            case SELECTED:
                this.select();
        }
    }

    public State getCurrentState() {
        return this.state;
    }

    public T getItem() {
        return this.item;
    }

    public void select() {
        this.btnRemove.setMouseTransparent(false);
        this.btnSelect.setMouseTransparent(true);
        this.getStyleClass().add("item--selected");
    }

    public void remove() {
        this.btnRemove.setMouseTransparent(true);
        this.btnSelect.setMouseTransparent(false);
        this.getStyleClass().remove("item--selected");
    }

    public void onSelect(final SelectionChangeHandler<T> handler) {
        this.onSelectHandler = handler;

    }

    public void onRemove(final SelectionChangeHandler<T> handler) {
        this.onRemoveHandler = handler;

    }

    private void onItemSelected() {
        this.onSelectHandler.handle(this);
        this.select();
    }

    private void onItemRemoved() {
        this.onRemoveHandler.handle(this);
        this.remove();
    }
}
