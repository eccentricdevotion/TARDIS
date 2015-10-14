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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
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
     * @param tmd the TARDISMaterialisationData
     * @param hide boolean determining whether to forget the protected Police
     * Box blocks.
     * @param preset the preset to destroy
     */
    @SuppressWarnings("deprecation")
    public void instaDestroyPreset(TARDISMaterialisationData tmd, boolean hide, PRESET preset) {
        Location l = tmd.getLocation();
        COMPASS d = tmd.getDirection();
        final int id = tmd.getTardisID();
        boolean sub = tmd.isSubmarine();
        Biome biome = tmd.getBiome();
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            // always remove the portal
            if (plugin.getTrackerKeeper().getPortals().containsKey(l)) {
                plugin.getTrackerKeeper().getPortals().remove(l);
            }
            // toggle the doors if neccessary
            new TARDISDoorCloser(plugin, tmd.getPlayer().getUniqueId(), id).closeDoors();
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
        if (l.getBlock().getBiome().equals(Biome.DEEP_OCEAN) || (l.getBlock().getBiome().equals(Biome.SKY) && !l.getWorld().getEnvironment().equals(Environment.THE_END)) && biome != null) {
            // reset the biome
            boolean run = true;
            for (int c = 0; c < 3 && run; c++) {
                for (int r = 0; r < 3 && run; r++) {
                    try {
                        w.setBiome(sbx + c, sbz + r, biome);
                    } catch (NullPointerException e) {
                        plugin.debug(plugin.getPluginName() + "Couldn't set biome!\nWorld = " + w.toString() + "\nsbx = " + sbx + "\nsbz = " + sbz + "\nbiome = " + biome.toString());
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
        // remove blue wool and door
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block b = w.getBlockAt((sbx + xx), (sby + yy), (sbz + zz));
                    if (!b.getType().equals(Material.AIR)) {
                        b.setType(Material.AIR);
                    }
                }
            }
        }

        if (sub && plugin.isWorldGuardOnServer()) {
            // replace the block under the door if there is one
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            where.put("block", 19);
            ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
            Block b;
            if (rs.resultSet()) {
                String replacedData = rs.getLocation();
                if (!replacedData.isEmpty()) {
                    Location sponge = plugin.getLocationUtils().getLocationFromBukkitString(replacedData);
                    if (sponge != null) {
                        b = sponge.getBlock();
                        plugin.getWorldGuardUtils().sponge(b, false);
                    }
                }
            }
        }

        // check protected blocks if has block id and data stored then put the block back!
        HashMap<String, Object> tid = new HashMap<String, Object>();
        tid.put("tardis_id", id);
        tid.put("police_box", 1);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
        if (rsb.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsb.getData();
            for (HashMap<String, String> map : data) {
                int bID = 0;
                byte bd = (byte) 0;
                if (map.get("block") != null) {
                    bID = TARDISNumberParsers.parseInt(map.get("block"));
                }
                if (map.get("data") != null) {
                    bd = TARDISNumberParsers.parseByte(map.get("data"));
                }
                String locStr = map.get("location");
                Location tl = plugin.getLocationUtils().getLocationFromBukkitString(locStr);
                TARDISBlockSetters.setBlock(tl, bID, bd);
            }
        }
        // if just hiding don't remove block protection
        if (!hide) {
            plugin.getPresetDestroyer().removeBlockProtection(id, new QueryFactory(plugin));
        }
        // refresh chunk
        w.refreshChunk(chunk.getX(), chunk.getZ());
    }
}
