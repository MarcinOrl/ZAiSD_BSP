package com.example.dungeon.render;

import com.example.dungeon.bsp.Leaf;
import com.example.dungeon.map.Corridor;
import com.example.dungeon.map.Room;
import com.example.dungeon.config.Config;

import java.io.FileWriter;
import java.util.List;

public class SVGRenderer {
    private final int width, height;
    private final Config cfg;

    public SVGRenderer(Config cfg){
        this.cfg = cfg;
        this.width = cfg.width;
        this.height = cfg.height;
    }

    private String header(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height +
                "\" viewBox=\"0 0 " + width + " " + height + "\">\n";
    }

    private String footer(){
        return "</svg>\n";
    }

    public void renderPartition(List<Leaf> leaves, String outPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(header());

        // background
        sb.append(String.format(
                "<rect width=\"100%%\" height=\"100%%\" fill=\"%s\"/>\n",
                cfg.backgroundColor));

        for (Leaf l : leaves) {
            sb.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" stroke=\"%s\" stroke-width=\"1\" />\n",
                    l.x, l.y, l.width, l.height,
                    cfg.leafFillColor, cfg.leafStrokeColor));
        }

        sb.append(footer());
        try (FileWriter w = new FileWriter(outPath)) {
            w.write(sb.toString());
        }
    }

    public void renderRooms(List<Room> rooms, String outPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(header());

        sb.append(String.format(
                "<rect width=\"100%%\" height=\"100%%\" fill=\"%s\"/>\n",
                cfg.backgroundColor));

        for (Room r : rooms) {
            sb.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" stroke=\"%s\" stroke-width=\"1\" />\n",
                    r.x, r.y, r.width, r.height,
                    cfg.roomFillColor, cfg.roomStrokeColor));
        }

        sb.append(footer());
        try (FileWriter w = new FileWriter(outPath)) {
            w.write(sb.toString());
        }
    }

    public void renderRoomsAndCorridors(List<Room> rooms, List<Corridor> corridors, String outPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(header());

        sb.append(String.format(
                "<rect width=\"100%%\" height=\"100%%\" fill=\"%s\"/>\n",
                cfg.backgroundColor));

        // corridors first
        int t = Math.max(1, cfg.corridorThickness);
        for (Corridor c : corridors) {
            if (c.isHorizontal()) {
                int x = Math.min(c.x1, c.x2);
                int wRect = Math.max(1, Math.abs(c.x2 - c.x1));
                int y = c.y1 - t / 2;

                sb.append(String.format(
                        "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" />\n",
                        x, y, wRect, t, cfg.corridorColor));
            } else {
                int y = Math.min(c.y1, c.y2);
                int hRect = Math.max(1, Math.abs(c.y2 - c.y1));
                int x = c.x1 - t / 2;

                sb.append(String.format(
                        "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" />\n",
                        x, y, t, hRect, cfg.corridorColor));
            }
        }

        // rooms on top
        for (Room r : rooms) {
            sb.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" stroke=\"%s\" stroke-width=\"1\" />\n",
                    r.x, r.y, r.width, r.height,
                    cfg.roomFillColor, cfg.roomStrokeColor));
        }

        sb.append(footer());
        try (FileWriter w = new FileWriter(outPath)) {
            w.write(sb.toString());
        }
    }
}
