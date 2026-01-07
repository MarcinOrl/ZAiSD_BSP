package com.example.dungeon.config;

import java.util.List;

public class Config {
    public int width;
    public int height;
    public int minLeaf;
    public int maxLeaf;
    public int minRoom;
    public int maxRoom;
    public int padding;
    public long seed;
    public String outDir = "out";
    public List<String> stages;

    // Visual options
    public String backgroundColor = "#ffffff";
    public String leafStrokeColor = "#000000";
    public String leafFillColor = "#ffffff";
    public String roomFillColor = "#454545";
    public String roomStrokeColor = "#1b1b1b";
    public String corridorColor = "#007dcaff";
    public int corridorThickness = 6;
}
