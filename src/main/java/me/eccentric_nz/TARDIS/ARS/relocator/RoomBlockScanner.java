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
package me.eccentric_nz.TARDIS.ARS.relocator;

import com.destroystokyo.paper.MaterialTags;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.ARS.JettisonSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.desktop.BlockScannerData;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class RoomBlockScanner {

    private final TARDIS plugin;
    private final String room;
    private final JettisonSlot jettison;
    private final UUID uuid;
    private int count = 0;

    public RoomBlockScanner(TARDIS plugin, String room, JettisonSlot jettison, UUID uuid) {
        this.plugin = plugin;
        this.room = room;
        this.jettison = jettison;
        this.uuid = uuid;
    }

    public BlockScannerData check() {
        // get JSON
        JsonObject obj = TARDISSchematicGZip.getObject(plugin, "rooms", room.toLowerCase(Locale.ROOT), false);
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
        int starty = jettison.getY();
        int startx = jettison.getX();
        int startz = jettison.getZ();
        World world = jettison.getChunk().getWorld();
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
                    if (MaterialTags.INFESTED_BLOCKS.isTagged(type)) {
                        type = Material.AIR;
                    }
                    if (type.equals(Material.BEDROCK)) {
                        type = Material.GLASS;
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
                        if (type == Material.ORANGE_WOOL) {
                            String[] tsplit = type.toString().split("_");
                            String m;
                            if (tsplit.length > 2) {
                                m = tsplit[0] + "_" + tsplit[1] + "_" + use_clay;
                            } else {
                                m = tsplit[0] + "_" + use_clay;
                            }
                            type = Material.getMaterial(m);
                        }
                        if (type.isAir()) {
                            v--;
                        }
                        Material material = b.getType();
                        if (!material.equals(Material.BARRIER) && !material.equals(type) && !(material.isAir() && type.isAir())) {
                            count++;
                        }
                    }
                }
            }
        }
        int changed = (int) ((count / v) * 100);
        // should return false if changed is higher than config
        boolean allow = changed < plugin.getConfig().getInt("desktop.block_change_percent");
        return new BlockScannerData(changed, count, v, allow, "");
    }
}
