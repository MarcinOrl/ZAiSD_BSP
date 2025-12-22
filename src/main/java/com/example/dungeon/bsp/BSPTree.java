package com.example.dungeon.bsp;

import com.example.dungeon.map.Corridor;
import com.example.dungeon.map.Room;
import com.example.dungeon.util.RandomProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSPTree {
    private final Leaf root;
    private final int minLeaf, maxLeaf;
    private final RandomProvider rng;

    private final List<Leaf> leaves = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Corridor> corridors = new ArrayList<>();

    public BSPTree(int x,int y,int width,int height,int minLeaf,int maxLeaf,long seed){
        this.root = new Leaf(x,y,width,height);
        this.minLeaf = minLeaf;
        this.maxLeaf = maxLeaf;
        this.rng = new RandomProvider(seed);
    }

    public void splitAll() {
        leaves.clear();
        leaves.add(root);
        boolean didSplit = true;
        while (didSplit) {
            didSplit = false;
            List<Leaf> snapshot = new ArrayList<>(leaves);
            for (int i = 0; i < snapshot.size(); i++) {
                Leaf l = snapshot.get(i);
                if (l.isLeaf()) {
                    if (l.width > maxLeaf || l.height > maxLeaf || rng.nextDouble() < 0.25) {
                        if (l.split(minLeaf, maxLeaf, rng)) {
                            // replace leaf with its children
                            leaves.remove(l);
                            leaves.add(l.left);
                            leaves.add(l.right);
                            didSplit = true;
                        }
                    }
                }
            }
        }
    }

    public List<Leaf> getLeaves(){ return new ArrayList<>(leaves); }

    public void createRooms(int minRoomSize, int maxRoomSize, int padding) {
        rooms.clear();
        for (Leaf l : leaves) {
            int maxW = Math.max(minRoomSize, Math.min(maxRoomSize, l.width - 2*padding));
            int maxH = Math.max(minRoomSize, Math.min(maxRoomSize, l.height - 2*padding));
            if (maxW < minRoomSize || maxH < minRoomSize) continue;
            int w = rng.nextInt(minRoomSize, maxW);
            int h = rng.nextInt(minRoomSize, maxH);
            int x = l.x + padding + rng.nextInt(0, Math.max(0, l.width - 2*padding - w));
            int y = l.y + padding + rng.nextInt(0, Math.max(0, l.height - 2*padding - h));
            Room r = new Room(x, y, w, h);
            l.room = r;
            rooms.add(r);
        }
    }

    public void connectRooms() {
        corridors.clear();
        connectRec(root);
    }

    private Room connectRec(Leaf node) {
        if (node == null) return null;
        if (node.isLeaf()) return node.room;
        Room leftRoom = connectRec(node.left);
        Room rightRoom = connectRec(node.right);
        if (leftRoom != null && rightRoom != null) {
            // connect centers with L-shaped corridor
            int ax = leftRoom.centerX();
            int ay = leftRoom.centerY();
            int bx = rightRoom.centerX();
            int by = rightRoom.centerY();
            // choose whether to go horizontal then vertical or vice versa
            if (rng.nextBoolean()) {
                // horizontal then vertical
                corridors.add(new Corridor(ax, ay, bx, ay));
                corridors.add(new Corridor(bx, ay, bx, by));
            } else {
                corridors.add(new Corridor(ax, ay, ax, by));
                corridors.add(new Corridor(ax, by, bx, by));
            }
        }
        // return one representative room for parent links
        return (leftRoom != null) ? leftRoom : rightRoom;
    }

    public List<Room> getRooms() { return new ArrayList<>(rooms); }
    public List<Corridor> getCorridors() { return new ArrayList<>(corridors); }
}
