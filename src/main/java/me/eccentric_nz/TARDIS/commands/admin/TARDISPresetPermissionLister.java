package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.enumeration.PRESET;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TARDISPresetPermissionLister {

	public void list(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "[tardis]" + ChatColor.RESET + " Chameleon Preset Permissions:");
		for (PRESET preset : PRESET.values()) {
			if (preset.getSlot() != -1) {
				sender.sendMessage("tardis.preset." + preset.toString().toLowerCase());
			}
		}
	}
}
