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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSiegeCommand {

    private final TARDIS plugin;
    private final List<String> siegeArgs = Arrays.asList("enabled", "breeding", "growth", "butcher", "creeper", "healing", "texture");
    private final List<String> siegeBool = Arrays.asList("enabled", "butcher", "creeper", "healing", "texture");

    public TARDISSiegeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setOption(CommandSender sender, String[] args) {
        if (args.length == 2 && args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
            plugin.getConfig().set("siege.enabled", Boolean.valueOf(args[1].toLowerCase(Locale.ENGLISH)));
            plugin.saveConfig();
            return true;
        }
        if (args.length < 3) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        if (!siegeArgs.contains(args[1].toLowerCase(Locale.ENGLISH))) {
            TARDISMessage.send(sender, "ARG_NOT_VALID");
            return true;
        }
        if (siegeBool.contains(args[1].toLowerCase(Locale.ENGLISH))) {
            String tf = args[2].toLowerCase(Locale.ENGLISH);
            if (!tf.equals("true") && !tf.equals("false")) {
                TARDISMessage.send(sender, "TRUE_FALSE");
                return true;
            }
            plugin.getConfig().set("siege." + args[1].toLowerCase(Locale.ENGLISH), Boolean.valueOf(tf));
        }
        if (!siegeBool.contains(args[1].toLowerCase(Locale.ENGLISH))) {
            int val;
            try {
                val = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                // not a number
                TARDISMessage.send(sender, "ARG_LAST_NUMBER");
                return false;
            }
            plugin.getConfig().set("siege." + args[1].toLowerCase(Locale.ENGLISH), val);
        }
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        plugin.saveConfig();
        return true;
    }
}
