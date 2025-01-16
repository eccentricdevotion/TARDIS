package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.ButtonModel;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class WayPointInteraction {

    private final TARDIS plugin;

    public WayPointInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openSaveGUI(int id, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.SAVES)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
            return;
        }
        // set custom model data for saves button item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new ButtonModel().setState(display, plugin, ConsoleInteraction.WAYPOINT_SELECTOR);
        }
        TARDISSavesPlanetInventory tssi = new TARDISSavesPlanetInventory(plugin, id, player);
        ItemStack[] saves = tssi.getPlanets();
        Inventory saved = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Dimension Map");
        saved.setContents(saves);
        player.openInventory(saved);
    }
}
