/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.discovery;

import com.google.gson.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Corner {

    public Material[][][] load(TARDIS plugin) {
        try {
            // get JSON
            InputStream stream = plugin.getResource("corner.json");
            if (stream != null) {
                String s = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(s).getAsJsonObject();
                JsonArray array = obj.get("input").getAsJsonArray();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Material.class, new MaterialDeserializer())
                        .create();
                return gson.fromJson(array, Material[][][].class);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public void save(TARDIS plugin, Player player) {
        UUID uuid = player.getUniqueId();
        // check they have selected start and end blocks
        if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NO_START");
            return;
        }
        if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NO_END");
            return;
        }
        // get the world
        World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
        String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getKey().getKey();
        if (!w.getKey().getKey().equals(chk_w)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_WORLD");
            return;
        }
        // get the raw coords
        int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
        int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
        int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
        int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
        int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
        int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
        // get the min & max coords
        int minx = Math.min(sx, ex);
        int maxx = Math.max(sx, ex);
        int miny = Math.min(sy, ey);
        int maxy = Math.max(sy, ey);
        int minz = Math.min(sz, ez);
        int maxz = Math.max(sz, ez);
        // create a JSON object for relative position
        JsonObject relative = new JsonObject();
        int px = player.getLocation().getBlockX() - minx;
        int py = player.getLocation().getBlockY() - miny;
        int pz = player.getLocation().getBlockZ() - minz;
        relative.addProperty("x", px);
        relative.addProperty("y", py);
        relative.addProperty("z", pz);
        // create a JSON object for dimensions
        JsonObject dimensions = new JsonObject();
        int width = (maxx - minx) + 1;
        int height = (maxy - miny) + 1;
        int length = (maxz - minz) + 1;
        dimensions.addProperty("width", width);
        dimensions.addProperty("height", height);
        dimensions.addProperty("length", length);
        if (width != length) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_SQUARE");
            plugin.getMessenger().message(player, TardisModule.TARDIS, "Dimensions are: (x,z)" + width + ", " + length);
            return;
        }
        if (width % 16 != 0) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_MULTIPLE");
            return;
        }
        // create JSON arrays for block data
        JsonArray levels = new JsonArray();
        // loop through the blocks inside this cube
        for (int l = miny; l <= maxy; l++) {
            JsonArray rows = new JsonArray();
            for (int r = minx; r <= maxx; r++) {
                JsonArray columns = new JsonArray();
                for (int c = minz; c <= maxz; c++) {
                    Block b = w.getBlockAt(r, l, c);
                    String type = b.getType().toString();
                    // ignore light blocks
                    if (type.equals("LIGHT")) {
                        type = "AIR";
                    }
                    JsonPrimitive material = new JsonPrimitive(type);
                    columns.add(material);
                }
                rows.add(columns);
            }
            levels.add(rows);
        }
        JsonObject schematic = new JsonObject();
        schematic.add("relative", relative);
        schematic.add("dimensions", dimensions);
        schematic.add("input", levels);
        String output = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + "corner.json";
        File file = new File(output);
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file), 16 * 1024)) {
                bw.write(schematic.toString());
            }
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_SAVED", "corner");
        } catch (IOException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_ERROR");
        }
    }
}
