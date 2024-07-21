package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateTemporalForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TemporarlLocatorAction {

    private final TARDIS plugin;

    public TemporarlLocatorAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, TARDISCircuitChecker tcc) {
        if (!TARDISPermission.hasPermission(player, "tardis.temporal")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TEMPORAL");
            return;
        }
        if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TEMP_MISSING");
            return;
        }
        UUID playerUUID = player.getUniqueId();
        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
            new FloodgateTemporalForm(plugin, playerUUID).send();
        } else {
            ItemStack[] clocks = new TARDISTemporalLocatorInventory(plugin).getTemporal();
            Inventory tmpl = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Temporal Locator");
            tmpl.setContents(clocks);
            player.openInventory(tmpl);
        }
    }
}
