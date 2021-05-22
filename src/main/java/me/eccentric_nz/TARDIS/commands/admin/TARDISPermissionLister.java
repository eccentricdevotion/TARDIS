package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TARDIS;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TARDISPermissionLister {

	private final TARDIS plugin;

	public TARDISPermissionLister(TARDIS plugin) {
		this.plugin = plugin;
	}

	void listPerms(CommandSender sender) {
		List<String> perms = new ArrayList<>(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("permissions").getKeys(true));
		perms.sort(Comparator.naturalOrder());
		String lastPerm = "";
		for (int i = perms.size() - 1; i >= 0; i--) {
			String perm = perms.get(i);
			if (perm.contains(".") && notThese(perm)) {
				if (!lastPerm.contains(perm)) {
					sender.sendMessage(perm);
					lastPerm = perm;
				}
			}
		}
	}

	private boolean notThese(String perm) {
		return !perm.contains("children") && !perm.contains("description") && !perm.contains("default") && !perm.contains("*");
	}
}
