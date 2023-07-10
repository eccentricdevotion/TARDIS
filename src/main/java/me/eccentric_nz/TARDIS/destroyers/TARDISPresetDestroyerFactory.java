/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.junk.TARDISJunkDestroyer;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
            ChameleonPreset demat = tardis.getDemat();
            ChameleonPreset preset = tardis.getPreset();
            // load the chunk if unloaded
            if (!dd.getLocation().getWorld().isChunkLoaded(dd.getLocation().getChunk())) {
                dd.getLocation().getWorld().loadChunk(dd.getLocation().getChunk());
            }
            if (!demat.equals(ChameleonPreset.INVISIBLE)) {
                Material cham_id = Material.LIGHT_GRAY_TERRACOTTA;
                if ((tardis.getAdaption().equals(Adaption.BIOME) && demat.equals(ChameleonPreset.FACTORY)) || demat.equals(ChameleonPreset.SUBMERGED) || tardis.getAdaption().equals(Adaption.BLOCK)) {
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
                int loops = dd.getThrottle().getLoops();
                if (loops == 3) {
                    TARDISSounds.playTARDISSound(dd.getLocation(), "tardis_takeoff_fast");
                    if (dd.getPlayer() != null && dd.getPlayer().getPlayer() != null && plugin.getUtils().inTARDISWorld(dd.getPlayer().getPlayer())) {
                        TARDISSounds.playTARDISSound(dd.getPlayer().getPlayer().getLocation(), "tardis_takeoff_fast");
                    }
                }
                if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                    dd.setThrottle(SpaceTimeThrottle.JUNK);
                }
                if (demat.equals(ChameleonPreset.JUNK)) {
                    TARDISJunkDestroyer runnable = new TARDISJunkDestroyer(plugin, dd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    plugin.getTrackerKeeper().getDematerialising().add(dd.getTardisID());
                    if (demat.equals(ChameleonPreset.SWAMP)) {
                        // remove door
                        destroyDoor(dd.getTardisID());
                    }
                    int taskID;
                    if (demat.usesArmourStand()) {
                        TARDISDematerialisePoliceBox frame = new TARDISDematerialisePoliceBox(plugin, dd, demat);
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                        frame.setTask(taskID);
                    } else {
                        TARDISDematerialisePreset runnable = new TARDISDematerialisePreset(plugin, dd, demat, cham_id.createBlockData());
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

    public void destroySign(Location l, COMPASS d, ChameleonPreset p) {
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case JUNK_MODE:
                switch (d) {
                    case EAST -> {
                        signx = 0;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = 0;
                        signz = -1;
                    }
                    default -> {
                        signx = 1;
                        signz = 0;
                    }
                }
                break;
            case GRAVESTONE:
                signx = 0;
                signz = 0;
                break;
            case TORCH:
                switch (d) {
                    case EAST -> {
                        signx = -1;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = -1;
                    }
                    case WEST -> {
                        signx = 1;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = 1;
                    }
                }
                break;
            case TOILET:
                switch (d) {
                    case EAST -> {
                        signx = 1;
                        signz = -1;
                    }
                    case SOUTH -> {
                        signx = 1;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = -1;
                        signz = 1;
                    }
                    default -> {
                        signx = -1;
                        signz = -1;
                    }
                }
                break;
            case APPERTURE:
                switch (d) {
                    case EAST -> {
                        signx = 1;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = -1;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = -1;
                    }
                }
                break;
            default:
                switch (d) {
                    case EAST -> {
                        signx = -2;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = -2;
                    }
                    case WEST -> {
                        signx = 2;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = 2;
                    }
                }
                break;
        }
        signy = switch (p) {
            case GAZEBO, JAIL, SHROOM, SWAMP -> 3;
            case TOPSYTURVEY, TOILET, TORCH -> 1;
            case ANGEL, APPERTURE, LAMP -> 0;
            default -> 2;
        };
        TARDISBlockSetters.setBlock(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz, Material.AIR);
        if (p.equals(ChameleonPreset.SWAMP)) {
            TARDISBlockSetters.setBlock(w, l.getBlockX() + signx, l.getBlockY(), l.getBlockZ() + signz, Material.AIR);
        }
    }

    public void destroyHandbrake(Location l, COMPASS d) {
        int lx;
        int lz;
        switch (d) {
            case EAST -> {
                lx = -1;
                lz = 1;
            }
            case SOUTH -> {
                lx = -1;
                lz = -1;
            }
            case WEST -> {
                lx = 1;
                lz = -1;
            }
            default -> {
                lx = 1;
                lz = 1;
            }
        }
        World w = l.getWorld();
        int tx = l.getBlockX() + lx;
        int ty = l.getBlockY() + 2;
        int tz = l.getBlockZ() + lz;
        TARDISBlockSetters.setBlock(w, tx, ty, tz, Material.AIR);
    }

    public void destroyLamp(Location l, ChameleonPreset p) {
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 3;
        int tz = l.getBlockZ();
        if (p.equals(ChameleonPreset.CAKE)) {
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
            case NORTH -> {
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ() + 1;
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ() + 1;
            }
            case WEST -> {
                leftx = l.getBlockX() + 1;
                leftz = l.getBlockZ() + 1;
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ() - 1;
            }
            case SOUTH -> {
                leftx = l.getBlockX() + 1;
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX() - 1;
                rightz = l.getBlockZ() - 1;
            }
            default -> {
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX() - 1;
                rightz = l.getBlockZ() + 1;
            }
        }
        TARDISBlockSetters.setBlock(w, leftx, eyey, leftz, Material.AIR);
        TARDISBlockSetters.setBlock(w, rightx, eyey, rightz, Material.AIR);
    }

    public void destroyMineshaftTorches(Location l, COMPASS d) {
        World w = l.getWorld();
        int leftx, leftz, rightx, rightz;
        int eyey = l.getBlockY() + 2;
        switch (d) {
            case NORTH, SOUTH -> {
                leftx = l.getBlockX() - 1;
                leftz = l.getBlockZ();
                rightx = l.getBlockX() + 1;
                rightz = l.getBlockZ();
            }
            default -> {
                leftx = l.getBlockX();
                leftz = l.getBlockZ() - 1;
                rightx = l.getBlockX();
                rightz = l.getBlockZ() + 1;
            }
        }
        TARDISBlockSetters.setBlock(w, leftx, eyey, leftz, Material.AIR);
        TARDISBlockSetters.setBlock(w, rightx, eyey, rightz, Material.AIR);
    }

    public void destroyLampTrapdoors(Location l, COMPASS d) {
        Block lamp = l.getBlock().getRelative(BlockFace.UP, 3).getRelative(plugin.getPresetBuilder().getOppositeFace(d));
        plugin.getGeneralKeeper().getFaces().forEach((f) -> lamp.getRelative(f).setBlockData(TARDISConstants.AIR));
    }
    
    public void destroyPistons(Location l) {
        Block piston = l.getBlock();
        Block piston_head = l.getBlock().getRelative(BlockFace.UP);
        plugin.getGeneralKeeper().getFaces().forEach((f) -> piston.getRelative(f).setBlockData(TARDISConstants.AIR));
        plugin.getGeneralKeeper().getFaces().forEach((f) -> piston_head.getRelative(f).setBlockData(TARDISConstants.AIR));
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
