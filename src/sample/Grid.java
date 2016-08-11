package sample;

import java.util.ArrayList;
import java.util.Random;

class Grid {

    private final Random randomizer = new Random();
    private Tile[][] tiles = new Tile[Config.GRID_SIZE][Config.GRID_SIZE];

    class MergeResult {
        boolean didChange;
        private ArrayList<Tile> mergedTiles = new ArrayList<>();
        private ArrayList<Tile> newTilesFromMerge = new ArrayList<>();
    }

    Grid rotatedGridClockwise() {
        Grid newGrid = new Grid();
        for (int y = 0; y < Config.GRID_SIZE; y++) {
            for (int x = 0; x < Config.GRID_SIZE; x++) {
                newGrid.tiles[y][x] = this.tiles[x][Config.GRID_SIZE - y - 1];
            }
        }
        return newGrid;
    }

    MergeResult mergeLeft() {
        MergeResult mergeResult = new MergeResult();

        for (int y = 0; y < Config.GRID_SIZE; y++) {
            Tile lastUnmergedTile = null;

            ArrayList<Tile> newRow = new ArrayList<>();

            for (int x = 0; x < Config.GRID_SIZE; x++) {
                Tile currentTile = tiles[y][x];
                if (lastUnmergedTile == null || currentTile.value != lastUnmergedTile.value) {
                    newRow.add(lastUnmergedTile);
                    lastUnmergedTile = currentTile;
                } else {
                    // we have a merge! deal with it.
                    mergeResult.mergedTiles.add(lastUnmergedTile);
                    mergeResult.mergedTiles.add(currentTile);

                    Tile newTile = new Tile(currentTile.value + 1);
                    newRow.add(newTile);
                    mergeResult.newTilesFromMerge.add(newTile);

                    // afterwards, reset lastUnmergedTile to null so the next iteration of this loop can start fresh:
                    lastUnmergedTile = null;
                }
            }

            for (int i = 0; i < Config.GRID_SIZE - newRow.size(); i++) {
                newRow.add(null);
            }

            tiles[y] = (Tile[]) newRow.toArray();
        }

        mergeResult.didChange = true;

        return mergeResult;
    }

    Tile addTile() {
        Coordinate spot = findOpenSpot();
        assert spot != null;

        int value = randomizer.nextDouble() < 0.9 ? 0 : 1;
        final Tile tile = new Tile(value, spot);
        tiles[spot.y][spot.x] = tile;
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
        for (int y = 0; y < Config.GRID_SIZE; y++) {
            for (int x = 0; x < Config.GRID_SIZE; x++) {
                if (tiles[y][x] == null)
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
