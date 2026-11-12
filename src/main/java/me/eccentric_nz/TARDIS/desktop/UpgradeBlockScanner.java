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
package me.eccentric_nz.TARDIS.desktop;

import com.destroystokyo.paper.MaterialTags;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPositioning;
import me.eccentric_nz.TARDIS.builders.interior.TIPSData;
import me.eccentric_nz.TARDIS.builders.interior.Template;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class UpgradeBlockScanner {

    private final TARDIS plugin;
    private final UpgradeData tud;
    private final UUID uuid;
    private int count = 0;

    public UpgradeBlockScanner(TARDIS plugin, UpgradeData tud, UUID uuid) {
        this.plugin = plugin;
        this.tud = tud;
        this.uuid = uuid;
    }

    public BlockScannerData check() {
        // get JSON
        JsonObject obj = SchematicGZip.getObject(plugin, "consoles", tud.getPrevious().getPermission(), tud.getPrevious().isCustom());
        if (obj == null) {
            return null;
        }
        // get dimensions
        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
        int h = dimensions.get("height").getAsInt();
        int w = dimensions.get("width").getAsInt();
        int l = dimensions.get("length").getAsInt();
        float v = h * w * l;
        // calculate startx, starty, startz
        CheckData start = getStart();
        if (start != null) {
            int startx = start.x();
            int startz = start.z();
            int starty = tud.getPrevious().getStartY();
            World world = start.world();
            Material wall_type = start.wall();
            Material floor_type = start.floor();
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
                        if (MaterialTags.INFESTED_BLOCKS.isTagged(type)) {
                            type = Material.AIR;
                        }
                        if (type.equals(Material.BEDROCK)) {
                            type = Material.GLASS;
                            beacon = world.getKey().asString() + ":" + x + ":" + y + ":" + z;
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
                                        type = floor_type;
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
                                case BLUE_WOOL -> type = Material.BARRIER;
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
                        Material material = world.getBlockAt(x, y, z).getType();
                        if (!material.equals(Material.BARRIER) && !material.equals(type) && !(material.isAir() && type.isAir())) {
                            count++;
                        }
                    }
                }
            }
            int changed = (int) ((count / v) * 100);
            // should return false if changed is higher than config
            boolean allow = changed < plugin.getConfig().getInt("desktop.block_change_percent");
            return new BlockScannerData(changed, count, v, allow, beacon);
        } else {
            return null;
        }
    }

    public BlockScannerData fadeCheck() {
        // FADE desktop do alternate check
        // get JSON
        String[][][] arr = new Template().load(plugin);
        // get dimensions
        int h = arr.length;
        int w = arr[0].length;
        int l = arr[0][0].length;
        float v = h * w * l;
        // calculate startx, starty, startz
        CheckData start = getStart();
        if (start != null) {
            int startx = start.x();
            int starty = tud.getPrevious().getStartY();
            int startz = start.z();
            World world = start.world();
            Material wall_type = start.wall();
            Material floor_type = start.floor();
            // get input array
            for (int level = 0; level < h; level++) {
                for (int row = 0; row < w; row++) {
                    for (int col = 0; col < l; col++) {
                        int x = startx + row;
                        int y = starty + level;
                        int z = startz + col;
                        BlockData data = plugin.getServer().createBlockData(arr[level][row][col]);
                        Material type = data.getMaterial();
                        if (type.isAir()) {
                            v--;
                        } else if (world.getBlockAt(x, y, z).getType().isAir()) {
                            count++;
                        }
                    }
                }
            }
            int changed = (int) ((count / v) * 100);
            // should return false if changed is higher than config
            boolean allow = changed < plugin.getConfig().getInt("desktop.block_change_percent");
            return new BlockScannerData(changed, count, v, allow, "");
        } else {
            return null;
        }
    }

    private CheckData getStart() {
        int x, z;
        World world;
        Material w;
        Material f;
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            int slot = tardis.getTIPS();
            if (slot != -1000001) { // default world - use TIPS
                TARDISInteriorPositioning tintpos = new TARDISInteriorPositioning(plugin);
                TIPSData pos = tintpos.getTIPSData(slot);
                x = pos.getCentreX();
                z = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                x = gsl[0];
                z = gsl[2];
            }
            // get wall/floor block prefs from database...
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                w = Material.getMaterial(rsp.getWall());
                f = Material.getMaterial(rsp.getFloor());
            } else {
                w = Material.ORANGE_WOOL;
                f = Material.LIGHT_GRAY_WOOL;
            }
            return new CheckData(x, z, world, w, f);
        }
        return null;
    }
}
