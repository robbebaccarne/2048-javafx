package sample;

import javafx.scene.paint.Color;
import javafx.util.Duration;

class Config {
    static final int GRID_SIZE = 4;

    static final Color BACKGROUND_COLOR = Color.web("#BBADA0");
    static final Color EMPTY_TILE_COLOR = Color.web("#cdc1b4");

    static final int TILE_PIXEL_LENGTH = 100;
    static final int TILE_PIXEL_RADIUS = TILE_PIXEL_LENGTH / 12;

    static final int BOARD_PIXEL_PADDING = TILE_PIXEL_LENGTH / 7;
    static final int BOARD_PIXEL_LENGTH = (TILE_PIXEL_LENGTH * GRID_SIZE) + ((GRID_SIZE + 1) * BOARD_PIXEL_PADDING);

    final static Duration ANIMATION_DURATION_FIRST_PART = Duration.millis(125);
    final static Duration ANIMATION_DURATION_SECOND_PART = Duration.millis(125);
    final static Duration ANIMATION_PAUSE_BEFORE_SECOND_PART = ANIMATION_DURATION_FIRST_PART.multiply(0.85);
    final static double ANIMATION_MOVE_EASING = 0.9;

    static class TileDesign {
        static final private TileDesign unknown = new TileDesign(Color.web("#3c3a32"), Color.web("#f9f6f2"), null, null, 5);
        static final private TileDesign[] tileDesigns = {
                new TileDesign(Color.web("#eee4da"), Color.web("#776e65"), null, null, 2),
                new TileDesign(Color.web("#ede0c8"), Color.web("#776e65"), null, null, 2),
                new TileDesign(Color.web("#f2b179"), Color.web("#f9f6f2"), null, null, 2),
                new TileDesign(Color.web("#f59563"), Color.web("#f9f6f2"), null, null, 2),
                new TileDesign(Color.web("#f67c5f"), Color.web("#f9f6f2"), null, null, 2),
                new TileDesign(Color.web("#f65e3b"), Color.web("#f9f6f2"), null, null, 2),
                new TileDesign(Color.web("#edcf72"), Color.web("#f9f6f2"), 0.23810, 0.14286, 2.5),
                new TileDesign(Color.web("#edcc61"), Color.web("#f9f6f2"), 0.31746, 0.19048, 2.5),
                new TileDesign(Color.web("#edc850"), Color.web("#f9f6f2"), 0.39683, 0.23810, 2.5),
                new TileDesign(Color.web("#edc53f"), Color.web("#f9f6f2"), 0.47619, 0.28571, 3),
                new TileDesign(Color.web("#edc22e"), Color.web("#f9f6f2"), 0.55556, 0.33333, 3),
        };

        final Color backColor;
        final Color foreColor;
        final Double outerGlow;
        final Double innerGlow;
        final double sizeFraction;

        TileDesign(Color backColor, Color foreColor, Double outerGlow, Double innerGlow, double sizeFraction) {
            this.backColor = backColor;
            this.foreColor = foreColor;
            this.outerGlow = outerGlow;
            this.innerGlow = innerGlow;
            this.sizeFraction = sizeFraction;
        }

        static TileDesign forValue(int i) {
            if (i < tileDesigns.length)
                return tileDesigns[i];
            else
                return unknown;
        }
    }
}
