/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.files;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.schematic.TardisSchematicGZip;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * The Unified Intelligence Taskforce — formerly known as the United Nations Intelligence Taskforce, and more usually
 * called UNIT — was a military organisation which operated under the auspices of the United Nations. Its remit was to
 * investigate and combat paranormal and extraterrestrial threats to the Earth. UNIT was not the only alien defence
 * organisation, but it was the one with which the Doctor had the closest personal involvement.
 *
 * @author eccentric_nz
 */
public class TardisRoomMap {

    private final TardisPlugin plugin;

    public TardisRoomMap(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads schematic data into a Map. This allows the rooms_require_blocks option to check the room block counts.
     */
    public void load() {
        String defaultbasepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        Objects.requireNonNull(plugin.getRoomsConfig().getConfigurationSection("rooms")).getKeys(false).forEach((r) -> {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                boolean user = plugin.getRoomsConfig().getBoolean("rooms." + r + ".user");
                String basepath = (user) ? userbasepath : defaultbasepath;
                String lower = r.toLowerCase(Locale.ENGLISH);
                File sch = new File(basepath + lower + ".tschm");
                if (sch.exists()) {
                    makeRoomMap(basepath + lower, r);
                } else {
                    plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + lower + ".tschm was not found in '" + basepath + "' and was disabled!");
                    plugin.getRoomsConfig().set("rooms." + r + ".enabled", false);
                    try {
                        plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    /**
     * Reads a tardis schematic file and maps the data for rooms_require_blocks.
     *
     * @param fileStr the schematic file to read
     * @param s       the schematic name
     * @return true if the schematic was loaded successfully
     */
    public boolean makeRoomMap(String fileStr, String s) {
        HashMap<String, Integer> blockTypes = new HashMap<>();
        File f = new File(fileStr + ".tschm");
        if (!f.exists()) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + "Could not find a schematic with that name!");
            return false;
        }
        // get JSON
        JsonObject obj = TardisSchematicGZip.unzip(fileStr + ".tschm");
        if (obj == null) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + "The supplied file [" + fileStr + ".tschm] is not a tardis JSON schematic!");
            return false;
        } else {
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            int h = dimensions.get("height").getAsInt();
            int w = dimensions.get("width").getAsInt();
            int l = dimensions.get("length").getAsInt();
            // get input array
            JsonArray arr = obj.get("input").getAsJsonArray();
            // loop like crazy
            for (int level = 0; level < h; level++) {
                JsonArray floor = arr.get(level).getAsJsonArray();
                for (int row = 0; row < w; row++) {
                    JsonArray r = floor.get(row).getAsJsonArray();
                    for (int col = 0; col < l; col++) {
                        JsonObject c = r.get(col).getAsJsonObject();
                        if (!(c.get("data").getAsString().contains("minecraft"))) {
                            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + "The supplied file [" + fileStr + ".tschm] needs updating to a tardis v4 schematic and was disabled!");
                            plugin.getRoomsConfig().set("rooms." + s + ".enabled", false);
                            try {
                                plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                            } catch (IOException ignored) {
                            }
                            return false;
                        }
                        String bid = getMaterialAsString(c.get("data").getAsString());
                        if (plugin.getBuildKeeper().getIgnoreBlocks().contains(bid)) {
                            continue;
                        }
                        if (plugin.getBuildKeeper().getBlockConversion().containsKey(bid)) {
                            bid = plugin.getBuildKeeper().getBlockConversion().get(bid);
                        }
                        if (blockTypes.containsKey(bid)) {
                            Integer count = blockTypes.get(bid) + 1;
                            blockTypes.put(bid, count);
                        } else {
                            blockTypes.put(bid, 1);
                        }
                    }
                    plugin.getBuildKeeper().getRoomBlockCounts().put(s, blockTypes);
                }
            }
            return true;
        }
    }

    private String getMaterialAsString(String data) {
        String[] square = data.split("\\[");
        String[] keyed = square[0].split(":");
        return keyed[1].toUpperCase();
    }
}
