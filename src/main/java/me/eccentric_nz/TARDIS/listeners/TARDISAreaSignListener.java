/*
 * Copyright (C) 2014 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAreaSignListener implements Listener {

    private final TARDIS plugin;

    public TARDISAreaSignListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onAreaTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS areas")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 45) {
                // get the TARDIS the player is in
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (rst.resultSet()) {
                    ItemStack is = inv.getItem(slot);
                    if (is != null) {
                        ItemMeta im = is.getItemMeta();
                        String area = im.getDisplayName();
                        Location l = plugin.getTardisArea().getNextSpot(area);
                        if (l == null) {
                            TARDISMessage.send(player, "NO_MORE_SPOTS");
                            close(player);
                            return;
                        }
                        // check the player is not already in the area!
                        if (plugin.getTardisArea().areaCheckInExisting(l)) {
                            TARDISMessage.send(player, "TRAVEL_NO_AREA");
                            close(player);
                            return;
                        }
                        player.performCommand("tardistravel area " + area);
                        close(player);
                        return;
                    }
                }
            }
            if (slot == 49) {
                // load TARDIS saves
                close(player);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", player.getUniqueId().toString());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (rs.resultSet()) {
                            TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, rs.getTardis_id());
                            ItemStack[] items = sst.getTerminal();
                            Inventory saveinv = plugin.getServer().createInventory(player, 54, "§4TARDIS saves");
                            saveinv.setContents(items);
                            player.openInventory(saveinv);
                        }
                    }
                }, 2L);
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
