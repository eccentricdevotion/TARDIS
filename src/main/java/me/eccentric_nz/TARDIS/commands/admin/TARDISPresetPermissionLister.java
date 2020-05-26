package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TARDISPresetPermissionLister {
    
    public void list(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[TARDIS]" + ChatColor.RESET + " Chameleon Preset Permissions:");
        for (PRESET preset : PRESET.values()) {
            if (preset.getSlot() != -1) {
                sender.sendMessage("tardis.preset." + preset.toString().toLowerCase());
            }
        }
    }
}
