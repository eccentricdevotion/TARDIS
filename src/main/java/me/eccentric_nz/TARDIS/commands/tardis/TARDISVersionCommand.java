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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISUpdateChecker;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * @author eccentric_nz
 */
class TARDISVersionCommand {

    private final TARDIS plugin;

    TARDISVersionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean displayVersion(CommandSender sender) {
        List<String> hooks = plugin.getDescription().getSoftDepend();
        String tardisversion = plugin.getDescription().getVersion();
        String cb = Bukkit.getVersion();
        // send server and TARDIS versions
        plugin.getMessenger().message(sender, TardisModule.TARDIS, "Server version: " + ChatColor.AQUA + cb);
        plugin.getMessenger().message(sender, TardisModule.TARDIS, "TARDIS version: " + ChatColor.AQUA + tardisversion);
        // send dependent plugin versions
        for (Plugin hook : plugin.getPM().getPlugins()) {
            PluginDescriptionFile desc = hook.getDescription();
            String name = desc.getName();
            String version = desc.getVersion();
            if (hooks.contains(name)) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, name + " version: " + ChatColor.AQUA + version);
            }
        }
        // check for new TARDIS build
        if (sender.isOp()) {
            plugin.getMessenger().message(sender, TardisModule.TARDIS, "Checking for new TARDIS builds...");
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new TARDISUpdateChecker(plugin, sender));
        }
        return true;
    }
}
