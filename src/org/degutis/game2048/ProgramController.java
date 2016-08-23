package org.degutis.game2048;

import javafx.stage.Stage;

class ProgramController {

    void start(Stage primaryStage) {
        GameController gameController = new GameController();
        gameController.newGame();
        primaryStage.setScene(gameController.scene);
        primaryStage.show();
        gameController.scene.getRoot().requestFocus();
    }

}
