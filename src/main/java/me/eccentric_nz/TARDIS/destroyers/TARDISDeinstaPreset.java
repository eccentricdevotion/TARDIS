/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.MaterialisationData;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
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
import org.bukkit.block.data.MultipleFacing;

import java.util.Collections;
import java.util.HashMap;

/**
 * A police box is a telephone kiosk that can be used by members of the public wishing to get help from the police.
 * Early in the First Doctor's travels, the TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISDeinstaPreset {

    private final TARDIS plugin;

    public TARDISDeinstaPreset(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Destroys the TARDIS Police Box. A 3 x 3 x 3 block area.
     *
     * @param dd     the MaterialisationData
     * @param hide   boolean determining whether to forget the protected Police Box blocks.
     * @param preset the preset to destroy
     */
    public void instaDestroyPreset(MaterialisationData dd, boolean hide, PRESET preset) {
        Location l = dd.getLocation();
        COMPASS d = dd.getDirection();
        int id = dd.getTardisID();
        boolean sub = dd.isSubmarine();
        Biome biome = dd.getBiome();
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            // always remove the portal
            plugin.getTrackerKeeper().getPortals().remove(l);
            // toggle the doors if necessary
            new TARDISDoorCloser(plugin, dd.getPlayer().getUniqueId(), id).closeDoors();
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
            case JUNK_MODE:
                plugin.getPresetDestroyer().destroyHandbrake(l, d);
                break;
            case SWAMP:
                int swampYTop = (dd.getLocation().getBlockY() + 2);
                int swampYBottom = (dd.getLocation().getBlockY() + 1);
                int swampYUnder = (dd.getLocation().getBlockY());
                TARDISBlockSetters.setBlock(w, dd.getLocation().getBlockX(), swampYTop, dd.getLocation().getBlockZ(), Material.AIR);
                TARDISBlockSetters.setBlock(w, dd.getLocation().getBlockX(), swampYBottom, dd.getLocation().getBlockZ(), Material.AIR);
                TARDISBlockSetters.setBlock(w, dd.getLocation().getBlockX(), swampYUnder, dd.getLocation().getBlockZ(), Material.AIR);
                break;
            default:
                break;
        }
        // remove door
        plugin.getPresetDestroyer().destroyDoor(id);
        // remove torch
        plugin.getPresetDestroyer().destroyLamp(l, preset);
        // remove sign
        plugin.getPresetDestroyer().destroySign(l, d, preset);
        // remove blue wool and door
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block b = w.getBlockAt((sbx + xx), (sby + yy), (sbz + zz));
                    if (!b.getType().equals(Material.AIR)) {
                        b.setBlockData(TARDISConstants.AIR);
                    }
                }
            }
        }
        l.getChunk().setForceLoaded(false);
        if (sub && plugin.isWorldGuardOnServer()) {
            // replace the block under the door if there is one
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("data", "minecraft:sponge");
            ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
            Block b;
            if (rs.resultSet()) {
                if (rs.getReplacedBlock().getLocation() != null) {
                    b = rs.getReplacedBlock().getLocation().getBlock();
                    plugin.getWorldGuardUtils().sponge(b, false);
                }
            }
        }
        // check protected blocks if has block id and data stored then put the block back!
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        tid.put("police_box", 1);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
        if (rsb.resultSet()) {
            rsb.getData().forEach((rb) -> TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData()));
        }
        // if just hiding don't remove block protection
        if (!hide) {
            plugin.getPresetDestroyer().removeBlockProtection(id);
        }
        if (dd.isSiege()) {
            Block siege = dd.getLocation().getBlock();
            siege.setBlockData(Material.BROWN_MUSHROOM_BLOCK.createBlockData());
            MultipleFacing mf = (MultipleFacing) siege.getBlockData();
            mf.getAllowedFaces().forEach((face) -> mf.setFace(face, true));
            siege.setBlockData(mf);
        }
        // refresh chunk
        plugin.getTardisHelper().refreshChunk(chunk);
        plugin.getTrackerKeeper().getDematerialising().removeAll(Collections.singleton(id));
        plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(id));
    }
}
