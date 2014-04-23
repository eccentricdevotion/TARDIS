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

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSignColourCommand {

    private final TARDIS plugin;
    private final List<String> COLOURS = Arrays.asList("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW");

    public TARDISSignColourCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setColour(CommandSender sender, String[] args) {
        String colour = args[1].toUpperCase();
        if (!COLOURS.contains(colour)) {
            sender.sendMessage(plugin.getPluginName() + "Incorrect colour specified, please check http://jd.bukkit.org/rb/apidocs/org/bukkit/ChatColor.html!");
            return true;
        }
        plugin.getConfig().set("police_box.sign_colour", colour);
        plugin.saveConfig();
        sender.sendMessage(plugin.getPluginName() + MESSAGE.CONFIG_UPDATED.getText());
        return true;
    }
}
