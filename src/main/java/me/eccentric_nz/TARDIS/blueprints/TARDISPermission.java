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
package me.eccentric_nz.tardis.blueprints;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlueprint;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISPermission {

    public static boolean hasPermission(Player player, String node) {
        if (player.hasPermission(node)) {
            return true;
        } else if (TARDISPlugin.plugin.getConfig().getBoolean("blueprints.enabled")) {
            // check database
            return hasBlueprintPermission(player.getUniqueId().toString(), node);
        } else {
            return false;
        }
    }

    public static boolean hasPermission(OfflinePlayer offlinePlayer, String node) {
        Player player = offlinePlayer.getPlayer();
        return player != null && hasPermission(player, node);
    }

    public static boolean hasPermission(UUID uuid, String node) {
        Player player = TARDISPlugin.plugin.getServer().getPlayer(uuid);
        return player != null && hasPermission(player, node);
    }

    public static boolean hasPermission(CommandSender sender, String node) {
        if (sender.hasPermission(node)) {
            return true;
        } else if (TARDISPlugin.plugin.getConfig().getBoolean("blueprints.enabled") && sender instanceof Player) {
            // check database
            return hasBlueprintPermission(((Player) sender).getUniqueId().toString(), node);
        } else {
            return false;
        }
    }

    private static boolean hasBlueprintPermission(String uuid, String node) {
        return new ResultSetBlueprint(TARDISPlugin.plugin).getPerm(uuid, node);
    }
}
