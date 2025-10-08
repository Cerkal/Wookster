package main;

import java.awt.Point;
import java.util.*;

import entity.Entity;

public class Pathfinder {
    private GamePanel gamePanal;

    public Pathfinder(GamePanel gamePanal) {
        this.gamePanal = gamePanal;
    }

    public List<Point> findPath(Point start, Point goal) {
        if (start == null || goal == null) return Collections.emptyList();
        if (start.equals(goal)) return Collections.emptyList();

        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        queue.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(goal)) break;

            for (Point neighbor : getNeighbors(current)) {
                if (!cameFrom.containsKey(neighbor) && isWalkable(neighbor)) {
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        // No path
        if (!cameFrom.containsKey(goal)) {
            return Collections.emptyList();
        }

        List<Point> path = new ArrayList<>();
        Point current = goal;
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        path.remove(0); // Remove the current location
        return path;
    }

    private List<Point> getNeighbors(Point p) {
        List<Point> neighbors = new ArrayList<>(4);
        neighbors.add(new Point(p.x + 1, p.y));
        neighbors.add(new Point(p.x - 1, p.y));
        neighbors.add(new Point(p.x, p.y + 1));
        neighbors.add(new Point(p.x, p.y - 1));
        return neighbors;
    }

    public boolean isTileWalkable(int x, int y) {
        return this.gamePanal.tileManager.isTileWalkable(x, y);
    }

    private boolean isWalkable(Point p) {
        if (p.x < 0 || p.y < 0 || p.x >= Constants.MAX_WORLD_COL || p.y >= Constants.MAX_WORLD_ROW) {
            return false;
        }
        return isTileWalkable(p.x, p.y);
    }

    public Point randomFrenzyPoint() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(Constants.MAX_WORLD_COL);
            y = rand.nextInt(Constants.MAX_WORLD_ROW);
        } while (!isTileWalkable(x, y));
        return new Point(x, y);
    }

    public Point randomFrenzyPointWithinAreaSquare(List<Point> area) {
        if (area == null || area.isEmpty() || areWalkableTilesInArea(area) == false) {
            return randomFrenzyPoint();
        }
        int x, y;
        do {
            x = Utils.generateRandomInt(area.get(0).x, area.get(1).x);
            y = Utils.generateRandomInt(area.get(0).y, area.get(1).y);
        } while (!isTileWalkable(x, y));
        return new Point(x, y);
    }

    private boolean areWalkableTilesInArea(List<Point> area) {
        for (int y = area.get(0).y; y < area.get(1).y; y++) {
            for (int x = area.get(0).x; x < area.get(1).x; x++) {
                if (isTileWalkable(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Point getPushBackLocationFollow(Entity entity, Entity collidingEntity) {
        int dx = collidingEntity.worldX - entity.worldX;
        int dy = collidingEntity.worldY - entity.worldY;
        int maxDistSq = -1;
        Point furthest = null;

        if (dx != 0 || dy != 0) {
            int ix = dx != 0 ? dx / Math.abs(dx) * -1 : 0;
            int iy = dy != 0 ? dy / Math.abs(dy) * -1 : 0;
            for (int y = entity.getRawY(); y > 0 && y < Constants.MAX_WORLD_ROW; y += (iy == 0 ? 1 : iy)) {
                for (int x =entity. getRawX(); x > 0 && x < Constants.MAX_WORLD_COL; x += (ix == 0 ? 1 : ix)) {
                    if (isTileWalkable(x, y)) {
                        int distSq = (x - entity.getRawX()) * (x - entity.getRawX()) + (y - entity.getRawY()) * (y - entity.getRawY());
                        if (distSq > maxDistSq) {
                            maxDistSq = distSq;
                            furthest = new Point(x, y);
                        }
                    }
                }
            }
        }
        if (furthest != null) {
            return furthest;
        }
        return randomFrenzyPoint();
    }
}
