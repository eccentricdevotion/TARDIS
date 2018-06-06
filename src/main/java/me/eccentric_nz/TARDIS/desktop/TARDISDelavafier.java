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

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISDelavafier {

    private final TARDIS plugin;
    private final UUID uuid;
    int startx, starty = 64, startz;

    public TARDISDelavafier(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void swap() {
        // calculate startx, starty, startz
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
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
            World world = plugin.getServer().getWorld(split[0]);
            for (int level = 2; level < 6; level++) {
                for (int row = 0; row < 32; row++) {
                    for (int col = 0; col < 32; col++) {
                        int x = startx + row;
                        int y = starty + level;
                        int z = startz + col;
                        Block b = world.getBlockAt(x, y, z);
                        Material type = b.getType();
                        if (type.equals(Material.LAVA)) {
                            b.setType(Material.ORANGE_TERRACOTTA);
                        }
                        if (type.equals(Material.WATER)) {
                            b.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                        }
                    }
                }
            }
        }
    }
}
