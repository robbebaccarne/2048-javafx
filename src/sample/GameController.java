package sample;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class GameController {
    private final HashMap<Tile, TileController> visibleTileViews = new HashMap<>();
    private final Pane board = new Pane();
    private Stage primaryStage;
    private Game game;
    private ParallelTransition activeTransition;

    GameController(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;

        final ArrayList<Tile> initialTiles = game.startGame();
        ArrayList<Transition> creationTransitions = new ArrayList<>();

        for (Tile tile : initialTiles) {
            final TileController tileController = new TileController(tile);
            board.getChildren().add(tileController.pane);
            visibleTileViews.put(tile, tileController);

            final Transition transition = tileController.creationTransition();
            creationTransitions.add(transition);
        }

        ParallelTransition creationTransition = new ParallelTransition(creationTransitions.toArray(new Transition[0]));
        creationTransition.play();
    }

    private void runMove(Game.Move move) {
        if (activeTransition != null) {
            activeTransition.jumpTo("end");
            activeTransition = null;
        }

        final Game.MoveResult moveResult = game.runMove(move);

        if (moveResult.isGameOver) {
            System.out.println("Game over!");
        } else if (moveResult.didChange) {
            ArrayList<Transition> moveTransitions = new ArrayList<>();
            for (Tile movedTile : visibleTileViews.keySet()) {
                final TileController tileController = visibleTileViews.get(movedTile);
                Transition moveTransition = tileController.moveTransition();
                moveTransitions.add(moveTransition);
            }
            ParallelTransition parallelMoveTransition = new ParallelTransition(moveTransitions.toArray(new Animation[0]));

            ArrayList<Transition> popUpTransitions = new ArrayList<>();

            TileController newTileController = new TileController(moveResult.newTile);
            board.getChildren().add(newTileController.pane);
            visibleTileViews.put(moveResult.newTile, newTileController);
            Transition creationTransition = newTileController.creationTransition();
            popUpTransitions.add(creationTransition);

            for (Tile createdTile : moveResult.newTilesFromMerge.keySet()) {
                final TileController tileController = new TileController(createdTile);
                board.getChildren().add(tileController.pane);
                visibleTileViews.put(createdTile, tileController);
                Transition mergeTransition = tileController.mergeTransition();
                popUpTransitions.add(mergeTransition);
            }
            ParallelTransition parallelPopUpTransition = new ParallelTransition(popUpTransitions.toArray(new Animation[0]));
            PauseTransition waitBeforePoppingUpTransition = new PauseTransition(Config.ANIMATION_PAUSE_BEFORE_SECOND_PART);
            SequentialTransition overallPopUpTransition = new SequentialTransition(waitBeforePoppingUpTransition, parallelPopUpTransition);

            activeTransition = new ParallelTransition(parallelMoveTransition, overallPopUpTransition);

            activeTransition.setOnFinished((actionEvent) -> {
                for (Tile createdTile : moveResult.newTilesFromMerge.keySet()) {
                    for (Tile goneTile : moveResult.newTilesFromMerge.get(createdTile)) {
                        final TileController tileController = visibleTileViews.get(goneTile);
                        board.getChildren().remove(tileController.pane);
                        visibleTileViews.remove(goneTile);
                    }
                }

                activeTransition = null;
            });

            activeTransition.play();
        }
    }

    void begin() {
        StackPane root = new StackPane();
        root.getChildren().add(buildBackground());
        root.getChildren().add(board);

        Scene scene = new Scene(root, Config.BOARD_PIXEL_LENGTH, Config.BOARD_PIXEL_LENGTH, Config.BACKGROUND_COLOR);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {
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
        });
        primaryStage.show();
    }

    private Pane buildBackground() {
        Pane backgroundPane = new GridPane();
        for (int y = 0; y < Config.GRID_SIZE; y++) {
            for (int x = 0; x < Config.GRID_SIZE; x++) {
                Rectangle r = new Rectangle(Config.PIXEL_LENGTH, Config.PIXEL_LENGTH);
                r.setArcWidth(Config.TILE_RADIUS);
                r.setArcHeight(Config.TILE_RADIUS);
                r.setFill(Config.EMPTY_TILE_COLOR);
                Point point = TileController.getPixelPoint(new Grid.Coordinate(x, y));
                r.setTranslateX(point.x);
                r.setTranslateY(point.y);
                backgroundPane.getChildren().add(r);
            }
        }
        return backgroundPane;
    }

}
