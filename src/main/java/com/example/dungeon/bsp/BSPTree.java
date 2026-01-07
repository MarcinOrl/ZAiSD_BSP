package com.example.dungeon.bsp;

import com.example.dungeon.map.Corridor;
import com.example.dungeon.map.Room;
import com.example.dungeon.util.RandomProvider;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BSPTree {

    private final Leaf root;
    private final int minLeaf, maxLeaf;
    private final RandomProvider rng;

    private final List<Leaf> leaves = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Corridor> corridors = new ArrayList<>();

    public BSPTree(int x, int y, int width, int height,
                   int minLeaf, int maxLeaf, long seed) {
        this.root = new Leaf(x, y, width, height);
        this.minLeaf = minLeaf;
        this.maxLeaf = maxLeaf;
        this.rng = new RandomProvider(seed);
    }

    // ---------------- BSP SPLIT ----------------

    public void splitAll() {
        leaves.clear();
        leaves.add(root);

        boolean didSplit = true;
        while (didSplit) {
            didSplit = false;
            List<Leaf> snapshot = new ArrayList<>(leaves);

            for (Leaf l : snapshot) {
                if (!l.isLeaf()) continue;

                if (l.width > maxLeaf || l.height > maxLeaf || rng.nextDouble() < 0.25) {
                    if (l.split(minLeaf, maxLeaf, rng)) {
                        leaves.remove(l);
                        leaves.add(l.left);
                        leaves.add(l.right);
                        didSplit = true;
                    }
                }
            }
        }
    }

    public List<Leaf> getLeaves() {
        return new ArrayList<>(leaves);
    }

    // ---------------- ROOMS ----------------

    public void createRooms(int minRoomSize, int maxRoomSize, int padding) {
        rooms.clear();

        for (Leaf l : leaves) {
            int maxW = Math.min(maxRoomSize, l.width - 2 * padding);
            int maxH = Math.min(maxRoomSize, l.height - 2 * padding);

            if (maxW < minRoomSize || maxH < minRoomSize) continue;

            int w = rng.nextInt(minRoomSize, maxW);
            int h = rng.nextInt(minRoomSize, maxH);

            int x = l.x + padding + rng.nextInt(0, l.width - 2 * padding - w);
            int y = l.y + padding + rng.nextInt(0, l.height - 2 * padding - h);

            Room r = new Room(x, y, w, h);
            l.room = r;
            rooms.add(r);
        }
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    // ---------------- CORRIDORS ----------------

    public void connectRooms() {
        corridors.clear();
        connectRec(root);
    }

    private Point connectRec(Leaf node) {
        if (node == null) return null;

        if (node.isLeaf()) {
            if (node.room == null) return null;
            return randomPointInRoom(node.room);
        }

        Point left = connectRec(node.left);
        Point right = connectRec(node.right);

        if (left != null && right != null) {
            createCorridor(left, right);
            return rng.nextBoolean() ? left : right;
        }

        return (left != null) ? left : right;
    }

    private Point randomPointInRoom(Room r) {
        int x = rng.nextInt(r.x + 1, r.x + r.width - 2);
        int y = rng.nextInt(r.y + 1, r.y + r.height - 2);
        return new Point(x, y);
    }

    private void createCorridor(Point a, Point b) {
        if (rng.nextBoolean()) {
            corridors.add(new Corridor(a.x, a.y, b.x, a.y));
            corridors.add(new Corridor(b.x, a.y, b.x, b.y));
        } else {
            corridors.add(new Corridor(a.x, a.y, a.x, b.y));
            corridors.add(new Corridor(a.x, b.y, b.x, b.y));
        }
    }

    public List<Corridor> getCorridors() {
        return new ArrayList<>(corridors);
    }
}
