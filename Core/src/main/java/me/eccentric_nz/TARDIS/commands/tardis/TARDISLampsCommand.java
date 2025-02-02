/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.TARDISChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * The TARDIS scanner was the main method for the occupants of the vessel to
 * observe the outside environment. The appearance and specifications of the
 * scanner system varied significantly in the course of the Doctor's travels.
 *
 * @author eccentric_nz
 */
class TARDISLampsCommand {

    private final TARDIS plugin;

    TARDISLampsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Updates TARDISes from pre-malfunction plugin versions so that the lamps
     * can flash.
     *
     * @param owner the Timelord of the TARDIS
     * @return true if the TARDIS has not been updated, otherwise false
     */
    boolean addLampBlocks(Player owner) {
        // check they have permission
        if (!TARDISPermission.hasPermission(owner, "tardis.update")) {
            plugin.getMessenger().send(owner, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", owner.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            // check if they have already got lamp records
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, false);
            if (rsl.resultSet()) {
                plugin.getMessenger().send(owner, TardisModule.TARDIS, "LAMP_DELETE");
                HashMap<String, Object> wheredel = new HashMap<>();
                wheredel.put("tardis_id", id);
                plugin.getQueryFactory().doDelete("lamps", wheredel);
            }
            // get the TARDIS console chunk
            String[] tc = tardis.getChunk().split(":");
            int cx = TARDISNumberParsers.parseInt(tc[1]);
            int cz = TARDISNumberParsers.parseInt(tc[2]);
            World world = TARDISAliasResolver.getWorldFromAlias(tc[0]);
            Chunk startChunk = world.getChunkAt(cx, cz);
            int starty, endy;
            Schematic schm = tardis.getSchematic();
            // get JSON
            JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
            if (obj != null) {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                int h = dimensions.get("height").getAsInt();
                if (schm.getPermission().equals("mechanical") || schm.getPermission().equals("cursed")) {
                    starty = 62;
                } else if (TARDISConstants.HIGHER.contains(schm.getPermission())) {
                    starty = 65;
                } else {
                    starty = 64;
                }
                endy = starty + h;
                String w = world.getName();
                // loop through the chunks
                for (Chunk chunk : TARDISChunkUtils.getConsoleChunks(startChunk, tardis.getSchematic())) {
                    // find the lamps in the chunks
                    int bx = chunk.getX() << 4;
                    int bz = chunk.getZ() << 4;
                    for (int xx = bx; xx < bx + 16; xx++) {
                        for (int zz = bz; zz < bz + 16; zz++) {
                            for (int yy = starty; yy < endy; yy++) {
                                Material mat = world.getBlockAt(xx, yy, zz).getType();
                                if (mat.equals(Material.LIGHT)) {
                                    String lamp = w + ":" + xx + ":" + yy + ":" + zz;
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("tardis_id", id);
                                    set.put("location", lamp);
                                    plugin.getQueryFactory().doInsert("lamps", set);
                                    plugin.getMessenger().send(owner, TardisModule.TARDIS, "LAMP_ADD", (xx + ":" + yy + ":" + zz));
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            plugin.getMessenger().send(owner, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return false;
        }
    }
}
