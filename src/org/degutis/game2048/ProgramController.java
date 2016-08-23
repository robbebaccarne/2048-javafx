package org.degutis.game2048;

import javafx.scene.Scene;
import javafx.stage.Stage;

class ProgramController {

    void start(Stage primaryStage) {
        GameController gameController = new GameController();
        gameController.newGame();
        Scene scene = new Scene(gameController.root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gameController.focusBoard();
    }

}
