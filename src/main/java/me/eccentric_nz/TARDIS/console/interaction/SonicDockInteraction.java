package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicDock;
import org.bukkit.Material;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SonicDockInteraction {

    private final TARDIS plugin;

    public SonicDockInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player, Interaction interaction, int id) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.BLAZE_ROD) && is.hasItemMeta()) {
            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
            if (im.getDisplayName().endsWith("Sonic Screwdriver")) {
                new TARDISSonicDock(plugin).dock(id, interaction, player, is);
            }
        } else if (is.getType() == Material.AIR) {
            new TARDISSonicDock(plugin).undock(interaction, player);
        }
    }
}
