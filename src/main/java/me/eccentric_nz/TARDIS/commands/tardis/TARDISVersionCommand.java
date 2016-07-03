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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISVersionCommand {

    private final TARDIS plugin;

    public TARDISVersionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean displayVersion(CommandSender sender) {
        String version = plugin.getPM().getPlugin("TARDIS").getDescription().getVersion();
        String cb = Bukkit.getVersion();
        sender.sendMessage(plugin.getPluginName() + "You are running TARDIS version: " + ChatColor.AQUA + version + ChatColor.RESET + " with CraftBukkit " + cb);
        return true;
    }
}
