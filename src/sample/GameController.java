package sample;

import javafx.animation.Transition;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

class GameController {
    private final HashMap<Tile, TileController> visibleTileViews = new HashMap<>();
    private Stage primaryStage;
    private Game game;
    private final Pane board = new Pane();

    private ArrayDeque<Game.Move> queuedMoves = new ArrayDeque<>();

    GameController(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;

        final ArrayList<Tile> initialTiles = game.startGame();

        for (Tile tile : initialTiles) {
            final TileController tileController = new TileController(tile);
            board.getChildren().add(tileController.pane);
            visibleTileViews.put(tile, tileController);
        }
    }

    private void queueMove(Game.Move move) {
        queuedMoves.add(move);
        System.out.println("welp: " + queuedMoves);
        runNextMove();
    }

    private void runNextMove() {
        if (queuedMoves.isEmpty())
            return;

        final Game.Move nextMove = queuedMoves.pop();
        final Game.MoveResult result = game.runMove(nextMove);

        if (result.isGameOver) {
            System.out.println("Game over!");
        }
        else {
            for (Tile movedTile : result.movedTiles) {
                final TileController tileController = visibleTileViews.get(movedTile);
                Transition t = tileController.makeTransition();
                t.play();
            }
        }
    }

    void setup() {
        StackPane root = new StackPane();
        root.getChildren().add(buildBackground());
        root.getChildren().add(board);

        Scene scene = new Scene(root, Config.BOARD_PIXEL_LENGTH, Config.BOARD_PIXEL_LENGTH, Config.BACKGROUND_COLOR);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {
            switch (keyEvent.getCode()) {
                case UP:
                    queueMove(Game.Move.Up);
                    break;
                case DOWN:
                    queueMove(Game.Move.Down);
                    break;
                case LEFT:
                    queueMove(Game.Move.Left);
                    break;
                case RIGHT:
                    queueMove(Game.Move.Right);
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