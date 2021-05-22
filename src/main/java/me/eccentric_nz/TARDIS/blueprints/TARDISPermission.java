package me.eccentric_nz.tardis.blueprints;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlueprint;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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

	public static boolean hasPermission(CommandSender sender, String node) {
		if (sender.hasPermission(node)) {
			return true;
		} else if (TARDIS.plugin.getConfig().getBoolean("blueprints.enabled") && sender instanceof Player) {
			// check database
			return hasBlueprintPermission(((Player) sender).getUniqueId().toString(), node);
		} else {
			return false;
		}
	}

	private static boolean hasBlueprintPermission(String uuid, String node) {
		return new ResultSetBlueprint(TARDIS.plugin).getPerm(uuid, node);
	}
}
