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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerPoliceBox {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISDestroyerPoliceBox(TARDIS plugin) {
        this.plugin = plugin;
    }

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
        Statement statement = null;
        ResultSet rs = null;
        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();
            String queryReplaced = "SELECT replaced FROM tardis WHERE tardis_id = '" + id + "' LIMIT 1";
            rs = statement.executeQuery(queryReplaced);
            if (rs.next()) {
                String replacedData = rs.getString("replaced");
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
            String queryForget = "UPDATE tardis SET replaced = '' WHERE tardis_id = " + id;
            statement.executeUpdate(queryForget);

            // get rid of platform is there is one
            if (plugin.getConfig().getBoolean("platform")) {
                String queryPlatform = "SELECT platform FROM tardis WHERE tardis_id = " + id;
                ResultSet prs = statement.executeQuery(queryPlatform);
                if (prs.next()) {
                    String plat = prs.getString("platform");
                    if (!prs.wasNull() && !plat.equals("")) {
                        int px = 0, py = 0, pz = 0;
                        String[] str_blocks = prs.getString("platform").split("~");
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
                    String queryEmptyP = "UPDATE tardis SET platform = '' WHERE tardis_id = " + id;
                    statement.executeUpdate(queryEmptyP);
                }
                prs.close();
            }
            // check protected blocks if has block id and data stored then put the block back!
            String queryGetBlocks = "SELECT * FROM blocks WHERE tardis_id = " + id;
            ResultSet rsBlocks = statement.executeQuery(queryGetBlocks);
            while (rsBlocks.next()) {
                int bID = rsBlocks.getInt("block");
                if (bID != 0) {
                    byte data = rsBlocks.getByte("data");
                    String locStr = rsBlocks.getString("location");
                    String[] loc_data = locStr.split(",");
                    // x, y, z - 1, 2, 3
                    String[] xStr = loc_data[1].split("=");
                    String[] yStr = loc_data[2].split("=");
                    String[] zStr = loc_data[3].split("=");
                    int rx = plugin.utils.parseNum(xStr[1].substring(0, (xStr[1].length() - 2)));
                    int ry = plugin.utils.parseNum(yStr[1].substring(0, (yStr[1].length() - 2)));
                    int rz = plugin.utils.parseNum(zStr[1].substring(0, (zStr[1].length() - 2)));
                    plugin.utils.setBlock(w, rx, ry, rz, bID, data);
                }
            }
            rsBlocks.close();
            // remove protected blocks from the blocks table
            if (hide == false) {
                String queryRemoveBlocks = "DELETE FROM blocks WHERE tardis_id = " + id;
                statement.executeUpdate(queryRemoveBlocks);
            }
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + " Save Replaced Block Error: " + e);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                statement.close();
            } catch (Exception e) {
            }
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