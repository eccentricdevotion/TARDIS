/*
 * Copyright (C) 2014 eccentric_nz
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
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMVModifyCommand {

    private final TARDIS plugin;

    public TARDISMVModifyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setMobSpawning(CommandSender sender) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (!plugin.getPM().isPluginEnabled("Multiverse-Core")) {
            sender.sendMessage(plugin.getPluginName() + "Multiverse-Core is not enabled!");
            return true;
        }
        // set animals / monsters true - set animalsrate / monstersrate 0
        for (World w : plugin.getServer().getWorlds()) {
            if (w.getName().contains("TARDIS")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set animals true " + w.getName());
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set monsters true " + w.getName());
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set animalsrate 0 " + w.getName());
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set monstersrate 0 " + w.getName());
                if (p != null) {
                    p.sendMessage(plugin.getPluginName() + "Multiverse settings for " + w.getName() + " modified!");
                }
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv reload");
            }
        }
        return true;
    }
}
