/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetExteriorLightLevel;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;

/**
 * @author eccentric_nz
 */
public class TARDISAdaptiveBoxLampToggler {

    private final TARDIS plugin;

    public TARDISAdaptiveBoxLampToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleLamp(int id, boolean on, ChameleonPreset preset) {
        ResultSetCurrentFromId rs = new ResultSetCurrentFromId(plugin, id);
        if (rs.resultSet()) {
            Current current = rs.getCurrent();
            Block light = current.location().getBlock().getRelative(BlockFace.UP, 2);
            if (preset.usesArmourStand()) {
                if (on) {
                    Levelled levelled = TARDISConstants.LIGHT;
                    // use lamp level preference for this tardis
                    ResultSetExteriorLightLevel rsell = new ResultSetExteriorLightLevel(plugin, id);
                    int level = (rsell.resultSet()) ? LightLevel.exterior_level[rsell.getLevel()] : 9;
                    levelled.setLevel(level);
                    light.setBlockData(levelled);
                } else {
                    light.setBlockData(TARDISConstants.AIR);
                }
            } else {
                Block lamp = current.location().getBlock().getRelative(BlockFace.UP, 3);
                while (!lamp.getChunk().isLoaded()) {
                    lamp.getChunk().load();
                }
                if (lamp.getType().equals(Material.REDSTONE_LAMP)) {
                    if (on) {
                        // turn on
                        light.setBlockData(TARDISConstants.POWER);
                    } else {
                        // turn off
                        light.setBlockData(Material.BLUE_WOOL.createBlockData());
                    }
                }
            }
        }
    }
}
