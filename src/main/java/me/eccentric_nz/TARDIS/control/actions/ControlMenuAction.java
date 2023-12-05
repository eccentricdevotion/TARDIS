package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISControlInventory;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ControlMenuAction {

    private final TARDIS plugin;

    public ControlMenuAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, int id) {
        if (player.isSneaking()) {
            return;
            // keyboard
        } else {
            UUID playerUUID = player.getUniqueId();
            // controls GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                TARDISFloodgate.sendControlForm(playerUUID);
            } else {
                ItemStack[] controls = new TARDISControlInventory(plugin, id).getControls();
                Inventory cgui = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Control Menu");
                cgui.setContents(controls);
                player.openInventory(cgui);
            }
        }
    }
}
