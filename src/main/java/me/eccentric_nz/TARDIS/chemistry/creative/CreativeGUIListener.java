package me.eccentric_nz.TARDIS.chemistry.creative;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CreativeGUIListener implements Listener {

    private final TARDIS plugin;

    public CreativeGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreativeMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Molecular compounds") || name.equals(ChatColor.DARK_RED + "Products")) {
            Player p = (Player) event.getWhoClicked();
            UUID uuid = p.getUniqueId();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 53:
                        // close
                        event.setCancelled(true);
                        close(p);
                        break;
                    default:
                        event.setCancelled(true);
                        // get clicked ItemStack
                        if (view.getItem(slot) != null) {
                            ItemStack choice = view.getItem(slot).clone();
                            choice.setAmount(event.getClick().equals(ClickType.SHIFT_LEFT) ? 64 : 1);
                            // add ItemStack to inventory if there is room
                            p.getInventory().addItem(choice);
                        }
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }
}
