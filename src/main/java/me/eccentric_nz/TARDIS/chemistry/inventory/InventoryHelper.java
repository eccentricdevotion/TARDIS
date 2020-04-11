package me.eccentric_nz.TARDIS.chemistry.inventory;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InventoryHelper implements Listener {

    private final TARDIS plugin;
    private final List INV_TITLES = Arrays.asList("Chemical compounds", "Lab table", "Product crafting", "Material reducer");

    public InventoryHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChemistryInventoryClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        String name = ChatColor.stripColor(view.getTitle());
        if (INV_TITLES.contains(name)) {
            Player player = (Player) event.getPlayer();
            ItemStack[] leftovers = view.getTopInventory().getContents();
            leftovers[8] = null;
            leftovers[17] = null;
            leftovers[26] = null;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                HashMap<Integer, ItemStack> notadded = player.getInventory().addItem(leftovers);
                if (!notadded.isEmpty()) {
                    Location location = player.getLocation();
                    for (ItemStack is : notadded.values()) {
                        location.getWorld().dropItemNaturally(location, is);
                    }
                }
            }, 1L);
        }
    }
}
