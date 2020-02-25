package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TARDISPresetPermissionLister {

    private final List<Material> notThese = Arrays.asList(Material.BARRIER, Material.BEDROCK, Material.IRON_INGOT, Material.FIRE);

    public void list(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[TARDIS]" + ChatColor.RESET + " Chameleon Preset Permissions:");
        for (PRESET preset : PRESET.values()) {
            if (!notThese.contains(preset.getCraftMaterial()) || preset.isColoured()) {
                sender.sendMessage("tardis.preset." + preset.toString().toLowerCase());
            }
        }
    }
}
