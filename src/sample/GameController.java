package sample;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class GameController {
    private final HashMap<Tile, TileView> visibleTileViews = new HashMap<>();
    private final Pane board = new Pane();
    private Stage primaryStage;
    private Game game;
    private ParallelTransition activeTransition;
    private final EventHandler<KeyEvent> gameEventHandler = (keyEvent) -> {
        switch (keyEvent.getCode()) {
            case UP:
                runMove(Game.Move.Up);
                break;
            case DOWN:
                runMove(Game.Move.Down);
                break;
            case LEFT:
                runMove(Game.Move.Left);
                break;
            case RIGHT:
                runMove(Game.Move.Right);
                break;
        }
    };

    GameController(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    void startGame() {
        final ArrayList<Tile> initialTiles = game.startGame();
        ArrayList<Transition> creationTransitions = new ArrayList<>();

        for (Tile tile : initialTiles) {
            final TileView tileView = new TileView(tile);
            board.getChildren().add(tileView.pane);
            visibleTileViews.put(tile, tileView);

            final Transition transition = tileView.creationTransition();
            creationTransitions.add(transition);
        }

        ParallelTransition creationTransition = new ParallelTransition(creationTransitions.toArray(new Transition[0]));
        creationTransition.play();

        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(buildBackground());
        root.getChildren().add(board);

        Scene scene = new Scene(root, Config.BOARD_PIXEL_LENGTH, Config.BOARD_PIXEL_LENGTH);
        primaryStage.setTitle("2048");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, gameEventHandler);
        primaryStage.show();
    }

    private void runMove(Game.Move move) {
        if (game.isGameOver())
            return;

        if (activeTransition != null) {
            activeTransition.jumpTo("end");
            activeTransition = null;
        }

        final Game.MoveResult moveResult = game.runMove(move);

        if (!moveResult.mergeResult.didChange)
            return;

        ArrayList<Transition> moveTransitions = new ArrayList<>();
        for (Tile movedTile : visibleTileViews.keySet()) {
            final TileView tileView = visibleTileViews.get(movedTile);
            Transition moveTransition = tileView.moveTransition();
            moveTransitions.add(moveTransition);
        }
        ParallelTransition parallelMoveTransition = new ParallelTransition(moveTransitions.toArray(new Animation[0]));

        ArrayList<Transition> popUpTransitions = new ArrayList<>();

        TileView newTileView = new TileView(moveResult.newTile);
        board.getChildren().add(newTileView.pane);
        visibleTileViews.put(moveResult.newTile, newTileView);
        Transition creationTransition = newTileView.creationTransition();
        popUpTransitions.add(creationTransition);

        for (Tile createdTile : moveResult.mergeResult.newTilesFromMerge.keySet()) {
            final TileView tileView = new TileView(createdTile);
            board.getChildren().add(tileView.pane);
            visibleTileViews.put(createdTile, tileView);
            Transition mergeTransition = tileView.mergeTransition();
            popUpTransitions.add(mergeTransition);
        }
        ParallelTransition parallelPopUpTransition = new ParallelTransition(popUpTransitions.toArray(new Animation[0]));
        PauseTransition waitBeforePoppingUpTransition = new PauseTransition(Config.ANIMATION_PAUSE_BEFORE_SECOND_PART);
        SequentialTransition overallPopUpTransition = new SequentialTransition(waitBeforePoppingUpTransition, parallelPopUpTransition);

        activeTransition = new ParallelTransition(parallelMoveTransition, overallPopUpTransition);

        activeTransition.setOnFinished((actionEvent) -> {
            for (Tile createdTile : moveResult.mergeResult.newTilesFromMerge.keySet()) {
                for (Tile goneTile : moveResult.mergeResult.newTilesFromMerge.get(createdTile)) {
                    final TileView tileView = visibleTileViews.get(goneTile);
                    board.getChildren().remove(tileView.pane);
                    visibleTileViews.remove(goneTile);
                }
            }

            activeTransition = null;

            maybeShowGameOver();
        });

        activeTransition.play();
    }

    private void maybeShowGameOver() {
        if (!game.isGameOver())
            return;

        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, gameEventHandler);

        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(board.getWidth(), board.getHeight());
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#000", 0.35), CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setOpacity(0);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);
        board.getChildren().add(gridPane);

        final Text text = new Text("Oh rats, the game is over. Good try though :)");
        text.setStroke(Color.WHITE);
        gridPane.add(text, 0, 0, 2, 1);

        final Button continueButton = new Button("New game");
        continueButton.setOnAction((e) -> {
            primaryStage.close();
            new GameController(primaryStage, new Game()).startGame();
        });
        gridPane.add(continueButton, 0, 1);

        final Button quitButton = new Button("Quit");
        quitButton.setOnAction((e) -> System.exit(0));
        gridPane.add(quitButton, 1, 1);

        FadeTransition transition = new FadeTransition(Duration.seconds(1), gridPane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    private Pane buildBackground() {
        Pane backgroundPane = new Pane();
        for (int y = 0; y < Config.GRID_SIZE; y++) {
            for (int x = 0; x < Config.GRID_SIZE; x++) {
                Rectangle r = new Rectangle(Config.TILE_PIXEL_LENGTH, Config.TILE_PIXEL_LENGTH);
                r.setArcWidth(Config.TILE_PIXEL_RADIUS);
                r.setArcHeight(Config.TILE_PIXEL_RADIUS);
                r.setFill(Config.EMPTY_TILE_COLOR);
                Point point = TileView.getPixelPoint(new Grid.Coordinate(x, y));
                r.setTranslateX(point.x);
                r.setTranslateY(point.y);
                backgroundPane.getChildren().add(r);
            }
        }
        return backgroundPane;
    }

}
