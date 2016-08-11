package sample;

import java.util.ArrayList;

class Game {

    private Grid grid = new Grid();

    ArrayList<Tile> startGame() {
        ArrayList<Tile> initialTiles = new ArrayList<>();
        initialTiles.add(grid.addTile());
        initialTiles.add(grid.addTile());
        return initialTiles;
    }

    MoveResult runMove(Move move) {
        MoveResult result = new MoveResult();

        int rotations = move.rotations;

        Grid tempGrid = grid;
        for (int i = 0; i < 4; i++) {
            if (i == rotations) {
                tempGrid.mergeLeft();
            }
            tempGrid = tempGrid.rotatedGridClockwise();
        }

//        final Tile tile = initialTiles.get(0);
//        tile.spot = new Grid.Coordinate(tile.spot.x, tile.spot.y + 1);
//
//        result.movedTiles = initialTiles;

        return result;
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
        boolean isGameOver = false;
        Tile newTile;
        ArrayList<Tile> movedTiles = new ArrayList<>();
        ArrayList<Tile> newTilesFromMerge = new ArrayList<>();
    }
}
