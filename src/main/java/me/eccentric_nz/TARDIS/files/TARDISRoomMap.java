/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * The Unified Intelligence Taskforce — formerly known as the United Nations Intelligence Taskforce, and more usually
 * called UNIT — was a military organisation which operated under the auspices of the United Nations. Its remit was to
 * investigate and combat paranormal and extraterrestrial threats to the Earth. UNIT was not the only alien defence
 * organisation, but it was the one with which the Doctor had the closest personal involvement.
 *
 * @author eccentric_nz
 */
public class TARDISRoomMap {

    private final TARDIS plugin;

    public TARDISRoomMap(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads schematic data into a Map. This allows the rooms_require_blocks option to check the room block counts.
     */
    public void load() {
        String defaultbasepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false).forEach((r) -> {
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
                }
            }
        });
    }

    /**
     * Reads a TARDIS schematic file and maps the data for rooms_require_blocks.
     *
     * @param fileStr the schematic file to read
     * @param s       the schematic name
     * @return true if the schematic was loaded successfully
     */
    public boolean makeRoomMap(String fileStr, String s) {
        HashMap<String, Integer> blockTypes = new HashMap<>();
        File f = new File(fileStr + ".tschm");
        if (!f.exists()) {
            plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
            return false;
        }
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(fileStr + ".tschm");
        if (obj == null) {
            plugin.debug(plugin.getPluginName() + "The supplied file [" + fileStr + ".tschm] is not a TARDIS JSON schematic!");
            return false;
        } else {
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            int h = dimensions.getInt("height");
            int w = dimensions.getInt("width");
            int l = dimensions.getInt("length");
            // get input array
            JSONArray arr = (JSONArray) obj.get("input");
            // loop like crazy
            for (int level = 0; level < h; level++) {
                JSONArray floor = (JSONArray) arr.get(level);
                for (int row = 0; row < w; row++) {
                    JSONArray r = (JSONArray) floor.get(row);
                    for (int col = 0; col < l; col++) {
                        JSONObject c = (JSONObject) r.get(col);
                        String bid = getMaterialAsString(c.getString("data"));
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
