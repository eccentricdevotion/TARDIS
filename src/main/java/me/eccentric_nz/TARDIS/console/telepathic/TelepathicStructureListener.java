package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TelepathicStructureListener extends TARDISMenuListener {

    public TelepathicStructureListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBiomeMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (!name.equals(ChatColor.DARK_RED + "Telepathic Structure Finder")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        if (slot == 53) {
            close(player);
        } else {
            ItemStack choice = view.getItem(slot);
            if (choice != null) {
                // get the structure
                ItemMeta im = choice.getItemMeta();
                String enumStr = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
                player.performCommand("tardistravel structure " + enumStr);
                close(player);
            }
        }
    }
}
