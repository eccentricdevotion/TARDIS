package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class TARDISSaveIconCommand {

	private final TARDIS plugin;

	public TARDISSaveIconCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	public boolean changeIcon(Player player, String[] args) {
		if (TARDISPermission.hasPermission(player, "tardis.save")) {
			if (args.length < 3) {
				TARDISMessage.send(player, "TOO_FEW_ARGS");
				return false;
			}
			ResultSetTardisID rs = new ResultSetTardisID(plugin);
			if (!rs.fromUUID(player.getUniqueId().toString())) {
				TARDISMessage.send(player, "NO_TARDIS");
				return false;
			}
			int id = rs.getTardis_id();
			HashMap<String, Object> whered = new HashMap<>();
			whered.put("dest_name", args[1]);
			whered.put("tardis_id", id);
			ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
			if (!rsd.resultSet()) {
				TARDISMessage.send(player, "SAVE_NOT_FOUND");
				return false;
			}
			Material material;
			try {
				material = Material.valueOf(args[2].toUpperCase(Locale.ROOT));
			} catch (IllegalArgumentException e) {
				TARDISMessage.send(player, "MATERIAL_NOT_VALID");
				return false;
			}
			int destID = rsd.getDest_id();
			HashMap<String, Object> did = new HashMap<>();
			did.put("dest_id", destID);
			HashMap<String, Object> set = new HashMap<>();
			set.put("icon", material.toString());
			plugin.getQueryFactory().doUpdate("destinations", set, did);
			TARDISMessage.send(player, "DEST_ICON", material.toString());
			return true;
		}
		return true;
	}
}
