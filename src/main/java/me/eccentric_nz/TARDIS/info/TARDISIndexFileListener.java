package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISIndexFileListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIndexFileClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Index File")) {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            return;
        }
        event.setCancelled(true);
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        if (slot == 26) {
            close(p);
        } else {
            ItemMeta im = is.getItemMeta();
            String name = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
            try {
                TISCategory category = TISCategory.valueOf(name);
                plugin.getTrackerKeeper().getInfoGUI().put(p.getUniqueId(), category);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ItemStack[] entries = new TARDISIndexFileSection(plugin, category).getMenu();
                    Inventory gui = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "TARDIS Info Category");
                    gui.setContents(entries);
                    p.openInventory(gui);
                }, 2L);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
