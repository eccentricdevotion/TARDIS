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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISRunnables;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class PowerDownCommand {

    private final TARDIS plugin;

    PowerDownCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean togglePowerDown(CommandSender sender, String[] args) {
        // check they typed true of false
        String tf = args[1].toLowerCase(Locale.ROOT);
        if (!tf.equals("true") && !tf.equals("false")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TRUE_FALSE");
            return false;
        }
        plugin.getConfig().set("allow.power_down", Boolean.valueOf(tf));
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "power_down");
        if (tf.equals("false")) {
            // if false, stop the repeating task
            plugin.getStandbyTask().cancel();
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "STANDBY_STOP");
        } else {
            // if true, start the repeating task
            TARDISRunnables.startStandBy(plugin);
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "STANDBY_START");
        }
        return true;
    }
}
