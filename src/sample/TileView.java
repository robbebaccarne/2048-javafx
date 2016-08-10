package sample;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.awt.*;

class TileView {

    Pane pane;

    TileView(Tile tile) {
        final Config.TileDesign design = Config.TileDesign.forValue(tile.value);

        Rectangle rectangle = new Rectangle(Config.PIXEL_LENGTH, Config.PIXEL_LENGTH);
        rectangle.setArcWidth(Config.TILE_RADIUS);
        rectangle.setArcHeight(Config.TILE_RADIUS);
        rectangle.setFill(design.backColor);

        Text text = new Text(String.valueOf(2 << tile.value));
        text.setFont(Font.font(text.getFont().getName(), FontWeight.BOLD, Config.PIXEL_LENGTH / design.sizeFraction));
        text.setFill(design.foreColor);
        text.setStroke(Color.TRANSPARENT);

        Point p = getPixelPoint(tile.spot);
        pane = new StackPane(rectangle, text);
        pane.setTranslateX(p.x);
        pane.setTranslateY(p.y);
    }

    static Point getPixelPoint(Grid.Coordinate spot) {
        int px = (Config.PIXEL_LENGTH * spot.x) + ((spot.x + 1) * Config.PIXEL_PADDING);
        int py = (Config.PIXEL_LENGTH * spot.y) + ((spot.y + 1) * Config.PIXEL_PADDING);
        return new Point(px, py);
    }

}