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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TARDISWeatherListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISWeatherListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherMenuInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Weather Menu")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (!plugin.getConfig().getBoolean("allow.weather_set")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_DISABLED");
            close(player);
            return;
        }
        if (slot < 0 || slot > 8) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        // check they initialised
        if (!tardis.isTardisInit()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
            return;
        }
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
            return;
        }
        if (!tardis.isHandbrakeOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
            return;
        }
        // get current location
        Current current = TARDISCache.CURRENT.get(tardis.getTardisId());
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            close(player);
            return;
        }
        switch (slot) {
            case 0 -> {
                // clear / sun
                if (TARDISPermission.hasPermission(player, "tardis.weather.clear")) {
                    TARDISWeather.setClear(current.location().getWorld());
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_SET", "clear");
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                }
                close(player);
            }
            case 1 -> {
                // rain
                if (TARDISPermission.hasPermission(player, "tardis.weather.rain")) {
                    TARDISWeather.setRain(current.location().getWorld());
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_SET", "rain");
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                }
                close(player);
            }
            case 2 -> {
                // thunderstorm
                if (TARDISPermission.hasPermission(player, "tardis.weather.thunder")) {
                    TARDISWeather.setThunder(current.location().getWorld());
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_SET", "thunder");
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                }
                close(player);
            }
            case 5 -> {
                // atmospheric excitation
                if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_EXCITE");
                    return;
                }
                new TARDISAtmosphericExcitation(plugin).excite(tardis.getTardisId(), player);
                plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                close(player);
            }
            // close
            case 8 -> close(player);
            default -> { }
        }
    }
}
