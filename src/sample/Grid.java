package sample;

import java.util.ArrayList;
import java.util.Random;

class Grid {

    private final Random randomizer = new Random();
    private Tile[][] tiles = new Tile[Config.GRID_SIZE][Config.GRID_SIZE];

    class MergeResult {
    }

    Grid rotatedGridClockwise() {
        Grid newGrid = new Grid();
        for (int x = 0; x < Config.GRID_SIZE; x++) {
            for (int y = 0; y < Config.GRID_SIZE; y++) {
                newGrid.tiles[x][y] = this.tiles[y][Config.GRID_SIZE - x - 1];
            }
        }
        return newGrid;
    }

    MergeResult mergeLeft() {
        return null;
    }

    Tile addTile() {
        Coordinate spot = findOpenSpot();
        assert spot != null;

        int value = randomizer.nextDouble() < 0.9 ? 0 : 1;
        final Tile tile = new Tile(value, spot);
        tiles[spot.x][spot.y] = tile;
        return tile;
    }

    private Coordinate findOpenSpot() {
        final ArrayList<Coordinate> coordinates = openSpots();
        if (openSpots().isEmpty())
            return null;

        int i = randomizer.nextInt(coordinates.size());
        return coordinates.get(i);
    }

    private ArrayList<Coordinate> openSpots() {
        ArrayList<Coordinate> spots = new ArrayList<Coordinate>();
        for (int x = 0; x < Config.GRID_SIZE; x++) {
            for (int y = 0; y < Config.GRID_SIZE; y++) {
                if (tiles[x][y] == null)
                    spots.add(new Coordinate(x, y));
            }
        }
        return spots;
    }

    static class Coordinate {
        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
