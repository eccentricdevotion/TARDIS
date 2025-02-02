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
package me.eccentric_nz.TARDIS.blueprints;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISPermission {

    public static boolean hasPermission(Player player, String node) {
        if (player.hasPermission(node)) {
            return true;
        } else if (TARDIS.plugin.getConfig().getBoolean("modules.blueprints")) {
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
        Player player = TARDIS.plugin.getServer().getPlayer(uuid);
        return player != null && hasPermission(player, node);
    }

    public static boolean hasPermission(CommandSender sender, String node) {
        if (sender.hasPermission(node)) {
            return true;
        } else if (TARDIS.plugin.getConfig().getBoolean("modules.blueprints") && sender instanceof Player) {
            // check database
            return hasBlueprintPermission(((Player) sender).getUniqueId().toString(), node);
        } else {
            return false;
        }
    }

    /**
     * Checks to see if a player has this permission node SET to FALSE.
     * If the player simply does not have this permission (unset), this will return FALSE.
     * This will only return true if the player has the node, and it's negated.
     *
     * @param player the player to check
     * @param node   the node to check
     * @return true if the player has the node, and it's negated
     */
    public static boolean isNegated(Player player, String node) {
        return player.isPermissionSet(node) && !player.hasPermission(node);
    }

    private static boolean hasBlueprintPermission(String uuid, String node) {
        return new ResultSetBlueprint(TARDIS.plugin).getPerm(uuid, node);
    }
}
