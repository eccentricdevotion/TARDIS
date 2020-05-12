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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISVersionCommand {

    private final TARDIS plugin;

    TARDISVersionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean displayVersion(CommandSender sender) {
        final PluginManager pm = plugin.getPM();
        List<String> hooks = plugin.getDescription().getSoftDepend();

        String tardisversion = pm.getPlugin("TARDIS").getDescription().getVersion();
        String chunkversion = pm.getPlugin("TARDISChunkGenerator").getDescription().getVersion();
        String cb = Bukkit.getVersion();
        String bv = Bukkit.getBukkitVersion();
        sender.sendMessage(plugin.getPluginName() + "Server version: " + ChatColor.AQUA + bv + " " + cb);
        sender.sendMessage(plugin.getPluginName() + "TARDIS version: " + ChatColor.AQUA + tardisversion);
        sender.sendMessage(plugin.getPluginName() + "TARDISChunkGenerator version: " + ChatColor.AQUA + chunkversion);

        for (Plugin hook : pm.getPlugins()) {
            final PluginDescriptionFile desc = hook.getDescription();
            String name = desc.getName();
            String version = desc.getVersion();

            if(hooks.contains(name) && !name.equals("TARDISChunkGenerator")) {
                sender.sendMessage(plugin.getPluginName() + name + " version: " + ChatColor.AQUA + version);
            }
        }

        return true;
    }
}
