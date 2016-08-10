package sample;

import java.util.ArrayList;

class Game {

    private Grid grid = new Grid();

    ArrayList<Tile> startGame() {
        final ArrayList<Tile> initialTiles = new ArrayList<>();
        initialTiles.add(grid.addTile());
        initialTiles.add(grid.addTile());
        return initialTiles;
    }

    MoveResult runMove(Move move) {
        return null;
    }

    enum Move {
        Up, Down, Left, Right
    }

    class MoveResult {
        Tile newTile;
        ArrayList<Tile> movedTiles;
        ArrayList<Tile> newTilesFromMerge;

        MoveResult(Tile newTile, ArrayList<Tile> movedTiles, ArrayList<Tile> newTilesFromMerge) {
            this.newTile = newTile;
            this.movedTiles = movedTiles;
            this.newTilesFromMerge = newTilesFromMerge;
        }
    }
}
