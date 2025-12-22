package com.example.dungeon.util;

import com.example.dungeon.config.Config;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.Reader;

public class ConfigParser {
    public static Config parse(String path) throws Exception {
        Gson g = new Gson();
        try (Reader r = new FileReader(path)) {
            return g.fromJson(r, Config.class);
        }
    }
}
