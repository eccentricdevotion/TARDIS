package me.eccentric_nz.TARDIS.blueprints;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlueprint;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISPermission {

    public static boolean hasPermission(Player player, String node) {
        if (player.hasPermission(node)) {
            return true;
        } else if (TARDIS.plugin.getConfig().getBoolean("blueprints.enabled")) {
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

    private static boolean hasBlueprintPermission(String uuid, String node) {
        return new ResultSetBlueprint(TARDIS.plugin).getPerm(uuid, node);
    }
}
