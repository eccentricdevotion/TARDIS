package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ProductGUIListener implements Listener {

    private final TARDIS plugin;
    private final List<Integer> slots = Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
    private final List<Integer> pipe = Arrays.asList(2, 11, 20);

    public ProductGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onProductMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Product crafting")) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 0:
                    case 1:
                    case 2:
                    case 9:
                    case 10:
                    case 11:
                    case 18:
                    case 19:
                    case 20:
                    case 14:
                        // do nothing
                        break;
                    case 17:
                        // craft
                        event.setCancelled(true);
                        craft(event.getClickedInventory(), player);
                        break;
                    case 26:
                        // close
                        event.setCancelled(true);
                        close(player);
                        break;
                    default:
                        event.setCancelled(true);
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

    private void craft(Inventory inventory, Player player) {
        StringBuilder builder = new StringBuilder();
        for (int slot : slots) {
            ItemStack is = inventory.getItem(slot);
            if (is != null) {
                Material material = is.getType();
                if ((material.equals(Material.GLASS_BOTTLE) || material.equals(Material.FEATHER)) && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName()) {
                        builder.append(im.getDisplayName()).append(pipe.contains(slot) ? "|" : ",");
                    }
                } else {
                    builder.append(is.getType().toString()).append(pipe.contains(slot) ? "|" : ",");
                }
            } else {
                builder.append("-").append(pipe.contains(slot) ? "|" : ",");
            }
        }
        String recipe = builder.toString().substring(0, builder.length() - 1);
        for (Product product : Product.values()) {
            if (product.getRecipe().equals(recipe)) {
                craft(product, inventory, player);
                return;
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }

    private void craft(Product product, Inventory inventory, Player player) {
        ItemStack crafted = ProductBuilder.getProduct(product);
        // set slot 14 to the crafted product
        inventory.setItem(14, crafted);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        // remove the crafting item stacks
        for (int i : slots) {
            inventory.setItem(i, null);
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
