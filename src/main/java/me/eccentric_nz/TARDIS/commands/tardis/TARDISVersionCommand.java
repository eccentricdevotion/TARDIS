/*
 * Copyright (C) 2020 eccentric_nz
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
 * @author eccentric_nz
 */
class TARDISVersionCommand {

    private final TARDIS plugin;

    TARDISVersionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean displayVersion(CommandSender sender) {
        String version = plugin.getPM().getPlugin("TARDIS").getDescription().getVersion();
        String chunkversion = plugin.getPM().getPlugin("TARDISChunkGenerator").getDescription().getVersion();
        String cb = Bukkit.getVersion();
        String bv = Bukkit.getBukkitVersion();
        sender.sendMessage(plugin.getPluginName() + "TARDIS version: " + ChatColor.AQUA + version);// + ChatColor.RESET + " running " + implementation + cb);
        sender.sendMessage(plugin.getPluginName() + "TARDISChunkGenerator version: " + ChatColor.AQUA + chunkversion);
        sender.sendMessage(plugin.getPluginName() + "Server version: " + ChatColor.AQUA + bv + " " + cb);
        return true;
    }
}
