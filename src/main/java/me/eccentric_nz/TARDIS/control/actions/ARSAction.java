package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISThemeButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ARSAction {
    private final TARDIS plugin;

    public ARSAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, Tardis tardis, TARDISCircuitChecker tcc, int id) {
        if (!tardis.isHandbrake_on()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_NO_TRAVEL");
            return;
        }
        // check they're in a compatible world
        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_OWN_WORLD");
            return;
        }
        if (player.isSneaking()) {
            // check they have permission to change the desktop
            if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_UPGRADE");
                return;
            }
            if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                return;
            }
            // upgrade menu
            new TARDISThemeButton(plugin, player, tardis.getSchematic(), tardis.getArtron_level(), id).clickButton();
        } else {
            // check they have permission to grow rooms
            if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_ROOMS");
                return;
            }
            if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                return;
            }
            ItemStack[] tars = new TARDISARSInventory(plugin, player).getARS();
            Inventory ars = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
            ars.setContents(tars);
            player.openInventory(ars);
        }
    }
}
