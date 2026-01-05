/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class DechargeCommand {

    private final TARDIS plugin;

    DechargeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean removeChargerStatus(CommandSender sender, String[] args) {
        if (!plugin.getConfig().contains("rechargers." + args[1])) {
            plugin.getMessenger().sendColouredCommand(sender, "CHARGER_NOT_FOUND", "/tardis list rechargers", plugin);
            return true;
        }
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.getWorldGuardUtils().removeRechargerRegion(args[1]);
        }
        plugin.getConfig().set("rechargers." + args[1], null);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "recharger");
        return true;
    }
}
