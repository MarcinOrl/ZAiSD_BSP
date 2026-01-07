package com.example.dungeon;

import com.example.dungeon.config.Config;
import com.example.dungeon.util.ConfigParser;
import com.example.dungeon.bsp.BSPTree;
import com.example.dungeon.render.SVGRenderer;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        String configPath = args.length > 0 ? args[0] : "example-config.json";
        Config cfg = ConfigParser.parse(configPath);

        // prepare out dir
        Path out = Path.of(cfg.outDir);
        Files.createDirectories(out);

        BSPTree tree = new BSPTree(0,0,cfg.width,cfg.height,cfg.minLeaf,cfg.maxLeaf,cfg.seed);
        tree.splitAll();

        // pass full config into renderer
        SVGRenderer renderer = new SVGRenderer(cfg);

        // stage 1: partition
        String f1 = out.resolve(cfg.seed + "_01_partition.svg").toString();
        renderer.renderPartition(tree.getLeaves(), f1);
        System.out.println("Wrote: " + f1);

        // stage 2: rooms
        tree.createRooms(cfg.minRoom, cfg.maxRoom, cfg.padding);
        String f2 = out.resolve(cfg.seed + "_02_rooms.svg").toString();
        renderer.renderRooms(tree.getRooms(), f2);
        System.out.println("Wrote: " + f2);

        // stage 3: corridors
        tree.connectRooms();
        String f3 = out.resolve(cfg.seed + "_03_rooms_corridors.svg").toString();
        renderer.renderRoomsAndCorridors(tree.getRooms(), tree.getCorridors(), f3);
        System.out.println("Wrote: " + f3);

        System.out.println("Done.");
    }
}
