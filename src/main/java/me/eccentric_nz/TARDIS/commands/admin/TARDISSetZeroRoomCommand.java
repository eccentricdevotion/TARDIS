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

import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetZeroRoomCommand {

    private final TARDIS plugin;

    public TARDISSetZeroRoomCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setConfigZero(CommandSender sender, String[] args) {
        // check they typed true of false
        String tf = args[1].toLowerCase(Locale.ENGLISH);
        if (!tf.equals("true") && !tf.equals("false")) {
            sender.sendMessage(plugin.getPluginName() + ChatColor.RED + "The last argument must be true or false!");
            return false;
        }
        plugin.getConfig().set("allow.zero_room", Boolean.valueOf(tf));
        plugin.saveConfig();
        sender.sendMessage(plugin.getPluginName() + MESSAGE.CONFIG_UPDATED.getText());
        if (tf.equals("true") && plugin.getServer().getWorld("TARDIS_Zero_Room") == null) {
            sender.sendMessage(plugin.getPluginName() + "The TARDIS_Zero_Room does not exist, attempting to create it!");
            new TARDISSpace(plugin).createDefaultWorld("TARDIS_Zero_Room");
            sender.sendMessage(plugin.getPluginName() + "A server restart is also required to enable Zero room isolation and healing.");
        }
        return true;
    }
}
