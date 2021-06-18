/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.config;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisSpace;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TardisSetZeroRoomCommand {

    private final TardisPlugin plugin;

    TardisSetZeroRoomCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean setConfigZero(CommandSender sender, String[] args) {
        // check they typed true of false
        String tf = args[1].toLowerCase(Locale.ENGLISH);
        if (!tf.equals("true") && !tf.equals("false")) {
            TardisMessage.send(sender, "TRUE_FALSE");
            return false;
        }
        plugin.getConfig().set("allow.zero_room", Boolean.valueOf(tf));
        plugin.saveConfig();
        TardisMessage.send(sender, "CONFIG_UPDATED");
        if (tf.equals("true") && plugin.getServer().getWorld("TARDIS_Zero_Room") == null) {
            TardisMessage.send(sender, "ZERO_CREATE");
            new TardisSpace(plugin).createDefaultWorld("TARDIS_Zero_Room");
            TardisMessage.send(sender, "ZERO_RESTART");
        }
        return true;
    }
}
