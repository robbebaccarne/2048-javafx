package sample;

import java.util.ArrayDeque;
import java.util.ArrayList;

class Game {

    private Grid grid = new Grid();
    private ArrayDeque<Move> queuedMoves = new ArrayDeque<>();

    ArrayList<Tile> startGame() {
        final ArrayList<Tile> initialTiles = new ArrayList<>();
        initialTiles.add(grid.addTile());
        initialTiles.add(grid.addTile());
        return initialTiles;
    }

    void queueMove(Move move) {
        queuedMoves.add(move);
        System.out.println("welp: " + queuedMoves);
    }

    enum Move {
        Up, Down, Left, Right
    }

}
