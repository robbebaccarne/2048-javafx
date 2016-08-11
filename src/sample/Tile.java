package sample;

import java.util.ArrayList;

class Tile {

    int value;
    Grid.Coordinate spot;
    ArrayList<Tile> mergedFrom = new ArrayList<>();

    Tile(int value) {
        this.value = value;
    }

    Tile(int value, Grid.Coordinate spot) {
        this.value = value;
        this.spot = spot;
    }
}
