package me.eccentric_nz.TARDIS.chemistry.reducer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.compound.Compound;
import me.eccentric_nz.TARDIS.chemistry.element.Element;
import me.eccentric_nz.TARDIS.chemistry.element.ElementBuilder;
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

public class ReducerGUIListener implements Listener {

    private final TARDIS plugin;

    public ReducerGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompoundMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Material reducer")) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 0:
                    case 9:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                        // do nothing
                        break;
                    case 17:
                        // reduce
                        event.setCancelled(true);
                        reduce(event.getClickedInventory(), player);
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

    private void reduce(Inventory inventory, Player player) {
        ItemStack is = inventory.getItem(0);
        if (is != null) {
            Material material = is.getType();
            if (material.equals(Material.GLASS_BOTTLE) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName()) {
                    String c = im.getDisplayName().replace(" ", "_");
                    for (Compound compound : Compound.values()) {
                        if (compound.toString().equals(c)) {
                            reduce(compound.getFormula(), inventory, player);
                            return;
                        }
                    }
                }
            } else {
                for (Reduction reduction : Reduction.values()) {
                    if (reduction.getMaterial().equals(material)) {
                        reduce(reduction.getElements(), inventory, player);
                        return;
                    }
                }
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
    }

    private void reduce(String data, Inventory inventory, Player player) {
        String[] elements = data.split("-");
        int i = 18;
        for (String e : elements) {
            String[] split = e.split(":");
            Element element = Element.valueOf(split[0]);
            ItemStack chemical = ElementBuilder.getElement(element);
            chemical.setAmount(Integer.parseInt(split[1]));
            if (i > 25) {
                i = 9;
            }
            // set slot i to the compound
            inventory.setItem(i, chemical);
            i++;
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            // remove the reduced item stack
            inventory.setItem(0, null);
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
