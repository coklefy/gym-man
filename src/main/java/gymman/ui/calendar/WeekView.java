package gymman.ui.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public final class WeekView extends ScrollPane implements CalendarView {

    private List<CalendarEntry> entries = new ArrayList<>();
    private final int NUM_COLS = 8;
    private final int NUM_ROWS = 25;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public WeekView() {
        getStylesheets().add(WeekView.class.getResource("WeekView.css").toExternalForm());
        getStyleClass().add(".calendar-week-view");
        setFitToHeight(true);
        setFitToWidth(true);
    }

    @Override
    public void setEntries(List<CalendarEntry> entries) {
        this.entries = entries;
        this.refresh();
    }

    @Override
    public void refresh() {
        getChildren().clear();
        setContent(buildGrid());
    }

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        Map<Coords, HBox> cells = new HashMap<>();

        grid.setPrefSize(GridPane.REMAINING, GridPane.REMAINING);
        
        for (int i = 0; i <= NUM_COLS; i++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(100 / NUM_COLS);
            constraint.setMinWidth(100);
            grid.getColumnConstraints().add(constraint);
        }
        for (int i = 0; i <= NUM_ROWS; i++) {
            RowConstraints constraint = new RowConstraints();
            constraint.setMinHeight(50);
            constraint.setPrefHeight(100);
            grid.getRowConstraints().add(constraint);
        }

        LocalDate startDate = getStartDate();
        LocalTime startTime = LocalTime.of(0, 0);

        for (int i = 1; i < NUM_COLS; i++) {
            grid.add(buildDateCell(startDate.plusDays(i), dateFormatter, Pos.BOTTOM_CENTER), i, 0);
        }

        for (int i = 1; i < NUM_ROWS; i++) {
            grid.add(buildDateCell(startTime.plusHours(i), timeFormatter, Pos.CENTER_RIGHT), 0, i);
        }

        for (int x = 1; x < NUM_COLS; x++) {
            for (int y = 1; y < NUM_ROWS; y++) {
                HBox cell = emptyCell();
                cells.put(new Coords(x, y), cell);
                grid.add(cell, x, y);
            }
        }

        for (CalendarEntry entry : this.entries) {
            entry.setPadding(new Insets(4));
            entry.setMinWidth(40);
            cells.get(new Coords(entry.getDate().getDayOfWeek().getValue(), entry.getTime().getHour()))
                    .getChildren()
                    .add(entry);
        }

        return grid;
    }

    private LocalDate getStartDate() {
        Optional<LocalDate> fromEntries = this.entries.stream()
                .map(e -> e.getDate())
                .min((a,b) -> a.compareTo(b));
        
        if (!fromEntries.isPresent()) {
            return LocalDate.now();
        }

        return fromEntries.get();
    }

    private VBox buildDateCell(TemporalAccessor date, DateTimeFormatter formatter, Pos labelPosition) {
        VBox box = new VBox();
        box.getStyleClass().add("calendar-cell");
        box.setAlignment(labelPosition);
        box.getChildren().add(new Label(formatter.format(date)));
        return box;
    }

    private HBox emptyCell() {
        HBox box = new HBox();
        box.getStyleClass().add("calendar-cell");
        return box;
    }

    private class Coords {
        public Integer x;
        public Integer y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return 17 + (31 * x) + (31 * y);
        }

        @Override
        public boolean equals(Object other) {
            if (!Coords.class.isInstance(other)) {
                return false;
            }
            Coords otherCoords = Coords.class.cast(other);
            return x == otherCoords.x && y == otherCoords.y;
        }
    }
}
