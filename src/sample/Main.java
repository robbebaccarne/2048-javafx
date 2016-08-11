package sample;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("2048");
        final Game game = new Game();
        final GameController gameController = new GameController(primaryStage, game);
        gameController.begin();
    }

}