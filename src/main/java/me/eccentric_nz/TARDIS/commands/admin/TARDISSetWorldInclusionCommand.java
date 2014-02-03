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
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetWorldInclusionCommand {

    private final TARDIS plugin;

    public TARDISSetWorldInclusionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setWorldStatus(CommandSender sender, String[] args) {
        String first = args[0];
        // get world name
        int count = args.length;
        StringBuilder buf = new StringBuilder();
        for (int i = 1; i < count; i++) {
            buf.append(args[i]).append(" ");
        }
        String tmp = buf.toString();
        String t = tmp.substring(0, tmp.length() - 1);
        // need to make there are no periods(.) in the text
        String nodots = StringUtils.replace(t, ".", "_");
        // check the world actually exists!
        if (plugin.getServer().getWorld(nodots) == null) {
            sender.sendMessage(plugin.getPluginName() + ChatColor.RED + "World does not exist!");
            return false;
        }
        if (first.equals("include")) {
            plugin.getConfig().set("worlds." + nodots, true);
        } else {
            plugin.getConfig().set("worlds." + nodots, false);
        }
        plugin.saveConfig();
        sender.sendMessage(plugin.getPluginName() + MESSAGE.CONFIG_UPDATED.getText());
        return true;
    }
}
