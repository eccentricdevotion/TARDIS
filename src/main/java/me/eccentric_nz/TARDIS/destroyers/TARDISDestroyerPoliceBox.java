/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.destroyers;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Destroy the TARDIS Police Box.
 *
 * The chameleon circuit is the component of a TARDIS which changes its outer
 * plasmic shell to assume a shape which blends in with its surroundings.
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerPoliceBox {

    private final TARDIS plugin;

    public TARDISDestroyerPoliceBox(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void destroyPoliceBox(Location l, TARDISConstants.COMPASS d, int id, boolean hide, boolean dematerialise, boolean c, Player player) {
        if (dematerialise && !hide) {
            int lamp = plugin.getConfig().getInt("tardis_lamp");
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("player", player.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                lamp = rsp.getLamp();
            }
            int mat = plugin.getConfig().getInt("wall_id");
            byte data = (byte) plugin.getConfig().getInt("wall_data");
            if (c) {
                // chameleon circuit is on - get chameleon_id/data
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                ResultSetTardis rsc = new ResultSetTardis(plugin, wherec, "", false);
                rsc.resultSet();
                mat = rsc.getChameleon_id();
                data = rsc.getChameleon_data();
            }
            TARDISDematerialisationRunnable runnable = new TARDISDematerialisationRunnable(plugin, l, lamp, mat, data, id, d, player);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
            runnable.setTask(taskID);
        } else {
            new TARDISDeinstaPoliceBox(plugin).instaDestroyPB(l, d, id, hide);
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

    public void destroyPlatform(String plat, int id) {
        if (!plat.isEmpty()) {
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
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> setp = new HashMap<String, Object>();
        setp.put("platform", "");
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("tardis_id", id);
        qf.doUpdate("tardis", setp, wherep);
    }
}
