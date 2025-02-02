/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISSiegeCommand {

    private final TARDIS plugin;
    private final List<String> siegeArgs = List.of("enabled", "breeding", "growth", "butcher", "creeper", "healing", "texture");
    private final List<String> siegeBool = List.of("enabled", "butcher", "creeper", "healing", "texture");

    TARDISSiegeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setOption(CommandSender sender, String[] args) {
        String first = args[1].toLowerCase(Locale.ROOT);
        if (args.length == 2 && first.equalsIgnoreCase("true") || first.equalsIgnoreCase("false")) {
            plugin.getConfig().set("siege.enabled", Boolean.valueOf(first));
            plugin.saveConfig();
            return true;
        }
        if (args.length < 3) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        if (!siegeArgs.contains(first)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_NOT_VALID");
            return true;
        }
        if (siegeBool.contains(first)) {
            String tf = args[2].toLowerCase(Locale.ROOT);
            if (!tf.equals("true") && !tf.equals("false")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TRUE_FALSE");
                return true;
            }
            plugin.getConfig().set("siege." + first, Boolean.valueOf(tf));
        }
        if (!siegeBool.contains(first)) {
            int val;
            try {
                val = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                // not a number
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_LAST_NUMBER");
                return false;
            }
            plugin.getConfig().set("siege." + first, val);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        plugin.saveConfig();
        return true;
    }
}
