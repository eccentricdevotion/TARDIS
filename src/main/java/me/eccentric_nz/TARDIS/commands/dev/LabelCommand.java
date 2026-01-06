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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.library.LibraryCatalogue;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LabelCommand {

    private final TARDIS plugin;

    public LabelCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean catalog(CommandSender sender) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            // get start location
            if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "SCHM_NO_START");
                return true;
            }
            Location start = plugin.getTrackerKeeper().getStartLocation().get(uuid);
            new LibraryCatalogue().label(start);
        }
        return true;
    }
}
