package gymman.ui.selectable_list;

public interface SelectionChangeHandler<T> {
    void handle(ListItem<T> item);
}
