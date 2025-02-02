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
package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class Tachyon {

    private final TARDIS plugin;

    public Tachyon(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, String player, String amount) {
        if (!plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RECIPE_VORTEX");
            return;
        }
        if (Bukkit.getOfflinePlayer(player).getName() == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return;
        }
        // Look up this player's UUID
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (offlinePlayer.getName() != null) {
            UUID uuid = offlinePlayer.getUniqueId();
            plugin.getServer().dispatchCommand(sender, "vm give " + uuid + " " + amount);
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND");
        }
    }
}
