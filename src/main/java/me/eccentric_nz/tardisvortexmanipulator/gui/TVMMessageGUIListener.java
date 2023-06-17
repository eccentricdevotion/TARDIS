/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetMessageById;
import net.md_5.bungee.api.ChatColor;
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
public class TVMMessageGUIListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    int selectedSlot = -1;

    public TVMMessageGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMessageGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "VM Messages")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 45 -> {}
                    case 46 -> close(player); // close
                    case 48 -> doPrev(view, player); // previous page
                    case 49 -> doNext(view, player); // next page
                    case 51 -> doRead(view, player); // read
                    case 53 -> doDelete(view, player); // delete
                    default -> selectedSlot = slot; // select a message
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
                TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, player.getUniqueId().toString());
                ItemStack[] gui = tvmm.getGUI();
                Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "VM Messages");
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
            TVMMessageGUI tvmm = new TVMMessageGUI(plugin, start, start + 44, player.getUniqueId().toString());
            ItemStack[] gui = tvmm.getGUI();
            Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "VM Messages");
            vmg.setContents(gui);
            player.openInventory(vmg);
        }, 2L);
    }

    private void doRead(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDISNumberParsers.parseInt(lore.get(2));
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, message_id);
            if (rsm.resultSet()) {
                close(player);
                TVMUtils.readMessage(player, rsm.getMessage());
                // update read status
                new TVMQueryFactory(plugin).setReadStatus(message_id);
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SELECT_MSG");
        }
    }

    private void doDelete(InventoryView view, Player player) {
        if (selectedSlot != -1) {
            ItemStack is = view.getItem(selectedSlot);
            ItemMeta im = is.getItemMeta();
            List<String> lore = im.getLore();
            int message_id = TARDISNumberParsers.parseInt(lore.get(2));
            TVMResultSetMessageById rsm = new TVMResultSetMessageById(plugin, message_id);
            if (rsm.resultSet()) {
                close(player);
                HashMap<String, Object> where = new HashMap<>();
                where.put("message_id", message_id);
                plugin.getQueryFactory().doDelete("messages", where);
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_DELETED");
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SELECT_MSG");
        }
    }
}
