/*
 * Copyright (C) 2018 eccentric_nz
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
 * along with this program. If ChatColor.RESET, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.hads;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISHADSEvent;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The Hostile Action Displacement System, or HADS, was one of the defence mechanisms of the Doctor's TARDIS. When the
 * outer shell of the vessel came under attack, the unit dematerialised the TARDIS and re-materialised it a short
 * distance away after the attacker had gone, in a safer locale. The HADS had to be manually set, and the Doctor often
 * forgot to do so.
 *
 * @author eccentric_nz
 */
class TARDISHostileDispersal {

    private final TARDIS plugin;
    private final List<Material> replace_with_barrier;

    public TARDISHostileDispersal(TARDIS plugin) {
        this.plugin = plugin;
        replace_with_barrier = buildList();
    }

    public void disperseTARDIS(int id, UUID uuid, Player hostile, PRESET preset) {
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            plugin.debug("Could not get current TARDIS location for HADS!");
        }
        if (rsc.isSubmarine()) {
            // underwater use displacement
            new TARDISHostileDisplacement(plugin).moveTARDIS(id, uuid, hostile, preset);
            return;
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        COMPASS d = rsc.getDirection();
        Biome biome = rsc.getBiome();
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            // always remove the portal
            plugin.getTrackerKeeper().getPortals().remove(l);
            // toggle the doors if neccessary
            new TARDISDoorCloser(plugin, uuid, id).closeDoors();
        }
        World w = l.getWorld();
        // make sure chunk is loaded
        Chunk chunk = w.getChunkAt(l);
        while (!chunk.isLoaded()) {
            chunk.load();
        }
        int sbx = l.getBlockX() - 1;
        int sby;
        if (preset.equals(PRESET.SUBMERGED)) {
            sby = l.getBlockY() - 1;
        } else {
            sby = l.getBlockY();
        }
        int sbz = l.getBlockZ() - 1;
        // reset biome and it's not The End
        if (!plugin.getUtils().restoreBiome(l, biome)) {
            // remove TARDIS from tracker
            plugin.getTrackerKeeper().getDematerialising().remove(Integer.valueOf(id));
        }
        // remove problem blocks first
        switch (preset) {
            case GRAVESTONE:
                // remove flower
                int flowerx;
                int flowery = (l.getBlockY() + 1);
                int flowerz;
                switch (d) {
                    case NORTH:
                        flowerx = l.getBlockX();
                        flowerz = l.getBlockZ() + 1;
                        break;
                    case WEST:
                        flowerx = l.getBlockX() + 1;
                        flowerz = l.getBlockZ();
                        break;
                    case SOUTH:
                        flowerx = l.getBlockX();
                        flowerz = l.getBlockZ() - 1;
                        break;
                    default:
                        flowerx = l.getBlockX() - 1;
                        flowerz = l.getBlockZ();
                        break;
                }
                TARDISBlockSetters.setBlock(w, flowerx, flowery, flowerz, Material.AIR);
                break;
            case DUCK:
                plugin.getPresetDestroyer().destroyDuckEyes(l, d);
                break;
            case MINESHAFT:
                plugin.getPresetDestroyer().destroyMineshaftTorches(l, d);
                break;
            case LAMP:
                plugin.getPresetDestroyer().destroyLampTrapdoors(l, d);
                break;
            default:
                break;
        }
        plugin.getTrackerKeeper().getDematerialising().remove(Integer.valueOf(id));
        plugin.getGeneralKeeper().getTardisChunkList().remove(l.getChunk());
        // remove door
        plugin.getPresetDestroyer().destroyDoor(id);
        // remove torch
        plugin.getPresetDestroyer().destroyLamp(l, preset);
        // remove sign
        plugin.getPresetDestroyer().destroySign(l, d, preset);
        if (preset.equals(PRESET.JUNK_MODE)) {
            plugin.getPresetDestroyer().destroyHandbrake(l, d);
        }
        // remove blue wool
        List<FallingBlock> falls = new ArrayList<>();
        Material tmp = Material.BLUE_CARPET;
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block b = w.getBlockAt((sbx + xx), (sby + yy), (sbz + zz));
                    if (yy == 0) {
                        Block under = b.getRelative(BlockFace.DOWN);
                        if (replace_with_barrier.contains(under.getType())) {
                            plugin.getBlockUtils().setUnderDoorBlock(w, (sbx + xx), (sby + yy) - 1, (sbz + zz), id, false);
                        }
                    }
                    if (!plugin.getUtils().isAir(b.getType())) {
                        float v = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
                        Material colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(b.getType());
                        if (yy == 1 && xx == 0 && zz == 0) {
                            tmp = colour;
                        }
                        FallingBlock fb = w.spawnFallingBlock(b.getLocation(), colour.createBlockData());
                        falls.add(fb);
                        fb.setDropItem(false);
                        fb.setVelocity(new Vector(v, v, v));
                        b.setBlockData(TARDISConstants.AIR);
                    }
                }
            }
        }
        BlockData mat = tmp.createBlockData();
        // schedule task to remove fallen blocks
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> falls.forEach((f) -> {
            f.getLocation().getBlock().setBlockData(TARDISConstants.AIR);
            f.remove();
        }), 10L);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // set carpet base at location
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block block = w.getBlockAt((sbx + xx), (sby), (sbz + zz));
                    block.setBlockData(mat);
                }
            }
        }, 15L);
        plugin.getTrackerKeeper().getDispersed().put(uuid, l);
        plugin.getTrackerKeeper().getDispersedTARDII().add(id);
        plugin.getPM().callEvent(new TARDISHADSEvent(hostile, id, l, HADS.DISPERSAL));
    }

    private List<Material> buildList() {
        List<Material> list = new ArrayList<>();
        plugin.getBlocksConfig().getStringList("under_door_blocks").forEach((str) -> list.add(Material.getMaterial(str)));
        return list;
    }
}
