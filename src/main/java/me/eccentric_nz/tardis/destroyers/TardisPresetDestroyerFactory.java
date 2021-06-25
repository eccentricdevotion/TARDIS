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
package me.eccentric_nz.tardis.destroyers;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.chameleon.TardisChameleonCircuit;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Adaption;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.junk.TardisJunkDestroyer;
import me.eccentric_nz.tardis.utility.TardisBlockSetters;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

/**
 * Destroy the tardis Police Box.
 * <p>
 * The chameleon circuit is the component of a tardis which changes its outer plasmic shell to assume a shape which
 * blends in with its surroundings.
 *
 * @author eccentric_nz
 */
public class TardisPresetDestroyerFactory {

    private final TardisPlugin plugin;

    public TardisPresetDestroyerFactory(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void destroyPreset(DestroyData dd) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", dd.getTardisId());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            Preset demat = tardis.getDemat();
            Preset preset = tardis.getPreset();
            // load the chunk if unloaded
            if (!Objects.requireNonNull(dd.getLocation().getWorld()).isChunkLoaded(dd.getLocation().getChunk())) {
                dd.getLocation().getWorld().loadChunk(dd.getLocation().getChunk());
            }
            if (!demat.equals(Preset.INVISIBLE)) {
                Material cham_id = Material.LIGHT_GRAY_TERRACOTTA;
                if ((tardis.getAdaption().equals(Adaption.BIOME) && demat.equals(Preset.FACTORY)) || demat.equals(Preset.SUBMERGED) || tardis.getAdaption().equals(Adaption.BLOCK)) {
                    Block chameleonBlock;
                    // chameleon circuit is on - get block under TARDIS
                    if (dd.getLocation().getBlock().getType() == Material.SNOW) {
                        chameleonBlock = dd.getLocation().getBlock();
                    } else {
                        chameleonBlock = dd.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    }
                    // determine cham_id
                    TardisChameleonCircuit tcc = new TardisChameleonCircuit(plugin);
                    cham_id = tcc.getChameleonBlock(chameleonBlock, dd.getPlayer());
                }
                int loops = dd.getThrottle().getLoops();
                if (loops == 3) {
                    TardisSounds.playTardisSound(dd.getLocation(), "tardis_takeoff_fast");
                    if (dd.getPlayer() != null && dd.getPlayer().getPlayer() != null && plugin.getUtils().inTardisWorld(dd.getPlayer().getPlayer())) {
                        TardisSounds.playTardisSound(dd.getPlayer().getPlayer().getLocation(), "tardis_takeoff_fast");
                    }
                }
                if (preset.equals(Preset.JUNK_MODE)) {
                    dd.setThrottle(SpaceTimeThrottle.JUNK);
                }
                if (demat.equals(Preset.JUNK)) {
                    TardisJunkDestroyer runnable = new TardisJunkDestroyer(plugin, dd);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    plugin.getTrackerKeeper().getDematerialising().add(dd.getTardisId());
                    if (demat.equals(Preset.SWAMP)) {
                        // remove door
                        destroyDoor(dd.getTardisId());
                    }
                    int taskID;
                    if (demat.usesItemFrame()) {
                        TardisDematerialisePoliceBox frame = new TardisDematerialisePoliceBox(plugin, dd, demat);
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, frame, 10L, 20L);
                        frame.setTask(taskID);
                    } else {
                        TardisDematerialisePreset runnable = new TardisDematerialisePreset(plugin, dd, demat, cham_id.createBlockData());
                        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                        runnable.setTask(taskID);
                    }
                }
            } else {
                new TardisDeinstantPreset(plugin).instaDestroyPreset(dd, dd.isHide(), demat);
            }
        }
    }

    public void destroyDoor(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
        if (rsd.resultSet()) {
            String dl = rsd.getDoorLocation();
            if (dl != null) {
                Location l = TardisStaticLocationGetters.getLocationFromDB(dl);
                if (l != null) {
                    Block b = l.getBlock();
                    b.setBlockData(TardisConstants.AIR);
                    b.getRelative(BlockFace.UP).setBlockData(TardisConstants.AIR);
                }
            }
        }
    }

    public void destroySign(Location l, CardinalDirection d, Preset p) {
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
        assert w != null;
        TardisBlockSetters.setBlock(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz, Material.AIR);
        if (p.equals(Preset.SWAMP)) {
            TardisBlockSetters.setBlock(w, l.getBlockX() + signx, l.getBlockY(), l.getBlockZ() + signz, Material.AIR);
        }
    }

    public void destroyHandbrake(Location l, CardinalDirection d) {
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
        assert w != null;
        TardisBlockSetters.setBlock(w, tx, ty, tz, Material.AIR);
    }

    public void destroyLamp(Location l, Preset p) {
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 3;
        int tz = l.getBlockZ();
        if (p.equals(Preset.CAKE)) {
            for (int i = (tx - 1); i < (tx + 2); i++) {
                for (int j = (tz - 1); j < (tz + 2); j++) {
                    assert w != null;
                    TardisBlockSetters.setBlock(w, i, ty, j, Material.AIR);
                }
            }
        } else {
            assert w != null;
            TardisBlockSetters.setBlock(w, tx, ty, tz, Material.AIR);
        }
    }

    public void destroyDuckEyes(Location l, CardinalDirection d) {
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
        assert w != null;
        TardisBlockSetters.setBlock(w, leftx, eyey, leftz, Material.AIR);
        TardisBlockSetters.setBlock(w, rightx, eyey, rightz, Material.AIR);
    }

    public void destroyMineshaftTorches(Location l, CardinalDirection d) {
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
        assert w != null;
        TardisBlockSetters.setBlock(w, leftx, eyey, leftz, Material.AIR);
        TardisBlockSetters.setBlock(w, rightx, eyey, rightz, Material.AIR);
    }

    public void destroyLampTrapdoors(Location l, CardinalDirection d) {
        Block lamp = l.getBlock().getRelative(BlockFace.UP, 3).getRelative(getOppositeFace(d));
        plugin.getGeneralKeeper().getFaces().forEach((f) -> lamp.getRelative(f).setBlockData(TardisConstants.AIR));
    }

    private BlockFace getOppositeFace(CardinalDirection c) {
        return switch (c) {
            case NORTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.EAST;
            case SOUTH -> BlockFace.NORTH;
            default -> BlockFace.WEST;
        };
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
