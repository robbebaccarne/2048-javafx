package sample;

import java.util.ArrayList;
import java.util.Random;

class Grid {

    private final Random randomizer = new Random();
    private Tile[][] tiles = new Tile[Config.GRID_SIZE][Config.GRID_SIZE];

    void fixCoordinates() {
        for (int y = 0; y < Config.GRID_SIZE; y++) {
            for (int x = 0; x < Config.GRID_SIZE; x++) {
                final Tile tile = tiles[y][x];
                if (tile != null)
                    tile.spot = new Coordinate(x, y);
            }
        }
    }

    class MergeResult {
        boolean didChange;
        ArrayList<Tile> mergedTiles = new ArrayList<>();
        ArrayList<Tile> newTilesFromMerge = new ArrayList<>();
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
        mergeResult.didChange = false;

        for (int y = 0; y < Config.GRID_SIZE; y++) {
            Tile lastUnmergedTile = null;

            ArrayList<Tile> newRow = new ArrayList<>();

            for (int x = 0; x < Config.GRID_SIZE; x++) {
                Tile currentTile = tiles[y][x];

                // on first iteration, lastUnmergedTile will be null
                if (lastUnmergedTile == null) {

                    // either this is the first iteration, or we just merged.
                    lastUnmergedTile = currentTile;
                    continue;

                } else if (currentTile == null) {

                    // we have an unmerged tile, but this is an empty space, so continue.
                    continue;

                } else {

                    // we have an unmerged tile, and we have a current tile!

                    if (currentTile.value == lastUnmergedTile.value) {
                        // they're the same! time to merge! GO GO GO GO GO!!!

                        // we have a merge! deal with it.
                        mergeResult.mergedTiles.add(lastUnmergedTile);
                        mergeResult.mergedTiles.add(currentTile);

                        Tile newTile = new Tile(currentTile.value + 1);
                        newRow.add(newTile);
                        mergeResult.newTilesFromMerge.add(newTile);

                        // afterwards, reset lastUnmergedTile to null so the next iteration of this loop can start fresh:
                        lastUnmergedTile = null;
                    } else {
                        // hmm, they're not the same. okay, skip this one.
                        newRow.add(lastUnmergedTile);
                        lastUnmergedTile = currentTile;
                        continue;
                    }
                }
            }

            for (int i = 0; i < Config.GRID_SIZE - newRow.size(); i++) {
                newRow.add(null);
            }

            tiles[y] = newRow.toArray(new Tile[0]);
        }

        mergeResult.didChange = true;

        return mergeResult;
    }

    Tile addRandomTile() {
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
