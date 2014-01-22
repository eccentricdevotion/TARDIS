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

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
     * @param l the location of the TARDIS Police Box (bottom centre).
     * @param d the direction the Police Box is facing.
     * @param id the unique key of the record for this TARDIS in the database.
     * @param hide boolean determining whether to forget the protected Police
     * Box blocks.
     * @param preset the preset to destroy
     * @param sub whether the next location is submarine
     */
    @SuppressWarnings("deprecation")
    public void instaDestroyPreset(Location l, COMPASS d, final int id, boolean hide, PRESET preset, boolean sub) {
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
                plugin.utils.setBlock(w, flowerx, flowery, flowerz, 0, (byte) 0);
                break;
            case DUCK:
                plugin.destroyerP.destroyDuckEyes(l, d);
                break;
            case MINESHAFT:
                plugin.destroyerP.destroyMineshaftTorches(l, d);
                break;
            default:
                break;
        }
        plugin.tardisDematerialising.remove(Integer.valueOf(id));
        plugin.tardisChunkList.remove(l.getChunk());
        // remove door
        plugin.destroyerP.destroyDoor(id);
        // remove torch
        plugin.destroyerP.destroyLamp(l, preset);
        // remove sign
        plugin.destroyerP.destroySign(l, d, preset);
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

        if (sub && plugin.worldGuardOnServer) {
            // replace the block under the door if there is one
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            where.put("block", 19);
            ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
            Block b;
            if (rs.resultSet()) {
                String replacedData = rs.getLocation();
                if (!replacedData.isEmpty()) {
                    Location sponge = plugin.utils.getLocationFromBukkitString(replacedData);
                    b = sponge.getBlock();
                    plugin.wgutils.sponge(b, false);
                }
            }
        }

        // check protected blocks if has block id and data stored then put the block back!
        HashMap<String, Object> tid = new HashMap<String, Object>();
        tid.put("tardis_id", id);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
        if (rsb.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsb.getData();
            for (HashMap<String, String> map : data) {
                int bID = 0;
                byte bd = (byte) 0;
                if (map.get("block") != null) {
                    bID = plugin.utils.parseInt(map.get("block"));
                }
                if (map.get("data") != null) {
                    bd = plugin.utils.parseByte(map.get("data"));
                }
                String locStr = map.get("location");
                String[] loc_data = locStr.split(",");
                // x, y, z - 1, 2, 3
                String[] xStr = loc_data[1].split("=");
                String[] yStr = loc_data[2].split("=");
                String[] zStr = loc_data[3].split("=");
                int rx = plugin.utils.parseInt(xStr[1].substring(0, (xStr[1].length() - 2)));
                int ry = plugin.utils.parseInt(yStr[1].substring(0, (yStr[1].length() - 2)));
                int rz = plugin.utils.parseInt(zStr[1].substring(0, (zStr[1].length() - 2)));
                plugin.utils.setBlock(w, rx, ry, rz, bID, bd);
            }
        }

        // if just hiding don't remove block protection
        if (!hide) {
            plugin.destroyerP.removeBlockProtection(id, new QueryFactory(plugin));
        }
    }
}
