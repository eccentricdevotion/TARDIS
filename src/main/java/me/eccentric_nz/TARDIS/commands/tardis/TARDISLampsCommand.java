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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * The TARDIS scanner was the main method for the occupants of the vessel to
 * observe the outside environment. The appearance and specifications of the
 * scanner system varied significantly in the course of the Doctor's travels.
 *
 * @author eccentric_nz
 */
public class TARDISLampsCommand {

    private final TARDIS plugin;

    public TARDISLampsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Updates TARDISes from pre-malfunction plugin versions so that the lamps
     * can flash.
     *
     * @param owner the Timelord of the TARDIS
     * @return true if the TARDIS has not been updated, otherwise false
     */
    @SuppressWarnings("deprecation")
    public boolean addLampBlocks(Player owner) {
        // check they have permission
        if (!owner.hasPermission("tardis.update")) {
            TARDISMessage.send(owner, "NO_PERMS");
            return false;
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", owner.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            // check if they have already got lamp records
            HashMap<String, Object> wherel = new HashMap<String, Object>();
            wherel.put("tardis_id", id);
            ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, false);
            QueryFactory qf = new QueryFactory(plugin);
            if (rsl.resultSet()) {
                TARDISMessage.send(owner, "LAMP_DELETE");
                HashMap<String, Object> wheredel = new HashMap<String, Object>();
                wheredel.put("tardis_id", id);
                qf.doDelete("lamps", wheredel);
            }
            // get the TARDIS console chunks
            HashMap<String, Object> wherec = new HashMap<String, Object>();
            wherec.put("tardis_id", id);
            ResultSetChunks rsc = new ResultSetChunks(plugin, wherec, true);
            if (rsc.resultSet()) {
                int starty, endy;
                SCHEMATIC schm = rs.getSchematic();
                String directory = (schm.equals(SCHEMATIC.CUSTOM)) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + schm.getFile();
                // get JSON
                JSONObject obj = TARDISSchematicGZip.unzip(path);
                // get dimensions
                JSONObject dimensions = (JSONObject) obj.get("dimensions");
                int h = dimensions.getInt("height");
                switch (schm) {
                    case BIGGER:
                    case REDSTONE:
                        starty = 65;
                        break;
                    default:
                        starty = 64;
                        break;
                }
                endy = starty + h;
                ArrayList<HashMap<String, String>> data = rsc.getData();
                // loop through the chunks
                for (HashMap<String, String> map : data) {
                    String w = map.get("world");
                    World world = plugin.getServer().getWorld(w);
                    int x = plugin.getUtils().parseInt(map.get("x"));
                    int z = plugin.getUtils().parseInt(map.get("z"));
                    Chunk chunk = world.getChunkAt(x, z);
                    // find the lamps in the chunks
                    int bx = chunk.getX() << 4;
                    int bz = chunk.getZ() << 4;
                    for (int xx = bx; xx < bx + 16; xx++) {
                        for (int zz = bz; zz < bz + 16; zz++) {
                            for (int yy = starty; yy < endy; yy++) {
                                Material typeId = world.getBlockAt(xx, yy, zz).getType();
                                if (typeId.equals(Material.REDSTONE_LAMP_ON)) {
                                    String lamp = w + ":" + xx + ":" + yy + ":" + zz;
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("tardis_id", id);
                                    set.put("location", lamp);
                                    qf.doInsert("lamps", set);
                                    TARDISMessage.send(owner, true, "LAMP_ADD", (xx + ":" + yy + ":" + zz));
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            TARDISMessage.send(owner, "NOT_A_TIMELORD");
            return false;
        }
    }
}
