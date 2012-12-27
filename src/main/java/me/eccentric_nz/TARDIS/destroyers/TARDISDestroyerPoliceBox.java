/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Destroy the TRDIS Police Box.
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerPoliceBox {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISDestroyerPoliceBox(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Destroys the TARDIS Police Box.
     *
     * @param l the location of the TARDIS Police Box.
     * @param d the direction the Police Box is facing.
     * @param id the unique key of the record for this TARDIS in the database.
     * @param hide boolean determining whether to forget the protected Police
     * Box blocks.
     */
    public void destroyPoliceBox(Location l, TARDISConstants.COMPASS d, int id, boolean hide) {
        World w = l.getWorld();
        int sbx = l.getBlockX() - 1;
        int rbx = sbx;
        int gbx = sbx;
        int sby = l.getBlockY();
        int sbz = l.getBlockZ() - 1;
        int rbz = sbz;
        int gbz = sbz;
        // remove blue wool and door
        for (int yy = 0; yy < 3; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    plugin.utils.setBlock(w, sbx, sby, sbz, 0, (byte) 0);
                    sbx++;
                }
                sbx = rbx;
                sbz++;
            }
            sbz = rbz;
            sby++;
        }
        // replace the block under the door if there is one
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        QueryFactory qf = new QueryFactory(plugin);
        if (rs.resultSet()) {
            String replacedData = rs.getReplaced();
            if (!replacedData.equals("")) {
                String[] parts = replacedData.split(":");
                World rw = plugin.getServer().getWorld(parts[0]);
                int rx, ry, rz, rID;
                byte rb = 0;
                rx = plugin.utils.parseNum(parts[1]);
                ry = plugin.utils.parseNum(parts[2]);
                rz = plugin.utils.parseNum(parts[3]);
                rID = plugin.utils.parseNum(parts[4]);
                try {
                    rb = Byte.valueOf(parts[5]);
                } catch (NumberFormatException nfe) {
                    plugin.console.sendMessage(plugin.pluginName + "Could not convert to number!");
                }
                Block b = rw.getBlockAt(rx, ry, rz);
                b.setTypeIdAndData(rID, rb, true);
            }
        }
        // finally forget the replaced block
        HashMap<String, Object> set = new HashMap<String, Object>();
        HashMap<String, Object> wherer = new HashMap<String, Object>();
        wherer.put("tardis_id", id);
        set.put("replaced", "");
        qf.doUpdate("tardis", set, wherer);

        // get rid of platform is there is one
        if (plugin.getConfig().getBoolean("platform")) {
            String plat = rs.getPlatform();
            if (!plat.equals("")) {
                int px = 0, py = 0, pz = 0;
                String[] str_blocks = plat.split("~");
                for (String sb : str_blocks) {
                    String[] p_data = sb.split(":");
                    World pw = plugin.getServer().getWorld(p_data[0]);
                    Material mat = Material.valueOf(p_data[4]);
                    try {
                        px = Integer.valueOf(p_data[1]);
                        py = Integer.valueOf(p_data[2]);
                        pz = Integer.valueOf(p_data[3]);
                    } catch (NumberFormatException nfe) {
                        plugin.console.sendMessage(plugin.pluginName + "Could not convert to number!");
                    }
                    Block pb = pw.getBlockAt(px, py, pz);
                    pb.setType(mat);
                }
            }
            // forget the platform blocks
            HashMap<String, Object> setp = new HashMap<String, Object>();
            setp.put("platform", "");
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("tardis_id", id);
            qf.doUpdate("tardis", setp, wherep);
        }
        // check protected blocks if has block id and data stored then put the block back!
        HashMap<String, Object> tid = new HashMap<String, Object>();
        tid.put("tardis_id", id);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
        if (rsb.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsb.getData();
            for (HashMap<String, String> map : data) {
                int bID = plugin.utils.parseNum(map.get("block"));
                if (bID != 0) {
                    byte bd = Byte.parseByte(map.get("data"));
                    String locStr = map.get("location");
                    String[] loc_data = locStr.split(",");
                    // x, y, z - 1, 2, 3
                    String[] xStr = loc_data[1].split("=");
                    String[] yStr = loc_data[2].split("=");
                    String[] zStr = loc_data[3].split("=");
                    int rx = plugin.utils.parseNum(xStr[1].substring(0, (xStr[1].length() - 2)));
                    int ry = plugin.utils.parseNum(yStr[1].substring(0, (yStr[1].length() - 2)));
                    int rz = plugin.utils.parseNum(zStr[1].substring(0, (zStr[1].length() - 2)));
                    plugin.utils.setBlock(w, rx, ry, rz, bID, bd);
                }
            }
        }
        if (hide == false) {
            HashMap<String, Object> whereb = new HashMap<String, Object>();
            whereb.put("tardis_id", id);
            qf.doDelete("blocks", whereb);
        }
    }

    public void destroySign(Location l, TARDISConstants.COMPASS d) {
        World w = l.getWorld();
        int signx = 0, signz = 0;
        switch (d) {
            case EAST:
                signx = -2;
                signz = 0;
                break;
            case SOUTH:
                signx = 0;
                signz = -2;
                break;
            case WEST:
                signx = 2;
                signz = 0;
                break;
            case NORTH:
                signx = 0;
                signz = 2;
                break;
        }
        int signy = 2;
        plugin.utils.setBlock(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz, 0, (byte) 0);
    }

    public void destroyTorch(Location l) {
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 3;
        int tz = l.getBlockZ();
        plugin.utils.setBlock(w, tx, ty, tz, 0, (byte) 0);
    }
}