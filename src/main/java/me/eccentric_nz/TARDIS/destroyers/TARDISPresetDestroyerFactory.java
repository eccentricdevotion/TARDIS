/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.junk.TARDISJunkDestroyer;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Collections;
import java.util.HashMap;

/**
 * Destroy the TARDIS Police Box.
 * <p>
 * The chameleon circuit is the component of a TARDIS which changes its outer plasmic shell to assume a shape which
 * blends in with its surroundings.
 *
 * @author eccentric_nz
 */
public class TARDISPresetDestroyerFactory {

    private final TARDIS plugin;

    public TARDISPresetDestroyerFactory(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void destroyPreset(DestroyData dd) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", dd.getTardisID());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            PRESET demat = tardis.getDemat();
            PRESET preset = tardis.getPreset();
            // load the chunk if unloaded
            if (!dd.getLocation().getWorld().isChunkLoaded(dd.getLocation().getChunk())) {
                dd.getLocation().getWorld().loadChunk(dd.getLocation().getChunk());
            }
            if (!demat.equals(PRESET.INVISIBLE)) {
                Material cham_id = Material.LIGHT_GRAY_TERRACOTTA;
                if ((tardis.getAdaption().equals(Adaption.BIOME) && demat.equals(PRESET.FACTORY)) || demat.equals(PRESET.SUBMERGED) || tardis.getAdaption().equals(Adaption.BLOCK)) {
                    Block chameleonBlock;
                    // chameleon circuit is on - get block under TARDIS
                    if (dd.getLocation().getBlock().getType() == Material.SNOW) {
                        chameleonBlock = dd.getLocation().getBlock();
                    } else {
                        chameleonBlock = dd.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    }
                    // determine cham_id
                    TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                    cham_id = tcc.getChameleonBlock(chameleonBlock, dd.getPlayer());
                }
                int loops = 18;
                if (dd.isHide() || dd.isSiege()) {
                    loops = 3;
                    TARDISSounds.playTARDISSound(dd.getLocation(), "tardis_takeoff_fast");
                    if (dd.getPlayer() != null && dd.getPlayer().getPlayer() != null && plugin.getUtils().inTARDISWorld(dd.getPlayer().getPlayer())) {
                        TARDISSounds.playTARDISSound(dd.getPlayer().getPlayer().getLocation(), "tardis_takeoff_fast");
                    }
                } else if (preset.equals(PRESET.JUNK_MODE)) {
                    loops = 25;
                }
                if (demat.equals(PRESET.JUNK)) {
                    TARDISJunkDestroyer runnable = new TARDISJunkDestroyer(plugin, dd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    plugin.getTrackerKeeper().getDematerialising().add(dd.getTardisID());
                    if (demat.equals(PRESET.SWAMP)) {
                        // remove door
                        destroyDoor(dd.getTardisID());
                    }
                    int taskID;
                    if (demat.isColoured()) {
                        TARDISDematerialisePoliceBox frame = new TARDISDematerialisePoliceBox(plugin, dd, loops, demat);
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                        frame.setTask(taskID);
                    } else {
                        TARDISDematerialisePreset runnable = new TARDISDematerialisePreset(plugin, dd, demat, cham_id.createBlockData(), loops);
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                        runnable.setTask(taskID);
                    }
                }
            } else {
                new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, dd.isHide(), demat);
            }
        }
    }

    public void destroyDoor(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
        if (rsd.resultSet()) {
            String dl = rsd.getDoor_location();
            if (dl != null) {
                Location l = TARDISStaticLocationGetters.getLocationFromDB(dl);
                if (l != null) {
                    Block b = l.getBlock();
                    b.setBlockData(TARDISConstants.AIR);
                    b.getRelative(BlockFace.UP).setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }

    public void destroySign(Location l, COMPASS d, PRESET p) {
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case JUNK_MODE:
                switch (d) {
                    case EAST:
                        signx = 0;
                        signz = 1;
                        break;
                    case WEST:
                        signx = 0;
                        signz = -1;
                        break;
                    default:
                        signx = 1;
                        signz = 0;
                        break;
                }
                break;
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
        TARDISBlockSetters.setBlock(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz, Material.AIR);
        if (p.equals(PRESET.SWAMP)) {
            TARDISBlockSetters.setBlock(w, l.getBlockX() + signx, l.getBlockY(), l.getBlockZ() + signz, Material.AIR);
        }
    }

    public void destroyHandbrake(Location l, COMPASS d) {
        int lx;
        int lz;
        switch (d) {
            case EAST:
                lx = -1;
                lz = 1;
                break;
            case SOUTH:
                lx = -1;
                lz = -1;
                break;
            case WEST:
                lx = 1;
                lz = -1;
                break;
            default:
                lx = 1;
                lz = 1;
                break;
        }
        World w = l.getWorld();
        int tx = l.getBlockX() + lx;
        int ty = l.getBlockY() + 2;
        int tz = l.getBlockZ() + lz;
        TARDISBlockSetters.setBlock(w, tx, ty, tz, Material.AIR);
    }

    public void destroyLamp(Location l, PRESET p) {
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 3;
        int tz = l.getBlockZ();
        if (p.equals(PRESET.CAKE)) {
            for (int i = (tx - 1); i < (tx + 2); i++) {
                for (int j = (tz - 1); j < (tz + 2); j++) {
                    TARDISBlockSetters.setBlock(w, i, ty, j, Material.AIR);
                }
            }
        } else {
            TARDISBlockSetters.setBlock(w, tx, ty, tz, Material.AIR);
        }
    }

    public void destroyDuckEyes(Location l, COMPASS d) {
        World w = l.getWorld();
        int leftx, leftz, rightx, rightz;
        int eyey = l.getBlockY() + 3;
        switch (d) {
            case NORTH:
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ() + 1;
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ() + 1;
                break;
            case WEST:
                leftx = l.getBlockX() + 1;
                leftz = l.getBlockZ() + 1;
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ() - 1;
                break;
            case SOUTH:
                leftx = l.getBlockX() + 1;
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX() - 1;
                rightz = l.getBlockZ() - 1;
                break;
            default:
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX() - 1;
                rightz = l.getBlockZ() + 1;
                break;
        }
        TARDISBlockSetters.setBlock(w, leftx, eyey, leftz, Material.AIR);
        TARDISBlockSetters.setBlock(w, rightx, eyey, rightz, Material.AIR);
    }

    public void destroyMineshaftTorches(Location l, COMPASS d) {
        World w = l.getWorld();
        int leftx, leftz, rightx, rightz;
        int eyey = l.getBlockY() + 2;
        switch (d) {
            case NORTH:
            case SOUTH:
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ();
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ();
                break;
            default:
                leftx = l.getBlockX();
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX();
                rightz = l.getBlockZ() + 1;
                break;
        }
        TARDISBlockSetters.setBlock(w, leftx, eyey, leftz, Material.AIR);
        TARDISBlockSetters.setBlock(w, rightx, eyey, rightz, Material.AIR);
    }

    public void destroyLampTrapdoors(Location l, COMPASS d) {
        Block lamp = l.getBlock().getRelative(BlockFace.UP, 3).getRelative(getOppositeFace(d));
        plugin.getGeneralKeeper().getFaces().forEach((f) -> lamp.getRelative(f).setBlockData(TARDISConstants.AIR));
    }

    private BlockFace getOppositeFace(COMPASS c) {
        switch (c) {
            case NORTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.EAST;
            case SOUTH:
                return BlockFace.NORTH;
            default:
                return BlockFace.WEST;
        }
    }

    public void removeBlockProtection(int id) {
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        whereb.put("police_box", 1);
        plugin.getQueryFactory().doDelete("blocks", whereb);
        // remove from protectBlockMap - remove(id) would only remove the first one
        plugin.getGeneralKeeper().getProtectBlockMap().values().removeAll(Collections.singleton(id));
    }
}
