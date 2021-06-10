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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISDispersedCommand {

    private final TARDISPlugin plugin;

    TARDISDispersedCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    boolean assemble(CommandSender sender, String which) {
        if (which.equalsIgnoreCase("clear")) {
            plugin.getTrackerKeeper().getDispersed().clear();
            plugin.getTrackerKeeper().getDispersedTARDII().clear();
            TARDISMessage.send(sender, "ASSEMBLE_ALL");
        } else if (which.equalsIgnoreCase("list")) {
            plugin.getTrackerKeeper().getDispersedTARDII().forEach((d) -> plugin.debug("TARDIS id: " + d));
        }
        return true;
    }
}
