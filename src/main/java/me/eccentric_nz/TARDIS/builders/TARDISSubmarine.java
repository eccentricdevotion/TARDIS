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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Creates an umbrella over the Police Box when it lands underwater.
 */
public class TARDISSubmarine {

    private final TARDIS plugin;

    public TARDISSubmarine(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void sheild(Location l, TARDISConstants.COMPASS d) {
        if (plugin.worldGuardOnServer) {
            Block sponge = l.getBlock();
            sponge.setTypeId(19);
            plugin.wgutils.sponge(sponge, true);
        }
    }
}
