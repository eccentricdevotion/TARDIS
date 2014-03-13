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
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetBooleanCommand {

    private final TARDIS plugin;

    public TARDISSetBooleanCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setConfigBool(CommandSender sender, String[] args, String section) {
        String first = (section.isEmpty()) ? args[0].toLowerCase() : section + "." + args[0].toLowerCase();
        // check they typed true of false
        String tf = args[1].toLowerCase(Locale.ENGLISH);
        if (!tf.equals("true") && !tf.equals("false")) {
            sender.sendMessage(plugin.getPluginName() + ChatColor.RED + "The last argument must be true or false!");
            return false;
        }
        plugin.getConfig().set(first, Boolean.valueOf(tf));
        plugin.saveConfig();
        sender.sendMessage(plugin.getPluginName() + MESSAGE.CONFIG_UPDATED.getText());
        return true;
    }
}
