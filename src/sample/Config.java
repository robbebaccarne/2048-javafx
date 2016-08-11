package sample;

import javafx.scene.paint.Color;
import javafx.util.Duration;

class Config {

    static final boolean QUICK_MODE = false;

    static final int GRID_SIZE = 4;
    static final int STARTING_TILES = 2;
    static final int WINNING_VALUE = 10;

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

}
