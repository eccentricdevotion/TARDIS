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
package me.eccentric_nz.tardis.commands.tardis;

import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetChunks;
import me.eccentric_nz.tardis.database.resultset.ResultSetLamps;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.schematic.TardisSchematicGZip;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The TARDIS scanner was the main method for the occupants of the vessel to observe the outside environment. The
 * appearance and specifications of the scanner system varied significantly in the course of the Doctor's travels.
 *
 * @author eccentric_nz
 */
class TardisLampsCommand {

    private final TardisPlugin plugin;

    TardisLampsCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Updates TARDISes from pre-malfunction plugin versions so that the lamps can flash.
     *
     * @param owner the Timelord of the TARDIS
     * @return true if the TARDIS has not been updated, otherwise false
     */

    boolean addLampBlocks(Player owner) {
        // check they have permission
        if (!TardisPermission.hasPermission(owner, "tardis.update")) {
            TardisMessage.send(owner, "NO_PERMS");
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
                TardisMessage.send(owner, "LAMP_DELETE");
                HashMap<String, Object> wheredel = new HashMap<>();
                wheredel.put("tardis_id", id);
                plugin.getQueryFactory().doDelete("lamps", wheredel);
            }
            // get the TARDIS console chunks
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            ResultSetChunks rsc = new ResultSetChunks(plugin, wherec, true);
            if (rsc.resultSet()) {
                int starty, endy;
                Schematic schm = tardis.getSchematic();
                Material lampon = (schm.hasLanterns()) ? Material.SEA_LANTERN : Material.REDSTONE_LAMP;
                // player preference
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, owner.getUniqueId().toString());
                if (rsp.resultSet() && rsp.isLanternsOn()) {
                    lampon = Material.SEA_LANTERN;
                }
                String directory = (schm.isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + schm.getPermission() + ".tschm";
                // get JSON
                JsonObject obj = TardisSchematicGZip.unzip(path);
                // get dimensions
                assert obj != null;
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                int h = dimensions.get("height").getAsInt();
                starty = TardisConstants.HIGHER.contains(schm.getPermission()) ? 65 : 64;
                endy = starty + h;
                ArrayList<HashMap<String, String>> data = rsc.getData();
                // loop through the chunks
                for (HashMap<String, String> map : data) {
                    String w = map.get("world");
                    World world = TardisAliasResolver.getWorldFromAlias(w);
                    int x = TardisNumberParsers.parseInt(map.get("x"));
                    int z = TardisNumberParsers.parseInt(map.get("z"));
                    assert world != null;
                    Chunk chunk = world.getChunkAt(x, z);
                    // find the lamps in the chunks
                    int bx = chunk.getX() << 4;
                    int bz = chunk.getZ() << 4;
                    for (int xx = bx; xx < bx + 16; xx++) {
                        for (int zz = bz; zz < bz + 16; zz++) {
                            for (int yy = starty; yy < endy; yy++) {
                                Material mat = world.getBlockAt(xx, yy, zz).getType();
                                if (mat.equals(lampon)) {
                                    String lamp = w + ":" + xx + ":" + yy + ":" + zz;
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("tardis_id", id);
                                    set.put("location", lamp);
                                    plugin.getQueryFactory().doInsert("lamps", set);
                                    TardisMessage.send(owner, "LAMP_ADD", (xx + ":" + yy + ":" + zz));
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            TardisMessage.send(owner, "NOT_A_TIMELORD");
            return false;
        }
    }
}
