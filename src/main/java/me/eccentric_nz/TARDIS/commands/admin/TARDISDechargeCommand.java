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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISDechargeCommand {

    private final TARDIS plugin;

    TARDISDechargeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean removeChargerStatus(CommandSender sender, String[] args) {
        if (!plugin.getConfig().contains("rechargers." + args[1])) {
            TARDISMessage.send(sender, "CHARGER_NOT_FOUND", ChatColor.AQUA + " /tardis list rechargers" + ChatColor.RESET + " first.");
            return true;
        }
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.getWorldGuardUtils().removeRechargerRegion(args[1]);
        }
        plugin.getConfig().set("rechargers." + args[1], null);
        plugin.saveConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED", "recharger");
        return true;
    }
}
