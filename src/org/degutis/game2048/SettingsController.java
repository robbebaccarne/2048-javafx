package org.degutis.game2048;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

class SettingsController {
    final ScrollPane root;

    SettingsController() {
        final Text text = new Text("Coming soon!");
        final GridPane gridPane = new GridPane();
        gridPane.add(text, 0, 0);
        root = new ScrollPane(gridPane);
        root.setPrefSize(Config.BOARD_PIXEL_LENGTH, Config.BOARD_PIXEL_LENGTH);
    }

}
