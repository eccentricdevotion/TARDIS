package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TARDISRecipeInventoryListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISRecipeInventoryListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRecipeInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Recipes")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 27) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    switch (slot) {
                        case 0:
                            // back
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                ItemStack[] emenu = new TARDISRecipeCategoryInventory().getMenu();
                                Inventory categories = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Recipe Categories");
                                categories.setContents(emenu);
                                player.openInventory(categories);
                            }, 2L);
                            break;
                        case 4:
                            // info
                            break;
                        case 8:
                            // close
                            close(player);
                            break;
                        default:
                            String command = ChatColor.stripColor(is.getItemMeta().getLore().get(0)).substring(1);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                player.performCommand(command);
                                plugin.getTrackerKeeper().getRecipeView().add(player.getUniqueId());
                            }, 2L);
                            break;
                    }
                }
            }
        }
    }
}
