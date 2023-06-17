/*
 * Copyright (C) 2023 eccentric_nz
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.data.BlockData;

/**
 * The Unified Intelligence Taskforce — formerly known as the United Nations
 * Intelligence Taskforce, and more usually called UNIT — was a military
 * organisation which operated under the auspices of the United Nations. Its
 * remit was to investigate and combat paranormal and extraterrestrial threats
 * to the Earth. UNIT was not the only alien defence organisation, but it was
 * the one with which the Doctor had the closest personal involvement.
 *
 * @author eccentric_nz
 */
public class TARDISRoomMap {

    private final TARDIS plugin;

    public TARDISRoomMap(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads schematic data into a Map. This allows the rooms_require_blocks
     * option to check the room block counts.
     */
    public void load() {
        plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false).forEach((r) -> {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                boolean success = makeRoomMap(r.toLowerCase(Locale.ENGLISH), r, plugin.getRoomsConfig().getBoolean("rooms." + r + ".user"));
                if (!success) {
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
     * Reads a TARDIS schematic file and maps the data for rooms_require_blocks.
     *
     * @param fileName the schematic file name to read
     * @param s        the schematic name
     * @param user     whether the room has been added by a user
     * @return true if the schematic was loaded successfully
     */
    public boolean makeRoomMap(String fileName, String s, boolean user) {
        HashMap<String, Integer> blockTypes = new HashMap<>();
        // get JSON
        JsonObject obj = TARDISSchematicGZip.getObject(plugin, "rooms", fileName, user);
        if (obj == null) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "The supplied file [" + fileName + ".tschm] is not a TARDIS JSON schematic!");
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
                            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, ChatColor.RED + "The supplied file [" + fileName + ".tschm] needs updating to a TARDIS v4 schematic and was disabled!");
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
        BlockData block = plugin.getServer().createBlockData(data);
        String bid = block.getMaterial().toString();
        if (plugin.getBuildKeeper().getBlockConversion().containsKey(bid)) {
            bid = plugin.getBuildKeeper().getBlockConversion().get(bid);
        }
        return bid;
    }
}
