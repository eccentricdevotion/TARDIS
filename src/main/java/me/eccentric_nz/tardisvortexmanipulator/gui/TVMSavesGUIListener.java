/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMSavesGUIListener extends TVMGUICommon implements Listener {

    private final TARDISVortexManipulator plugin;
    int selectedSlot = -1;

    public TVMSavesGUIListener(TARDISVortexManipulator plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals("§4VM Saves")) {
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

    private void doPrev(InventoryView view, Player p) {
        int page = getPageNumber(view);
        if (page > 1) {
            int start = (page * 44) - 44;
            close(p);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TVMSavesGUI tvms = new TVMSavesGUI(plugin, start, start + 44, p.getUniqueId().toString());
                ItemStack[] gui = tvms.getGUI();
                Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
                vmg.setContents(gui);
                p.openInventory(vmg);
            }, 2L);
        }
    }

    private void doNext(InventoryView view, Player p) {
        int page = getPageNumber(view);
        int start = (page * 44) + 44;
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI tvms = new TVMSavesGUI(plugin, start, start + 44, p.getUniqueId().toString());
            ItemStack[] gui = tvms.getGUI();
            Inventory vmg = plugin.getServer().createInventory(p, 54, "§4VM Saves");
            vmg.setContents(gui);
            p.openInventory(vmg);
        }, 2L);
    }

    private void delete(InventoryView view, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            String save_name = im.getDisplayName();
            TVMResultSetWarpByName rss = new TVMResultSetWarpByName(plugin, p.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(p);
                HashMap<String, Object> where = new HashMap<>();
                where.put("save_id", rss.getId());
                new TVMQueryFactory(plugin).doDelete("saves", where);
                p.sendMessage(plugin.getPluginName() + "Save deleted.");
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a save!");
        }
    }

    private void doWarp(InventoryView view, Player p) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            String save_name = im.getDisplayName();
            TVMResultSetWarpByName rss = new TVMResultSetWarpByName(plugin, p.getUniqueId().toString(), save_name);
            if (rss.resultSet()) {
                close(p);
                List<Player> players = new ArrayList<>();
                players.add(p);
                if (plugin.getConfig().getBoolean("allow.multiple")) {
                    p.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((e) -> {
                        if (e instanceof Player && !e.getUniqueId().equals(p.getUniqueId())) {
                            players.add((Player) e);
                        }
                    });
                }
                int required = plugin.getConfig().getInt("tachyon_use.travel.saved") * players.size();
                if (!TVMUtils.checkTachyonLevel(p.getUniqueId().toString(), required)) {
                    close(p);
                    p.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
                    return;
                }
                Location l = rss.getWarp();
                p.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                while (!l.getChunk().isLoaded()) {
                    l.getChunk().load();
                }
                TVMUtils.movePlayers(players, l, p.getLocation().getWorld());
                // remove tachyons
                new TVMQueryFactory(plugin).alterTachyons(p.getUniqueId().toString(), -required);
            }
        } else {
            p.sendMessage(plugin.getPluginName() + "Select a save!");
        }
    }
}
