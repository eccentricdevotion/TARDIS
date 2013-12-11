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

import java.util.Collections;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Destroy the TARDIS Police Box.
 *
 * The chameleon circuit is the component of a TARDIS which changes its outer
 * plasmic shell to assume a shape which blends in with its surroundings.
 *
 * @author eccentric_nz
 */
public class TARDISPresetDestroyerFactory {

    private final TARDIS plugin;

    public TARDISPresetDestroyerFactory(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void destroyPreset(Location l, TARDISConstants.COMPASS d, int id, boolean hide, boolean dematerialise, boolean c, Player player) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            TARDISConstants.PRESET demat = rs.getDemat();
            if (dematerialise && !hide) {
                int cham_id = rs.getChameleon_id();
                byte cham_data = rs.getChameleon_data();
                if (c && (demat.equals(TARDISConstants.PRESET.NEW) || demat.equals(TARDISConstants.PRESET.OLD) || demat.equals(TARDISConstants.PRESET.SUBMERGED))) {
                    Block chameleonBlock;
                    // chameleon circuit is on - get block under TARDIS
                    if (l.getBlock().getType() == Material.SNOW) {
                        chameleonBlock = l.getBlock();
                    } else {
                        chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
                    }
                    // determine cham_id
                    TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                    int[] b_data = tcc.getChameleonBlock(chameleonBlock, player, false);
                    cham_id = b_data[0];
                    cham_data = (byte) b_data[1];
                }
                int lamp = plugin.getConfig().getInt("tardis_lamp");
                HashMap<String, Object> wherepp = new HashMap<String, Object>();
                wherepp.put("player", player.getName());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                if (rsp.resultSet()) {
                    lamp = rsp.getLamp();
                }
                TARDISDematerialisationPreset runnable = new TARDISDematerialisationPreset(plugin, l, demat, lamp, id, d, cham_id, cham_data, player);
                int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                runnable.setTask(taskID);
            } else {
                new TARDISDeinstaPreset(plugin).instaDestroyPreset(l, d, id, hide, demat);
            }
        }
    }

    public void destroyDoor(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
        if (rsd.resultSet()) {
            String dl = rsd.getDoor_location();
            float f = 0.0F;
            Block b = plugin.utils.getLocationFromDB(dl, f, f).getBlock();
            b.setType(Material.AIR);
            b.getRelative(BlockFace.UP).setType(Material.AIR);
        }
    }

    public void destroySign(Location l, TARDISConstants.COMPASS d, TARDISConstants.PRESET p) {
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case GRAVESTONE:
                signx = 0;
                signz = 0;
                break;
            case TORCH:
                switch (d) {
                    case EAST:
                        signx = -1;
                        signz = 0;
                        break;
                    case SOUTH:
                        signx = 0;
                        signz = -1;
                        break;
                    case WEST:
                        signx = 1;
                        signz = 0;
                        break;
                    default:
                        signx = 0;
                        signz = 1;
                        break;
                }
                break;
            case TOILET:
                switch (d) {
                    case EAST:
                        signx = 1;
                        signz = -1;
                        break;
                    case SOUTH:
                        signx = 1;
                        signz = 1;
                        break;
                    case WEST:
                        signx = -1;
                        signz = 1;
                        break;
                    default:
                        signx = -1;
                        signz = -1;
                        break;
                }
                break;
            case APPERTURE:
                switch (d) {
                    case EAST:
                        signx = 1;
                        signz = 0;
                        break;
                    case SOUTH:
                        signx = 0;
                        signz = 1;
                        break;
                    case WEST:
                        signx = -1;
                        signz = 0;
                        break;
                    default:
                        signx = 0;
                        signz = -1;
                        break;
                }
                break;
            default:
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
                    default:
                        signx = 0;
                        signz = 2;
                        break;
                }
                break;
        }
        switch (p) {
            case GAZEBO:
            case JAIL:
            case SHROOM:
            case SWAMP:
                signy = 3;
                break;
            case TOPSYTURVEY:
            case TOILET:
            case TORCH:
                signy = 1;
                break;
            case ANGEL:
            case APPERTURE:
            case LAMP:
                signy = 0;
                break;
            default:
                signy = 2;
                break;
        }
        plugin.utils.setBlock(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz, 0, (byte) 0);
        if (p.equals(TARDISConstants.PRESET.SWAMP)) {
            plugin.utils.setBlock(w, l.getBlockX() + signx, l.getBlockY(), l.getBlockZ() + signz, 0, (byte) 0);
        }
    }

    public void destroyLamp(Location l, TARDISConstants.PRESET p) {
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 3;
        int tz = l.getBlockZ();
        if (p.equals(TARDISConstants.PRESET.CAKE)) {
            for (int i = (tx - 1); i < (tx + 2); i++) {
                for (int j = (tz - 1); j < (tz + 2); j++) {
                    plugin.utils.setBlock(w, i, ty, j, 0, (byte) 0);
                }
            }
        } else {
            plugin.utils.setBlock(w, tx, ty, tz, 0, (byte) 0);
        }
    }

    public void destroyDuckEyes(Location l, TARDISConstants.COMPASS d) {
        World w = l.getWorld();
        int leftx, leftz, rightx, rightz;
        int eyey = l.getBlockY() + 3;
        switch (d) {
            case NORTH:
                leftx = l.getBlockX() + 1;
                leftz = l.getBlockZ() + 1;
                rightx = l.getBlockX() - 1;
                rightz = l.getBlockZ() + 1;
                break;
            case WEST:
                leftx = l.getBlockX() + 1;
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ() + 1;
                break;
            case SOUTH:
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ() - 1;
                break;
            default:
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ() + 1;
                rightx = l.getBlockX() - 1;
                rightz = l.getBlockZ() - 1;
                break;
        }
        plugin.utils.setBlock(w, leftx, eyey, leftz, 0, (byte) 0);
        plugin.utils.setBlock(w, rightx, eyey, rightz, 0, (byte) 0);
    }

    public void removeBlockProtection(int id, QueryFactory qf) {
        HashMap<String, Object> whereb = new HashMap<String, Object>();
        whereb.put("tardis_id", id);
        whereb.put("police_box", 1);
        qf.doDelete("blocks", whereb);
        // remove from protectBlockMap - remove(Integer.valueOf(id)) would only remove the first one
        plugin.protectBlockMap.values().removeAll(Collections.singleton(Integer.valueOf(id)));
    }
}
