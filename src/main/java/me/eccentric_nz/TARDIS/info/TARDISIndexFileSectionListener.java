package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileSectionListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISIndexFileSectionListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIndexFileSectionClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Info Category")) {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 54) {
            return;
        }
        event.setCancelled(true);
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        if (slot == 53) {
            close(p);
        } else {
            ItemMeta im = is.getItemMeta();
            String name = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
            try {
                TARDISInfoMenu tim = TARDISInfoMenu.valueOf(name);
                TISCategory category = plugin.getTrackerKeeper().getInfoGUI().get(p.getUniqueId());
                if (category == TISCategory.ROOMS) {
                    new TISRoomInfo(plugin).show(p, tim);
                    close(p);
                } else if ((category.isFirstLevel() && !hasRecipe(tim)) || (category == TISCategory.MONSTERS && tim != TARDISInfoMenu.K9)) {
                    new TISInfo(plugin).show(p, tim);
                    close(p);
                } else {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        ItemStack[] entries = new TARDISIndexFileEntry(plugin, tim).getMenu();
                        Inventory gui = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Info Entry");
                        gui.setContents(entries);
                        p.openInventory(gui);
                    }, 2L);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private boolean hasRecipe(TARDISInfoMenu tim) {
        switch (tim) {
            case EXTERIOR_LAMP_LEVEL_SWITCH, INTERIOR_LIGHT_LEVEL_SWITCH, TARDIS_MONITOR, MONITOR_FRAME -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
