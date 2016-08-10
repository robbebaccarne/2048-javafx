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

    enum Move {
        Up, Down, Left, Right
    }

}
