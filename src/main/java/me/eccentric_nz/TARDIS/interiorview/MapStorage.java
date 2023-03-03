package me.eccentric_nz.TARDIS.interiorview;

import com.google.gson.Gson;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapStorage {

    private final Gson gson = new Gson();

    public void store(int id, Color[][] data) {
        File file = new File (TARDIS.plugin.getDataFolder(), "interior_views" + File.separator + "view_" + id + ".json");
        try {
            gson.toJson(data, new FileWriter(file));
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error writing to mapId: " + id);
        }
    }
}
