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
package me.eccentric_nz.TARDIS.desktop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author eccentric_nz
 */
public class TARDISUpgradeBlockScanner {

    private final TARDIS plugin;
    private final TARDISUpgradeData tud;
    private final UUID uuid;
    private int count = 0;

    public TARDISUpgradeBlockScanner(TARDIS plugin, TARDISUpgradeData tud, UUID uuid) {
        this.plugin = plugin;
        this.tud = tud;
        this.uuid = uuid;
    }

    public TARDISBlockScannerData check() {
        String directory = (tud.getPrevious().isCustom()) ? "user_schematics" : "schematics";
        String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getPrevious().getPermission() + ".tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug("Could not find a schematic with that name!");
            return null;
        }
        // get JSON
        JsonObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
        int h = dimensions.get("height").getAsInt();
        int w = dimensions.get("width").getAsInt();
        int l = dimensions.get("length").getAsInt();
        float v = h * w * l;
        // calculate startx, starty, startz
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            int startz;
            int startx;
            if (slot != -1000001) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                startz = gsl[2];
            }
            int starty;
            if (tud.getPrevious().getPermission().equals("mechanical")) {
                starty = 62;
            } else if (TARDISConstants.HIGHER.contains(tud.getPrevious().getPermission())) {
                starty = 65;
            } else {
                starty = 64;
            }
            World world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            Material wall_type;
            Material floor_type;
            // get wall/floor block prefs from database...
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                wall_type = Material.getMaterial(rsp.getWall());
                floor_type = Material.getMaterial(rsp.getFloor());
            } else {
                wall_type = Material.ORANGE_WOOL;
                floor_type = Material.LIGHT_GRAY_WOOL;
            }
            String beacon = "";
            // get input array
            JsonArray arr = obj.get("input").getAsJsonArray();
            for (int level = 0; level < h; level++) {
                JsonArray floor = arr.get(level).getAsJsonArray();
                for (int row = 0; row < w; row++) {
                    JsonArray r = (JsonArray) floor.get(row);
                    for (int col = 0; col < l; col++) {
                        JsonObject c = r.get(col).getAsJsonObject();
                        int x = startx + row;
                        int y = starty + level;
                        int z = startz + col;
                        BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
                        Material type = data.getMaterial();
                        Block b = world.getBlockAt(x, y, z);
                        if (type.equals(Material.ORANGE_WOOL)) {
                            type = wall_type;
                        }
                        if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                            type = floor_type;
                        }
                        if (type.equals(Material.SPONGE)) {
                            type = Material.AIR;
                        }
                        if (type.equals(Material.CAKE)) {
                            type = Material.LEVER;
                        }
                        if (type.equals(Material.SPAWNER)) {
                            type = Material.OAK_BUTTON;
                        }
                        if (type.equals(Material.MUSHROOM_STEM)) {
                            type = Material.REPEATER;
                        }
                        if (TARDISMaterials.infested.contains(type)) {
                            type = Material.AIR;
                        }
                        if (type.equals(Material.BEDROCK)) {
                            type = Material.GLASS;
                            beacon = world.getName() + ":" + x + ":" + y + ":" + z;
                        }
                        if (type.equals(Material.COMMAND_BLOCK)) {
                            type = Material.STONE_BRICKS;
                        }
                        if (type.equals(Material.JUKEBOX) || type.equals(Material.NOTE_BLOCK)) {
                            type = Material.MUSHROOM_STEM;
                        }
                        if (Tag.WOOL.isTagged(type)) {
                            // determine 'use_clay' material
                            UseClay use_clay;
                            try {
                                use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
                            } catch (IllegalArgumentException e) {
                                use_clay = UseClay.WOOL;
                            }
                            switch (type) {
                                case ORANGE_WOOL -> {
                                    if (wall_type == Material.LAPIS_BLOCK) { // if using the default Lapis Block - then use Orange Wool / Terracotta
                                        type = switch (use_clay) {
                                            case TERRACOTTA -> Material.ORANGE_TERRACOTTA;
                                            case CONCRETE -> Material.ORANGE_CONCRETE;
                                            default -> Material.ORANGE_WOOL;
                                        };
                                    } else {
                                        type = wall_type;
                                    }
                                }
                                case LIGHT_GRAY_WOOL -> {
                                    if (!tud.getSchematic().getPermission().equals("eleventh")) {
                                        if (floor_type == Material.LAPIS_BLOCK) { // if using the default Lapis Block - then use Light Grey Wool / Terracotta
                                            type = switch (use_clay) {
                                                case TERRACOTTA -> Material.LIGHT_GRAY_TERRACOTTA;
                                                case CONCRETE -> Material.LIGHT_GRAY_CONCRETE;
                                                default -> Material.LIGHT_GRAY_WOOL;
                                            };
                                        } else {
                                            type = floor_type;
                                        }
                                    } else {
                                        String[] tsplit = type.toString().split("_");
                                        String m;
                                        if (tsplit.length > 2) {
                                            m = tsplit[0] + "_" + tsplit[1] + "_" + use_clay;
                                        } else {
                                            m = tsplit[0] + "_" + use_clay;
                                        }
                                        type = Material.getMaterial(m);
                                    }
                                }
                                case BLUE_WOOL -> type = Material.MUSHROOM_STEM;
                                default -> {
                                    String[] tsplit = type.toString().split("_");
                                    String m;
                                    if (tsplit.length > 2) {
                                        m = tsplit[0] + "_" + tsplit[1] + "_" + use_clay;
                                    } else {
                                        m = tsplit[0] + "_" + use_clay;
                                    }
                                    type = Material.getMaterial(m);
                                }
                            }
                        }
                        if (type.isAir()) {
                            v--;
                        }
                        Material material = b.getType();
                        if (!material.equals(type) && !(material.isAir() && type.isAir())) {
                            count++;
                        }
                    }
                }
            }
            TARDISBlockScannerData tbsd = new TARDISBlockScannerData();
            tbsd.setCount(count);
            tbsd.setVolume(v);
            int changed = (int) ((count / v) * 100);
            tbsd.setChanged(changed);
            // should return false if changed is higher than config
            tbsd.setAllow(changed < plugin.getConfig().getInt("desktop.block_change_percent"));
            tbsd.setBeacon(beacon);
            return tbsd;
        } else {
            return null;
        }
    }
}
