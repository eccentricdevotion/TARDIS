/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TVMSavesGUIListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    int selectedSlot = -1;

    public TVMSavesGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "VM Saves")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                if (view.getItem(slot) != null) {
                    switch (slot) {
                        case 45 -> {} // page number
                        case 46 -> close(player); // close
                        case 48 -> doPrev(view, player); // previous page
                        case 49 -> doNext(view, player); // next page
                        case 51 -> delete(view, player); // delete save
                        case 53 -> doWarp(view, player); // warp
                        default -> selectedSlot = slot;
                    }
                }
            }
        }
    }

    private void doPrev(InventoryView view, Player player) {
        int page = getPageNumber(view);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(player);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TVMSavesGUI tvms = new TVMSavesGUI(plugin, start, start + 44, player.getUniqueId().toString());
                ItemStack[] gui = tvms.getGUI();
                Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "VM Saves");
                vmg.setContents(gui);
                player.openInventory(vmg);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player player) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI tvms = new TVMSavesGUI(plugin, start, start + 44, player.getUniqueId().toString());
            ItemStack[] gui = tvms.getGUI();
            Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "VM Saves");
            vmg.setContents(gui);
            player.openInventory(vmg);
        }, 2L);
    }

    private void delete(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            String save_name = im.getDisplayName();
            TVMResultSetWarpByName rss = new TVMResultSetWarpByName(plugin, player.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(player);
                HashMap<String, Object> where = new HashMap<>();
                where.put("save_id", rss.getId());
                plugin.getQueryFactory().doDelete("saves", where);
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_DELETED");
            }
        } else {
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SELECT");
        }
    }

    private void doWarp(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            String save_name = im.getDisplayName();
            TVMResultSetWarpByName rss = new TVMResultSetWarpByName(plugin, player.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(player);
                List<Player> players = new ArrayList<>();
                players.add(player);
                if (plugin.getConfig().getBoolean("allow.multiple")) {
                    player.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((e) -> {
                        if (e instanceof Player && !e.getUniqueId().equals(player.getUniqueId())) {
                            players.add((Player) e);
                        }
                    });
                }
                int required = plugin.getConfig().getInt("tachyon_use.travel.saved") * players.size();
                if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
                    close(player);
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_REQUIRED", required);
                    return;
                }
                Location l = rss.getWarp();
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_STANDY");
                while (!l.getChunk().isLoaded()) {
                    l.getChunk().load();
                }
                TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
                // remove tachyons
                new TVMQueryFactory(plugin).alterTachyons(player.getUniqueId().toString(), -required);
            }
        } else {
            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SELECT");
        }
    }
}
