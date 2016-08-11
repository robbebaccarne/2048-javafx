package sample;

import java.util.ArrayList;
import java.util.HashMap;

class Game {

    private Grid grid = new Grid();

    ArrayList<Tile> startGame() {
        ArrayList<Tile> initialTiles = new ArrayList<>();
        initialTiles.add(grid.addRandomTile());
        initialTiles.add(grid.addRandomTile());
        return initialTiles;
    }

    MoveResult runMove(Move move) {
        MoveResult moveResult = new MoveResult();

        Grid.MergeResult mergeResult = null;
        for (int i = 0; i < 4; i++) {
            if (i == move.rotations) {
                mergeResult = grid.mergeLeft();
            }
            grid.rotateClockwise();
        }
        assert mergeResult != null;
        grid.reassignCoordinates();


        for (Tile newTileFromMerge : mergeResult.newTilesFromMerge.keySet()) {
            for (Tile goneTile : mergeResult.newTilesFromMerge.get(newTileFromMerge)) {
                goneTile.spot = newTileFromMerge.spot;
            }
        }

        moveResult.didChange = mergeResult.didChange;
        moveResult.newTilesFromMerge = mergeResult.newTilesFromMerge;
        moveResult.isGameOver = false;

        if (mergeResult.didChange) {
            moveResult.newTile = grid.addRandomTile();
        }

        return moveResult;
    }

    enum Move {
        Left(0),
        Down(1),
        Right(2),
        Up(3);

        private int rotations;

        Move(int rotations) {
            this.rotations = rotations;
        }
    }

    class MoveResult {
        boolean didChange = false;
        boolean isGameOver = false;
        Tile newTile;
        HashMap<Tile, ArrayList<Tile>> newTilesFromMerge = new HashMap<>();
    }

}
