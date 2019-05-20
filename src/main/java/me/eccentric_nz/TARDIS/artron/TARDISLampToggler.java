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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISLampToggler {

    private final TARDIS plugin;

    public TARDISLampToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void flickSwitch(int id, UUID uuid, boolean on, boolean lantern) {
        // get lamp locations
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, true);
        if (rsl.resultSet()) {
            HashMap<String, Object> wherepp = new HashMap<>();
            wherepp.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            boolean use_wool = false;
            if (rsp.resultSet()) {
                use_wool = rsp.isWoolLightsOn();
                lantern = rsp.isLanternsOn();
            }
            BlockData onlamp = (lantern) ? TARDISConstants.LANTERN : TARDISConstants.LAMP;
            for (Block b : rsl.getData()) {
                while (!b.getChunk().isLoaded()) {
                    b.getChunk().load();
                }
                if (on) {
                    if (b.getType().equals(Material.SEA_LANTERN) || (b.getType().equals(Material.REDSTONE_LAMP))) {
                        if (use_wool) {
                            b.setBlockData(TARDISConstants.BLACK);
                        } else if (lantern) {
                            b.setBlockData(Material.INFESTED_STONE.createBlockData());
                        } else {
                            b.setBlockData(Material.SPONGE.createBlockData());
                        }
                    }
                } else if (b.getType().equals(Material.SPONGE) || b.getType().equals(Material.INFESTED_STONE) || b.getType().equals(Material.BLACK_WOOL)) {
                    b.setBlockData(onlamp);
                }
            }
        }
    }
}
