package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISRevokeCommand {

	private final TARDIS plugin;

	public TARDISRevokeCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	public boolean removePermission(CommandSender sender, String[] args) {
		// tardisadmin revoke [player] [permission]
		if (args.length < 3) {
			TARDISMessage.send(sender, "TOO_FEW_ARGS");
			return true;
		}
		Player player = plugin.getServer().getPlayer(args[1]);
		if (player == null) {
			TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
			return true;
		}
		int id = new ResultSetBlueprint(plugin).getRecordId(player.getUniqueId().toString(), args[2].toLowerCase());
		if (id != -1) {
			// delete record
			HashMap<String, Object> where = new HashMap<>();
			where.put("bp_id", id);
			plugin.getQueryFactory().doDelete("blueprint", where);
			TARDISMessage.send(sender, "BLUEPRINT_REVOKED");
		} else {
			TARDISMessage.send(sender, "BLUEPRINT_NOT_FOUND");
		}
		return true;
	}
}
