package de.dhbw.advancewars.event;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.graphics.MapPane;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.maps.data.TileType;

import java.util.*;

/**
 * Default controller for events surrounding and regarding a game/match.
 */
public class GameController implements IGameController {
    public MapPane mapPane;

    private String mapName;

    /**
     * @param mapName The name of the map to set.
     */
    @Override
    public void handleUserSetMap(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return The name of the current map.
     */
    @Override
    public String getMapName() {
        return this.mapName;
    }

    /**
     * @param tile The tile that was clicked.
     */
    @Override
    public void handleTileClick(MapTile tile) {
        System.out.println("Clicked on tile " + tile);
//        List<MapTile> way = findPath(AdvanceWars.getMap().tiles(), 0, 0, tile.x(), tile.y());
//        System.out.println(way.size());
//        MapTile[] res = new MapTile[way.size()];
//        AdvanceWars.getMapRenderer().overlayTiles(mapPane, way.toArray(res));
    }

    /**
     * @param tile The tile that was hovered over.
     */
    @Override
    public void handleTileHover(MapTile tile) {
    }

    /**
     * @param tile The tile that was exited.
     */
    @Override
    public void handleTileExit(MapTile tile) {
    }

    /**
     * @param tile The tile that was right-clicked.
     */
    @Override
    public void handleTileRightClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was left-clicked.
     */
    @Override
    public void handleTileLeftClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was middle-clicked.
     */
    @Override
    public void handleTileMiddleClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * Calculate the most direct way to a certain tile.
     *
     * @param start The starting tile.
     * @param end   The target tile.
     * @return The tiles to cross.
     */
    @Override
    public List<MapTile> getPath(MapTile start, MapTile end) {
        boolean finished = false;
        List<MapTile> used = new ArrayList<>();

        used.add(start);

        while (!finished) {
            List<MapTile> nOpen = new ArrayList<>();
            for (MapTile tile : used) {
                List<MapTile> neighbors = getNeighbors(tile);
                for (MapTile neighbor : neighbors) {
                    if (!used.contains(neighbor) && !nOpen.contains(neighbor)) {
                        nOpen.add(neighbor);
                    }
                }
            }

            for (MapTile tile : nOpen) {
                if (tile.equals(end)) {
                    finished = true;
                    break;
                }
            }

            if (!finished && nOpen.isEmpty()) {
                return null;
            }
        }

        List<MapTile> path = new ArrayList<>();
        MapTile last = used.get(used.size() - 1);

        while (!last.equals(start)) {
            path.add(last);
            List<MapTile> neighbors = getNeighbors(last);
            for (MapTile neighbor : neighbors) {
                if (used.contains(neighbor)) {
                    last = neighbor;
                    break;
                }
            }
        }

        Collections.reverse(path);
        return path;
    }

    private boolean isReachable(MapTile end) {
        // TODO: Implement this method to check for character abilities...
        return end != null;
    }

    private List<MapTile> getNeighbors(MapTile tile) {
        List<MapTile> result = new ArrayList<>();

        MapTile left = AdvanceWars.getMap().offset(tile.x() - 1, tile.y());
        MapTile right = AdvanceWars.getMap().offset(tile.x() + 1, tile.y());
        MapTile up = AdvanceWars.getMap().offset(tile.x(), tile.y() - 1);
        MapTile down = AdvanceWars.getMap().offset(tile.x(), tile.y() + 1);

        if (isReachable(left)) {
            result.add(left);
        }

        if (isReachable(right)) {
            result.add(right);
        }

        if (isReachable(up)) {
            result.add(up);
        }

        if (isReachable(down)) {
            result.add(down);
        }

        return result;
    }

    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};

    public static List<MapTile> findPath(MapTile[][] grid, int startX, int startY, int goalX, int goalY) {
        PriorityQueue<MapTile> openList = new PriorityQueue<>();
        HashSet<MapTile> closedList = new HashSet<>();
        openList.add(new MapTile(TileType.PLAIN, startX, startY, 0, heuristic(startX, startY, goalX, goalY), null));

        while (!openList.isEmpty()) {
            MapTile current = openList.poll();
            if (current.x() == goalX && current.y() == goalY) {
                return constructPath(current);
            }

            closedList.add(current);

            for (int i = 0; i < 4; i++) {
                int newX = current.x() + DX[i];
                int newY = current.y() + DY[i];

                if (isValidMove(grid, newX, newY) && !isInClosedList(closedList, newX, newY)) {
                    int newG = current.g() + 1;
                    int newH = heuristic(newX, newY, goalX, goalY);
                    MapTile neighbor = new MapTile(TileType.PLAIN, newX, newY, newG, newH, current);

                    if (!isInOpenList(openList, neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }

    private static boolean isValidMove(MapTile[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length /** && grid[x][y] == 0 **/;
    }

    private static boolean isInClosedList(HashSet<MapTile> closedList, int x, int y) {
        return closedList.stream().anyMatch(node -> node.x() == x && node.y() == y);
    }

    private static boolean isInOpenList(PriorityQueue<MapTile> openList, MapTile neighbor) {
        return openList.stream().anyMatch(node -> node.x() == neighbor.x() && node.y() == neighbor.y() && node.g() <= neighbor.g());
    }

    private static List<MapTile> constructPath(MapTile node) {
        List<MapTile> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent();
        }
        Collections.reverse(path);
        return path;
    }

    private static int heuristic(int x, int y, int goalX, int goalY) {
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }
}
