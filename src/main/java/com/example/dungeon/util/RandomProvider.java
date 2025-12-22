package com.example.dungeon.util;

import java.util.Random;

public class RandomProvider {
    private final Random rng;
    public RandomProvider(long seed) { rng = new Random(seed); }
    public int nextInt(int bound) { return rng.nextInt(bound); }
    public int nextInt(int min, int max) { return min + rng.nextInt(max - min + 1); }
    public boolean nextBoolean() { return rng.nextBoolean(); }
    public double nextDouble() { return rng.nextDouble(); }
}
