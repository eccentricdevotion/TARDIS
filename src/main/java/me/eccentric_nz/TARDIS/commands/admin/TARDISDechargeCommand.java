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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDechargeCommand {

    private final TARDIS plugin;

    public TARDISDechargeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean removeChragerStatus(CommandSender sender, String[] args) {
        if (!plugin.getConfig().contains("rechargers." + args[1])) {
            sender.sendMessage(plugin.pluginName + "Could not find a recharger with that name! Try using " + ChatColor.AQUA + "/tardis list rechargers" + ChatColor.RESET + " first.");
            return true;
        }
        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.wgutils.removeRechargerRegion(args[1]);
        }
        plugin.getConfig().set("rechargers." + args[1], null);
        plugin.saveConfig();
        sender.sendMessage(plugin.pluginName + MESSAGE.CONFIG_UPDATED.getText());
        return true;
    }
}
