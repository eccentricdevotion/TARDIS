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

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.ArtronFurnaceUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPoweredFurnaces;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Furnace;

public class ArtronPoweredRunnable implements Runnable {

    private final TARDIS plugin;
    private final Integer burnTime;

    public ArtronPoweredRunnable(TARDIS plugin) {
        this.plugin = plugin;
        burnTime = plugin.getArtronConfig().getInt("artron_furnace.power_cycle");
    }

    @Override
    public void run() {
        // get all TARDIS powered furnaces
        ResultSetPoweredFurnaces rs = new ResultSetPoweredFurnaces(plugin);
        rs.fetchAsync((hasResult, resultSetBlocks) -> {
            if (hasResult) {
                for (Pair<String, Integer> loc : rs.getData()) {
                    Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(loc.getFirst());
                    if (location != null && location.getBlock().getState() instanceof Furnace furnace) {
                        if (plugin.getTardisHelper().isArtronFurnace(furnace.getBlock())) {
                            // does the TARDIS have enough power?
                            ResultSetTardisArtron rsa = new ResultSetTardisArtron(plugin);
                            if (rsa.fromID(loc.getSecond()) && rsa.getArtronLevel() > plugin.getArtronConfig().getInt("artron_furnace.power_drain")) {
                                if (furnace.getInventory().getSmelting() != null) {
                                    // power the furnace
                                    furnace.setBurnTime(burnTime.shortValue());
                                    furnace.update(true);
                                    TARDISArtronFurnaceListener.setLit(furnace.getBlock(), true);
                                    // drain power from tardis
                                    ArtronFurnaceUtils.drain(loc.getSecond(), plugin);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
