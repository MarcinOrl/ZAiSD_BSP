package com.example.dungeon.bsp;

import com.example.dungeon.map.Room;
import com.example.dungeon.util.RandomProvider;

public class Leaf {
    public final int x, y, width, height;
    public Leaf left, right;
    public Room room;

    public Leaf(int x, int y, int width, int height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }

    public boolean isLeaf() { return left == null && right == null; }

    public boolean split(int minLeafSize, int maxLeafSize, RandomProvider rng) {
        if (!isLeaf()) return false;
        boolean splitH = rng.nextBoolean();
        if (width > height && width / height >= 1.25) splitH = false;
        else if (height > width && height / width >= 1.25) splitH = true;

        int max = (splitH ? height : width) - minLeafSize;
        if (max <= minLeafSize) return false;

        int split = rng.nextInt(minLeafSize, max);

        if (splitH) {
            left = new Leaf(x, y, width, split);
            right = new Leaf(x, y + split, width, height - split);
        } else {
            left = new Leaf(x, y, split, height);
            right = new Leaf(x + split, y, width - split, height);
        }
        return true;
    }
}
