package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chameleon.shell.TARDISShellInventory;
import me.eccentric_nz.TARDIS.chameleon.shell.TARDISShellPresetInventory;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateShellLoaderForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShellRoomAction {

    private final TARDIS plugin;

    public ShellRoomAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, int id) {
        if (plugin.getConfig().getBoolean("police_box.load_shells") && player.isSneaking()) {
            if (!TARDISPermission.hasPermission(player, "tardis.load_shells")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                return;
            }
            // Chameleon load GUI
            UUID playerUUID = player.getUniqueId();
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                new FloodgateShellLoaderForm(plugin, playerUUID).send();
            } else {
                ItemStack[] shells = new TARDISShellPresetInventory(plugin, player, id).getShells();
                Inventory sgui = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Shell Loader");
                sgui.setContents(shells);
                player.openInventory(sgui);
            }
        } else {
            // load player shells GUI
            ItemStack[] shellStacks = new TARDISShellInventory(plugin, id).getPlayerShells();
            Inventory playerShells = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Shells");
            playerShells.setContents(shellStacks);
            player.openInventory(playerShells);
        }
    }
}
