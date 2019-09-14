package me.eccentric_nz.TARDIS.chemistry.compound;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.element.Element;
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

public class CompoundGUIListener implements Listener {

    private final TARDIS plugin;

    public CompoundGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompoundMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Chemical compounds")) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 0:
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
                        // check formula
                        event.setCancelled(true);
                        checkFormula(event.getClickedInventory(), player);
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

    private void checkFormula(Inventory inventory, Player player) {
        StringBuilder formula = new StringBuilder();
        for (int i = 18; i < 26; i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null && is.getType().equals(Material.FEATHER) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName()) {
                    try {
                        Element element = Element.valueOf(im.getDisplayName());
                        int amount = is.getAmount();
                        formula.append(element.toString()).append(":").append(amount).append("-");
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        }
        String f = (formula.length() > 1) ? formula.toString().substring(0, formula.length() - 1) : "";
        if (!f.isEmpty()) {
            for (Compound compound : Compound.values()) {
                if (compound.getFormula().equals(f)) {
                    ItemStack chemical = CompoundBuilder.getCompound(compound);
                    // set slot 0 to the compound
                    inventory.setItem(0, chemical);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    // remove the other item stacks
                    for (int i = 18; i < 26; i++) {
                        inventory.setItem(i, null);
                    }
                    return;
                }
            }
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
        }
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    private void close(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
    }
}
