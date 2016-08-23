package org.degutis.game2048;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

class SettingsController {

    final ScrollPane root;

    private final Settings defaultSettings = new Settings();

    private SimpleIntegerProperty startingTiles = new SimpleIntegerProperty(defaultSettings.startingTiles);

    SettingsController(NewSettingsTask newSettingsTask) {
        final GridPane gridPane = new GridPane();
        root = new ScrollPane(gridPane);
        root.setPrefSize(Config.BOARD_PIXEL_LENGTH, Config.BOARD_PIXEL_LENGTH);

        gridPane.add(new Text("Starting tiles:"), 0, 0);
        TextField startingTilesField = new TextField();
        gridPane.add(startingTilesField, 1, 0);
        startingTilesField.textProperty().bindBidirectional(startingTiles, new NumberStringConverter());

        final Button newGameButton = new Button("Start new game using these settings");
        newGameButton.setOnAction(e -> newSettingsTask.use(buildSettings()));
        gridPane.add(newGameButton, 0, 1, 2, 1);
    }

    Settings buildSettings() {
        Settings settings = new Settings();
        settings.startingTiles = startingTiles.get();
        return settings;
    }
}
