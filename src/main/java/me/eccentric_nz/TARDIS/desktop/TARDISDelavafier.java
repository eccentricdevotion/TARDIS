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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISDelavafier {

    private final TARDIS plugin;
    private final UUID uuid;
    private final BlockData ORANGE = Material.ORANGE_TERRACOTTA.createBlockData();
    public static final BlockData GLASS = Material.LIGHT_BLUE_STAINED_GLASS.createBlockData();

    TARDISDelavafier(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    void swap() {
        // calculate startx, starty, startz
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            int startx;
            int startz;
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                startz = gsl[2];
            }
            String[] split = tardis.getChunk().split(":");
            String w = (split[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : split[0].toLowerCase(Locale.ENGLISH));
            World world = plugin.getServer().getWorld(w);
            for (int level = 2; level < 6; level++) {
                for (int row = 0; row < 32; row++) {
                    for (int col = 0; col < 32; col++) {
                        int x = startx + row;
                        int starty = 64;
                        int y = starty + level;
                        int z = startz + col;
                        Block b = world.getBlockAt(x, y, z);
                        Material type = b.getType();
                        if (type.equals(Material.LAVA)) {
                            b.setBlockData(ORANGE);
                        }
                        if (type.equals(Material.WATER)) {
                            b.setBlockData(GLASS);
                        }
                    }
                }
            }
        }
    }
}
