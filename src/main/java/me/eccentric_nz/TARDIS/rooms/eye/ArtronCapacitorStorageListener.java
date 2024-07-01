package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class ArtronCapacitorStorageListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public ArtronCapacitorStorageListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCapacitorStorageClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "Artron Capacitor Storage")) {
            UUID uuid = event.getPlayer().getUniqueId();
            // get the TARDIS the player is in
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (!rst.resultSet()) {
                // should never happen - gui is only accessible from a room inside the TARDIS
                return;
            }
            int id = rst.getTardis_id();
            // scan the inventory for capacitors
            int capacitors = 0;
            int damaged = 0;
            for (int i = 2; i < 8; i++) {
                ItemStack stack = view.getItem(i);
                if (stack == null || !stack.getType().equals(Material.BUCKET) || !stack.hasItemMeta()) {
                    continue;
                }
                ItemMeta im = stack.getItemMeta();
                if (!im.hasDisplayName() || !im.getDisplayName().equals("Artron Capacitor") || !im.hasCustomModelData()) {
                    continue;
                }
                int cmd = im.getCustomModelData();
                if (cmd != 10000003 && cmd != 10000004) {
                    continue;
                }
                capacitors++;
                if (cmd == 10000004) {
                    damaged++;
                }
            }
            // stop and start the particles
            int task = new EyeOfHarmonyPartcles(plugin).stopStart(id, capacitors, event.getPlayer().getUniqueId());
            // update eyes record
            HashMap<String, Object> set = new HashMap<>();
            set.put("capacitors", capacitors);
            set.put("damaged", damaged);
            if (task != -1) {
                set.put("task", task);
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            plugin.getQueryFactory().doSyncUpdate("eyes", set, where);
            // not a real inventory, so any random items left in there will be vapourised
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCapacitorStorageInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "Artron Capacitor Storage")) {
            int slot = event.getRawSlot();
            if ((slot < 2 || slot > 7) || event.isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }
}
