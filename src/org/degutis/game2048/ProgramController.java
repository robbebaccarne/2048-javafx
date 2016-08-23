package org.degutis.game2048;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

class ProgramController {

    void start(Stage primaryStage) {
        GameController gameController = new GameController();
        Tab boardTab = new Tab("Game", gameController.root);
        boardTab.setClosable(false);

        SettingsController settingsController = new SettingsController();
        Tab settingsPane = new Tab("Settings", settingsController.root);
        settingsPane.setClosable(false);

        final TabPane tabPane = new TabPane(boardTab, settingsPane);
        tabPane.setFocusTraversable(false);

        tabPane.getSelectionModel().selectedItemProperty().addListener(o -> Platform.runLater(() -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            selectedTab.getContent().requestFocus();
        }));

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        gameController.newGame();
        gameController.root.requestFocus();
    }

}
