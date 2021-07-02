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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TARDISDisguiseCommand {

    private final TARDIS plugin;

    public TARDISDisguiseCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void disguise(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("disguise")) {
            Player player = null;
            if (args.length == 3) {
                player = plugin.getServer().getPlayer(args[2]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.message(sender, "You need to specify a player!");
                return;
            }
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(args[1]);
            } catch (IllegalArgumentException e) {
                TARDISMessage.message(sender, "You need to specify a valid living entity type!");
                return;
            }
            plugin.getTardisHelper().disguise(entityType, player);
        }
        if (args[0].equalsIgnoreCase("undisguise")) {
            Player player = null;
            if (args.length == 2) {
                player = plugin.getServer().getPlayer(args[1]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.message(sender, "You need to specify a player!");
                return;
            }
            plugin.getTardisHelper().undisguise(player);
        }
    }
}
