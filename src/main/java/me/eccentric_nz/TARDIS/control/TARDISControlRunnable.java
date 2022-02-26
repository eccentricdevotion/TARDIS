/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsole;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOccupied;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.ChatColor;
import org.bukkit.Tag;
import org.bukkit.block.Sign;

/**
 * @author eccentric_nz
 */
public class TARDISControlRunnable implements Runnable {

    private final TARDIS plugin;
    private int modulo = 0;

    public TARDISControlRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // get occupied TARDISes
        ResultSetOccupied rso = new ResultSetOccupied(plugin);
        rso.resultSetAsync(resultSetOccupied -> {
            for (int id : rso.getData()) {
                ResultSetConsole rsc = new ResultSetConsole(plugin, id);
                if (modulo % 2 == 0) {
                    // get the location data
                    rsc.locationAsync((hasResult, resultSetConsole) -> {
                        if (hasResult) {
                            if (Tag.SIGNS.isTagged(resultSetConsole.getSign().getType())) {
                                // get the sign
                                Sign sign = (Sign) resultSetConsole.getSign().getState();
                                // update the data
                                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    sign.setLine(0, ChatColor.DARK_PURPLE + "Drifting in the");
                                    sign.setLine(1, ChatColor.DARK_PURPLE + "time vortex...");
                                    sign.setLine(2, "");
                                } else {
                                    String worldName = (resultSetConsole.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(resultSetConsole.getWorld()) : "";
                                    if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldName.equals("")) {
                                        worldName = plugin.getMVHelper().getAlias(worldName);
                                    }
                                    sign.setLine(0, ChatColor.DARK_PURPLE + worldName);
                                    sign.setLine(1, ChatColor.BLACK + resultSetConsole.getLocation());
                                    sign.setLine(2, ChatColor.BLACK + resultSetConsole.getBiome());
                                }
                                sign.setLine(3, ChatColor.BLUE + resultSetConsole.getPreset());
                                sign.update();
                            }
                        }
                    });
                } else {
                    // get the artron data
                    rsc.artronAsync((hasResult, resultSetConsole) -> {
                        if (hasResult) {
                            int fc = plugin.getArtronConfig().getInt("full_charge");
                            int current_level = resultSetConsole.getArtronLevel();
                            int percent = Math.round((current_level * 100.0f) / fc);
                            if (Tag.SIGNS.isTagged(resultSetConsole.getSign().getType())) {
                                // get the sign
                                Sign sign = (Sign) resultSetConsole.getSign().getState();
                                // update the data
                                sign.setLine(0, ChatColor.BLACK + plugin.getLanguage().getString("ARTRON_DISPLAY"));
                                sign.setLine(1, ChatColor.AQUA + plugin.getLanguage().getString("ARTRON_MAX") + ":" + plugin.getArtronConfig().getInt("full_charge"));
                                sign.setLine(2, ChatColor.GREEN + plugin.getLanguage().getString("ARTRON_REMAINING") + ":" + current_level);
                                sign.setLine(3, ChatColor.LIGHT_PURPLE + plugin.getLanguage().getString("ARTRON_PERCENT") + ":" + percent + "%");
                                sign.update();
                            }
                        }
                    });
                }
            }
            modulo++;
            if (modulo == 2) {
                modulo = 0;
            }
        });
    }
}

