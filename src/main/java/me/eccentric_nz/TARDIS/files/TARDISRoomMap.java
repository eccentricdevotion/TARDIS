/*
 * Copyright (C) 2014 eccentric_nz
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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.ChatColor;

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
    private final HashMap<String, String> blockConversion = new HashMap<String, String>();
    private final List<String> ignoreBlocks = Arrays.asList(new String[]{"AIR", "BEDROCK", "WATER", "STATIONARY_WATER", "LAVA", "STATIONARY_LAVA", "GOLD_ORE", "SPONGE", "PISTON_EXTENSION", "MOB_SPAWNER", "ICE"});

    public TARDISRoomMap(TARDIS plugin) {
        this.plugin = plugin;
        blockConversion.put("STONE", "COBBLESTONE");
        blockConversion.put("GRASS", "DIRT");
        blockConversion.put("LEAVES", "SAPLING");
        blockConversion.put("LEAVES_2", "SAPLING");
        blockConversion.put("WEB", "STRING");
        blockConversion.put("LONG_GRASS", "SEEDS");
        blockConversion.put("DOUBLE_STEP", "STEP");
        blockConversion.put("REDSTONE_WIRE", "REDSTONE");
        blockConversion.put("CROPS", "SEEDS");
        blockConversion.put("SOIL", "DIRT");
        blockConversion.put("SIGN_POST", "SIGN");
        blockConversion.put("WOODEN_DOOR", "WOOD_DOOR");
        blockConversion.put("WALL_SIGN", "SIGN");
        blockConversion.put("IRON_DOOR_BLOCK", "IRON_DOOR");
        blockConversion.put("REDSTONE_TORCH_OFF", "REDSTONE_TORCH_ON");
        blockConversion.put("SUGAR_CANE_BLOCK", "SUGAR_CANE");
        blockConversion.put("CAKE_BLOCK", "LEVER");
        blockConversion.put("DIODE_BLOCK_OFF", "DIODE");
        blockConversion.put("DIODE_BLOCK_ON", "DIODE");
        blockConversion.put("HUGE_MUSHROOM_1", "BROWN_MUSHROOM");
        blockConversion.put("HUGE_MUSHROOM_2", "RED_MUSHROOM");
        blockConversion.put("PUMPKIN_STEM", "PUMPKIN_SEEDS");
        blockConversion.put("MELON_STEM", "MELON_SEEDS");
        blockConversion.put("MYCEL", "DIRT");
        blockConversion.put("NETHER_WARTS", "NETHER_STALK");
        blockConversion.put("BREWING_STAND", "BREWING_STAND_ITEM");
        blockConversion.put("CAULDRON", "CAULDRON_ITEM");
        blockConversion.put("REDSTONE_LAMP_ON", "REDSTONE_LAMP_OFF");
        blockConversion.put("COCOA", "INK_SACK");
        blockConversion.put("FLOWER_POT", "FLOWER_POT_ITEM");
        blockConversion.put("CARROT", "CARROT_ITEM");
        blockConversion.put("POTATO", "POTATO_ITEM");
        blockConversion.put("REDSTONE_COMPARATOR_OFF", "REDSTONE_COMPARATOR");
        blockConversion.put("REDSTONE_COMPARATOR_ON", "REDSTONE_COMPARATOR");
    }

    /**
     * Loads schematic data into a Map. This allows the rooms_require_blocks
     * option to check the room block counts.
     */
    public void load() {
        String defaultbasepath = plugin.getDataFolder() + File.separator + "schematics" + File.separator;
        String userbasepath = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator;
        for (String r : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                boolean user = plugin.getRoomsConfig().getBoolean("rooms." + r + ".user");
                String basepath = (user) ? userbasepath : defaultbasepath;
                String lower = r.toLowerCase(Locale.ENGLISH);
                File sch = new File(basepath + lower + ".tschm");
                if (sch.exists()) {
                    makeRoomMap(basepath + lower, r);
                } else {
                    plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + lower + ".tschm was not found in 'user_schematics' and was disabled!");
                    plugin.getRoomsConfig().set("rooms." + r + ".enabled", false);
                }
            }
        }
    }

    /**
     * Reads a TARDIS schematic file and maps the data for rooms_require_blocks.
     *
     * @param fileStr the schematic file to read
     * @param s the schematic name
     */
    public void makeRoomMap(String fileStr, String s) {
        HashMap<String, Integer> blockIDs = new HashMap<String, Integer>();
        File f = new File(fileStr + ".tschm");
        if (!f.exists()) {
            plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
            return;
        }
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(fileStr + ".tschm");
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
                    String bid = c.getString("type");
                    if (ignoreBlocks.contains(bid)) {
                        continue;
                    }
                    if (blockConversion.containsKey(bid)) {
                        bid = blockConversion.get(bid);
                    }
                    if (bid.equals("WOOL") && (c.getByte("data") == 1 || c.getByte("data") == 8)) {
                        String bstr = bid + ":" + c.getByte("data");
                        if (blockIDs.containsKey(bstr)) {
                            Integer count = blockIDs.get(bstr) + 1;
                            blockIDs.put(bstr, count);
                        } else {
                            blockIDs.put(bstr, 1);
                        }
                    } else {
                        if (blockIDs.containsKey(bid)) {
                            Integer count = blockIDs.get(bid) + 1;
                            blockIDs.put(bid, count);
                        } else {
                            blockIDs.put(bid, 1);
                        }
                    }
                }
                plugin.getBuildKeeper().getRoomBlockCounts().put(s, blockIDs);
            }
        }
    }
}
