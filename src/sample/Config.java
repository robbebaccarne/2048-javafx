package sample;

import javafx.scene.paint.Color;
import javafx.util.Duration;

class Config {
    static final int GRID_SIZE = 4;

    static final Color BACKGROUND_COLOR = Color.web("#BBADA0");
    static final Color EMPTY_TILE_COLOR = Color.web("#cdc1b4");

    static final int PIXEL_LENGTH = 100;
    static final int TILE_RADIUS = PIXEL_LENGTH / 12;
    static final int PIXEL_PADDING = PIXEL_LENGTH / 7;

    static final int BOARD_PIXEL_LENGTH = (PIXEL_LENGTH * GRID_SIZE) + ((GRID_SIZE + 1) * PIXEL_PADDING);

    final static Duration ANIMATION_DURATION_FIRST_PART = Duration.millis(125);
    final static Duration ANIMATION_DURATION_SECOND_PART = Duration.millis(125);
    final static Duration ANIMATION_PAUSE_BEFORE_SECOND_PART = ANIMATION_DURATION_FIRST_PART.multiply(0.85);
    final static double ANIMATION_MOVE_EASING = 0.9;

    static class TileDesign {
        static final private TileDesign unknown = new TileDesign(Color.web("#3c3a32"), Color.web("#f9f6f2"), 5);
        static final private TileDesign[] tileDesigns = {
                new TileDesign(Color.web("#eee4da"), Color.web("#776e65"), 2),
                new TileDesign(Color.web("#ede0c8"), Color.web("#776e65"), 2),
                new TileDesign(Color.web("#f2b179"), Color.web("#f9f6f2"), 2),
                new TileDesign(Color.web("#f59563"), Color.web("#f9f6f2"), 2),
                new TileDesign(Color.web("#f67c5f"), Color.web("#f9f6f2"), 2),
                new TileDesign(Color.web("#f65e3b"), Color.web("#f9f6f2"), 2),
                new TileDesign(Color.web("#edcf72"), Color.web("#f9f6f2"), 2.5),
                new TileDesign(Color.web("#edcc61"), Color.web("#f9f6f2"), 2.5),
                new TileDesign(Color.web("#edc850"), Color.web("#f9f6f2"), 2.5),
                new TileDesign(Color.web("#edc53f"), Color.web("#f9f6f2"), 3),
                new TileDesign(Color.web("#edc22e"), Color.web("#f9f6f2"), 3),
        };

        final Color backColor;
        final Color foreColor;
        final double sizeFraction;

        TileDesign(Color back, Color fore, double sizeFraction) {
            this.backColor = back;
            this.foreColor = fore;
            this.sizeFraction = sizeFraction;
        }

        static TileDesign forValue(int i) {
            if (i > tileDesigns.length)
                return unknown;
            else
                return tileDesigns[i];
        }
    }
}
