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
 * along with this program. If ChatColor.RESET, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.hads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * The Hostile Action Displacement System, or HADS, was one of the defence
 * mechanisms of the Doctor's TARDIS. When the outer shell of the vessel came
 * under attack, the unit dematerialised the TARDIS and re-materialised it a
 * short distance away after the attacker had gone, in a safer locale. The HADS
 * had to be manually set, and the Doctor often forgot to do so.
 *
 * @author eccentric_nz
 */
public class TARDISHostileDispersal {

    private final TARDIS plugin;
    private final List<Material> has_colour;

    public TARDISHostileDispersal(TARDIS plugin) {
        this.has_colour = Arrays.asList(Material.WOOL, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.STAINED_CLAY);
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void disperseTARDIS(final int id, boolean cham, UUID uuid, Player hostile, PRESET preset) {
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            plugin.debug("Could not get current TARDIS location for HADS!");
        }
        if (rsc.isSubmarine()) {
            // underwater use displacement
            new TARDISHostileDisplacement(plugin).moveTARDIS(id, cham, uuid, hostile, preset);
            return;
        }
        Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        COMPASS d = rsc.getDirection();
        Biome biome = rsc.getBiome();
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            // always remove the portal
            if (plugin.getTrackerKeeper().getPortals().containsKey(l)) {
                plugin.getTrackerKeeper().getPortals().remove(l);
            }
            // toggle the doors if neccessary
            new TARDISDoorCloser(plugin, uuid, id).closeDoors();
        }
        final World w = l.getWorld();
        // make sure chunk is loaded
        Chunk chunk = w.getChunkAt(l);
        while (!chunk.isLoaded()) {
            chunk.load();
        }
        final int sbx = l.getBlockX() - 1;
        final int sby;
        if (preset.equals(PRESET.SUBMERGED)) {
            sby = l.getBlockY() - 1;
        } else {
            sby = l.getBlockY();
        }
        final int sbz = l.getBlockZ() - 1;
        // reset biome and it's not The End
        if (l.getBlock().getBiome().equals(Biome.DEEP_OCEAN) || (l.getBlock().getBiome().equals(Biome.SKY) && !l.getWorld().getEnvironment().equals(World.Environment.THE_END)) && biome != null) {
            // reset the biome
            boolean run = true;
            for (int c = 0; c < 3 && run; c++) {
                for (int r = 0; r < 3 && run; r++) {
                    try {
                        w.setBiome(sbx + c, sbz + r, biome);
                    } catch (NullPointerException e) {
                        //plugin.debug(plugin.getPluginName() + "Couldn't set biome!\nWorld = " + w.toString() + "\nsbx = " + sbx + "\nsbz = " + sbz + "\nbiome = " + biome.toString());
                        e.printStackTrace();
                        run = false;
                        // remove TARDIS from tracker
                        plugin.getTrackerKeeper().getDematerialising().remove(Integer.valueOf(id));
                    }
                }
            }
            // refresh the chunk
            w.refreshChunk(chunk.getX(), chunk.getZ());
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
                TARDISBlockSetters.setBlock(w, flowerx, flowery, flowerz, 0, (byte) 0);
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
        final List<FallingBlock> falls = new ArrayList<FallingBlock>();
        byte tmp = 0;
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block b = w.getBlockAt((sbx + xx), (sby + yy), (sbz + zz));
                    if (!b.getType().equals(Material.AIR)) {
                        float v = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
                        byte colour = (has_colour.contains(b.getType())) ? b.getData() : plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(b.getTypeId());
                        if (yy == 1 && xx == 0 && zz == 0) {
                            tmp = colour;
                        }
                        FallingBlock fb = w.spawnFallingBlock(b.getLocation(), Material.CARPET, colour);
                        falls.add(fb);
                        fb.setDropItem(false);
                        fb.setVelocity(new Vector(v, v, v));
                        b.setType(Material.AIR);
                    }
                }
            }
        }
        final byte data = tmp;
        // schedule task to remove fallen blocks
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (FallingBlock f : falls) {
                    f.getLocation().getBlock().setType(Material.AIR);
                    f.remove();
                }
            }
        }, 10L);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                // set carpet base at location
                for (int xx = 0; xx < 3; xx++) {
                    for (int zz = 0; zz < 3; zz++) {
                        Block block = w.getBlockAt((sbx + xx), (sby), (sbz + zz));
                        block.setType(Material.CARPET);
                        block.setData(data);
                    }
                }
            }
        }, 15L);
        plugin.getTrackerKeeper().getDispersed().put(uuid, l);
    }
}
