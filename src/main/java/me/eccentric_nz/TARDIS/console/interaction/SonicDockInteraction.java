package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.SonicDockModel;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicDock;
import org.bukkit.Material;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class SonicDockInteraction {

    private final TARDIS plugin;

    public SonicDockInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player, Interaction interaction, int id) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        boolean activate = false;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.BLAZE_ROD) && is.hasItemMeta()) {
            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
            if (im.getDisplayName().endsWith("Sonic Screwdriver")) {
                new TARDISSonicDock(plugin).dock(id, interaction, player, is);
                activate = true;
            }
        } else if (is.getType() == Material.AIR) {
            new TARDISSonicDock(plugin).undock(interaction, player);
        }
        // set custom model data for lamp level switch item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new SonicDockModel().setState(display, activate);
        }
    }
}
