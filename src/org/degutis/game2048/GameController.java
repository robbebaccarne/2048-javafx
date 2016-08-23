package org.degutis.game2048;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class GameController {
    private HashMap<Tile, TilePresenter> visibleTileViews;
    private Pane board;
    private Game game;
    private ParallelTransition activeTransition;
    private boolean sawEndScreen;
    private boolean showingEndScreen;
    final StackPane root = new StackPane();

    void newGame() {
        game = new Game();
        sawEndScreen = false;
        showingEndScreen = false;

        board = new Pane();
        board.setPrefSize(Config.BOARD_PIXEL_LENGTH, Config.BOARD_PIXEL_LENGTH);
        board.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {
            if (showingEndScreen)
                return;

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

        visibleTileViews = new HashMap<>();
        final ArrayList<Tile> initialTiles = game.addInitialTiles();
        ArrayList<Transition> creationTransitions = new ArrayList<>();

        for (Tile tile : initialTiles) {
            final TilePresenter tilePresenter = new TilePresenter(tile);
            board.getChildren().add(tilePresenter.pane);
            visibleTileViews.put(tile, tilePresenter);

            final Transition transition = tilePresenter.creationTransition();
            creationTransitions.add(transition);
        }

        ParallelTransition creationTransition = new ParallelTransition(creationTransitions.toArray(new Transition[0]));
        creationTransition.play();

        root.setBackground(new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().clear();
        root.getChildren().add(buildBackground());
        root.getChildren().add(board);
    }

    private void runMove(Game.Move move) {
        if (!game.canMove())
            return;

        if (activeTransition != null) {
            final EventHandler<ActionEvent> oldHandler = activeTransition.getOnFinished();
            activeTransition.setOnFinished(event -> {
                oldHandler.handle(null);
                runMove(move);
            });
            activeTransition.setRate(10);
            return;
        }

        final Game.MoveResult moveResult = game.runMove(move);

        if (!moveResult.mergeResult.didChange)
            return;

        ArrayList<Transition> moveTransitions = new ArrayList<>();
        for (Tile movedTile : visibleTileViews.keySet()) {
            final TilePresenter tilePresenter = visibleTileViews.get(movedTile);
            Transition moveTransition = tilePresenter.moveTransition();
            moveTransitions.add(moveTransition);
        }
        ParallelTransition parallelMoveTransition = new ParallelTransition(moveTransitions.toArray(new Animation[0]));

        ArrayList<Transition> popUpTransitions = new ArrayList<>();

        TilePresenter newTilePresenter = new TilePresenter(moveResult.newTile);
        board.getChildren().add(newTilePresenter.pane);
        visibleTileViews.put(moveResult.newTile, newTilePresenter);
        Transition creationTransition = newTilePresenter.creationTransition();
        popUpTransitions.add(creationTransition);

        for (Tile createdTile : moveResult.mergeResult.newTilesFromMerge.keySet()) {
            final TilePresenter tilePresenter = new TilePresenter(createdTile);
            board.getChildren().add(tilePresenter.pane);
            visibleTileViews.put(createdTile, tilePresenter);
            Transition mergeTransition = tilePresenter.mergeTransition();
            popUpTransitions.add(mergeTransition);
        }
        ParallelTransition parallelPopUpTransition = new ParallelTransition(popUpTransitions.toArray(new Animation[0]));
        PauseTransition waitBeforePoppingUpTransition = new PauseTransition(Config.ANIMATION_PAUSE_BEFORE_SECOND_PART);
        SequentialTransition overallPopUpTransition = new SequentialTransition(waitBeforePoppingUpTransition, parallelPopUpTransition);

        activeTransition = new ParallelTransition(parallelMoveTransition, overallPopUpTransition);

        activeTransition.setOnFinished(actionEvent -> {
            for (Tile createdTile : moveResult.mergeResult.newTilesFromMerge.keySet()) {
                for (Tile goneTile : moveResult.mergeResult.newTilesFromMerge.get(createdTile)) {
                    final TilePresenter tilePresenter = visibleTileViews.get(goneTile);
                    board.getChildren().remove(tilePresenter.pane);
                    visibleTileViews.remove(goneTile);
                }
            }

            activeTransition = null;

            int highestNewMergeValue = 0;
            for (Tile createdTile : moveResult.mergeResult.newTilesFromMerge.keySet()) {
                if (createdTile.value > highestNewMergeValue)
                    highestNewMergeValue = createdTile.value;
            }

            maybeShowGameOver(highestNewMergeValue);
        });

        activeTransition.play();
    }

    private void maybeShowGameOver(int newestValue) {
        if (game.canMove() && (newestValue != Config.WINNING_VALUE || sawEndScreen))
            return;

        showingEndScreen = true;
        sawEndScreen = true;
        boolean didWin = newestValue == Config.WINNING_VALUE;

        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(board.getWidth(), board.getHeight());
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#000", 0.35), CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setOpacity(0);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);
        board.getChildren().add(gridPane);

        final Text text = new Text(didWin ? "YOU WON!!! Great job!" : "Oh rats, the game is over.\nGood try though :)");
        text.setFont(new Font(30));
        text.setFill(Color.WHITE);
        gridPane.add(text, 0, 0, 3, 1);

        int nextCol = 0;

        if (didWin) {
            final Button keepPlayingButton = new Button("Keep playing");
            keepPlayingButton.setFont(new Font(Config.BUTTON_FONT_SIZE));
            keepPlayingButton.setOnAction((e) -> {
                showingEndScreen = false;
                board.getChildren().remove(gridPane);
                focusBoard();
            });
            gridPane.add(keepPlayingButton, nextCol++, 1);
        }

        final Button continueButton = new Button("New game");
        continueButton.setFont(new Font(Config.BUTTON_FONT_SIZE));
        continueButton.setOnAction(e -> {
            showingEndScreen = false;
            newGame();
            focusBoard();
        });
        gridPane.add(continueButton, nextCol, 1);

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
                Point point = TilePresenter.getPixelPoint(new Grid.Coordinate(x, y));
                r.setTranslateX(point.x);
                r.setTranslateY(point.y);
                backgroundPane.getChildren().add(r);
            }
        }
        return backgroundPane;
    }

    void focusBoard() {
        board.requestFocus();
    }
}
