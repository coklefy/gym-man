package gymman.ui.calendar;

import javafx.scene.paint.Color;

public class ColorGenerator {
    private ColorGenerator() {}

    public static Color colorFrom(int hash) {
        return Color.hsb(hash % 360, 0.40, 0.80);
    }
}