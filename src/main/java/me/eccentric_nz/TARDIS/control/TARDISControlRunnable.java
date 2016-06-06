/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetConsole;
import me.eccentric_nz.TARDIS.database.ResultSetOccupied;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;

/**
 *
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
        rso.resultSet();
        for (int id : rso.getData()) {
            if (!plugin.getTrackerKeeper().getKeyboard().contains(id)) {
                ResultSetConsole rsc = new ResultSetConsole(plugin, id);
                switch (modulo % 2) {
                    case 0:
                        // get the location data
                        if (rsc.locationData()) {
                            if (rsc.getSign().getType().equals(Material.WALL_SIGN) || rsc.getSign().getType().equals(Material.SIGN_POST)) {
                                // get the sign
                                Sign sign = (Sign) rsc.getSign().getState();
                                // update the data
                                String worldname = rsc.getWorld();
                                if (plugin.isMVOnServer()) {
                                    worldname = plugin.getMVHelper().getAlias(rsc.getWorld());
                                }
                                sign.setLine(0, ChatColor.DARK_PURPLE + worldname);
                                sign.setLine(1, rsc.getLocation());
                                sign.setLine(2, rsc.getBiome());
                                sign.setLine(3, ChatColor.BLUE + rsc.getPreset());
                                sign.update();
                            }
                        }
                        break;
                    default:
                        // get the artron data
                        if (rsc.artronData()) {
                            int fc = plugin.getArtronConfig().getInt("full_charge");
                            int current_level = rsc.getArtronLevel();
                            int percent = Math.round((current_level * 100F) / fc);
                            if (rsc.getSign().getType().equals(Material.WALL_SIGN) || rsc.getSign().getType().equals(Material.SIGN_POST)) {
                                // get the sign
                                Sign sign = (Sign) rsc.getSign().getState();
                                // update the data
                                sign.setLine(0, plugin.getLanguage().getString("ARTRON_DISPLAY"));
                                sign.setLine(1, ChatColor.AQUA + plugin.getLanguage().getString("ARTRON_MAX") + ":" + plugin.getArtronConfig().getInt("full_charge"));
                                sign.setLine(2, ChatColor.GREEN + plugin.getLanguage().getString("ARTRON_REMAINING") + ":" + current_level);
                                sign.setLine(3, ChatColor.LIGHT_PURPLE + plugin.getLanguage().getString("ARTRON_PERCENT") + ":" + percent + "%");
                                sign.update();
                            }
                        }
                        break;
                }
            }
        }
        modulo++;
        if (modulo == 2) {
            modulo = 0;
        }
    }
}
