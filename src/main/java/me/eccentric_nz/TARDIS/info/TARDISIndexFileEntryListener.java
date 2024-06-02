package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileEntryListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISIndexFileEntryListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIndexFileEntryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Info Entry")) {
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
        if (slot == 26) {
            close(p);
        } else if (slot > 8) {
            // get item from slot 0
            ItemStack zero = view.getItem(0);
            ItemMeta zim = zero.getItemMeta();
            ItemMeta im = is.getItemMeta();
            String name = TARDISStringUtils.toEnumUppercase(zim.getDisplayName()) + "_" + TARDISStringUtils.toEnumUppercase(im.getDisplayName());
            try {
                TARDISInfoMenu tim = TARDISInfoMenu.valueOf(name);
                if (im.getDisplayName().equals("Recipe")) {
                    new TISRecipe(plugin).show(p, tim);
                } else {
                    new TISInfo(plugin).show(p, tim);
                    close(p);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
