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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISSpace;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class SetZeroRoomCommand {

    private final TARDIS plugin;

    SetZeroRoomCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setConfigZero(CommandSender sender, boolean b) {
        // check they typed true of false
        plugin.getConfig().set("allow.zero_room", b);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "zero_room");
        if (b && plugin.getServer().getWorld("TARDIS_Zero_Room") == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ZERO_CREATE");
            new TARDISSpace(plugin).createDefaultWorld("TARDIS_Zero_Room");
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ZERO_RESTART");
        }
        return true;
    }
}
