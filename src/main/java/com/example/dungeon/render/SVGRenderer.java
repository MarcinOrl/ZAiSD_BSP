package com.example.dungeon.render;

import com.example.dungeon.bsp.Leaf;
import com.example.dungeon.map.Corridor;
import com.example.dungeon.map.Room;

import java.io.FileWriter;
import java.util.List;

public class SVGRenderer {
    private final int width, height;
    private final int corridorThickness = 6;

    public SVGRenderer(int width, int height){
        this.width = width; this.height = height;
    }

    private String header(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height + "\" viewBox=\"0 0 " + width + " " + height + "\">\n";
    }
    private String footer(){ return "</svg>\n"; }

    public void renderPartition(List<Leaf> leaves, String outPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(header());
        // background
        sb.append(String.format("<rect width=\"100%%\" height=\"100%%\" fill=\"#ffffff\"/>\n"));
        for (Leaf l : leaves) {
            sb.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"none\" stroke=\"#cccccc\" stroke-width=\"1\" />\n",
                    l.x, l.y, l.width, l.height));
        }
        sb.append(footer());
        try (FileWriter w = new FileWriter(outPath)) { w.write(sb.toString()); }
    }

    public void renderRooms(List<Room> rooms, String outPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(header());
        sb.append("<rect width=\"100%\" height=\"100%\" fill=\"#ffffff\"/>\n");
        for (Room r : rooms) {
            sb.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"#e8e8e8\" stroke=\"#333333\" stroke-width=\"1\" />\n",
                    r.x, r.y, r.width, r.height));
        }
        sb.append(footer());
        try (FileWriter w = new FileWriter(outPath)) { w.write(sb.toString()); }
    }

    public void renderRoomsAndCorridors(List<Room> rooms, List<Corridor> corridors, String outPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(header());
        sb.append("<rect width=\"100%\" height=\"100%\" fill=\"#ffffff\"/>\n");

        // corridors first
        for (Corridor c : corridors) {
            if (c.isHorizontal()) {
                int x = Math.min(c.x1, c.x2);
                int w = Math.abs(c.x2 - c.x1);
                int y = c.y1 - corridorThickness/2;
                sb.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"#b0b0b0\" />\n", x, y, Math.max(1,w), corridorThickness));
            } else {
                int y = Math.min(c.y1, c.y2);
                int h = Math.abs(c.y2 - c.y1);
                int x = c.x1 - corridorThickness/2;
                sb.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"#b0b0b0\" />\n", x, y, corridorThickness, Math.max(1,h)));
            }
        }

        // rooms on top
        for (Room r : rooms) {
            sb.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"#e8e8e8\" stroke=\"#333333\" stroke-width=\"1\" />\n",
                    r.x, r.y, r.width, r.height));
        }

        sb.append(footer());
        try (FileWriter w = new FileWriter(outPath)) { w.write(sb.toString()); }
    }
}
