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
package me.eccentric_nz.TARDIS.commands.tardis;

import io.papermc.paper.plugin.configuration.PluginMeta;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class VersionCommand {

    private final TARDIS plugin;

    public VersionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void displayVersion(CommandSender sender) {
        List<String> hooks = plugin.getPluginMeta().getPluginSoftDependencies();
        String tardisVersion = plugin.getPluginMeta().getVersion();
        String cb = Bukkit.getVersion();
        // send server and TARDIS versions
        plugin.getMessenger().sendWithColours(sender, TardisModule.TARDIS, "Server version: ", "#FFFFFF", cb, "#55FFFF");
        plugin.getMessenger().sendWithColours(sender, TardisModule.TARDIS, "TARDIS version: ", "#FFFFFF", tardisVersion, "#55FFFF");
        // send dependent plugin versions
        for (Plugin hook : plugin.getPM().getPlugins()) {
            PluginMeta desc = hook.getPluginMeta();
            String name = desc.getName();
            String version = desc.getVersion();
            if (hooks.contains(name)) {
                plugin.getMessenger().sendWithColours(sender, TardisModule.TARDIS, name + " version: ", "#FFFFFF", version, "#55FFFF");
            }
        }
    }
}
