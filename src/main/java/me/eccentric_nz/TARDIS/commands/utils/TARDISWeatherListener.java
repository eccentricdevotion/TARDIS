/*
 * Copyright (C) 2023 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.utils;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TARDISWeatherListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISWeatherListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherMenuInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Weather Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (!plugin.getConfig().getBoolean("allow.weather_set")) {
                TARDISMessage.send(player, "WEATHER_DISABLED");
                return;
            }
            if (slot >= 0 && slot < 9) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            // check they initialised
                            if (!tardis.isTardis_init()) {
                                TARDISMessage.send(player, "ENERGY_NO_INIT");
                                return;
                            }
                            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                                TARDISMessage.send(player, "POWER_DOWN");
                                return;
                            }
                            if (!tardis.isHandbrake_on()) {
                                TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                return;
                            }
                            // get current location
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", tardis.getTardis_id());
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                            if (!rsc.resultSet()) {
                                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                close(player);
                            }
                            switch (slot) {
                                case 0 -> {
                                    // clear / sun
                                    if (TARDISPermission.hasPermission(player, "tardis.weather.clear")) {
                                        TARDISWeather.setClear(rsc.getWorld());
                                        TARDISMessage.send(player, "WEATHER_SET", "clear");
                                    } else {
                                        TARDISMessage.send(player, "NO_PERMS");
                                    }
                                    close(player);
                                }
                                case 1 -> {
                                    // rain
                                    if (TARDISPermission.hasPermission(player, "tardis.weather.rain")) {
                                        TARDISWeather.setRain(rsc.getWorld());
                                        TARDISMessage.send(player, "WEATHER_SET", "rain");
                                    } else {
                                        TARDISMessage.send(player, "NO_PERMS");
                                    }
                                    close(player);
                                }
                                case 2 -> {
                                    // thunderstorm
                                    if (TARDISPermission.hasPermission(player, "tardis.weather.thunder")) {
                                        TARDISWeather.setThunder(rsc.getWorld());
                                        TARDISMessage.send(player, "WEATHER_SET", "thunder");
                                    } else {
                                        TARDISMessage.send(player, "NO_PERMS");
                                    }
                                    close(player);
                                }
                                case 5 -> {
                                    // atmospheric excitation
                                    if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
                                        TARDISMessage.send(player, "CMD_EXCITE");
                                        return;
                                    }
                                    new TARDISAtmosphericExcitation(plugin).excite(tardis.getTardis_id(), player);
                                    plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                                    close(player);
                                }
                                case 8 ->
                                    // close
                                        close(player);
                                default -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
