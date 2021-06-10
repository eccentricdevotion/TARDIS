/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.listeners.TARDISMenuListener;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
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

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISAreaSignListener extends TARDISMenuListener implements Listener {

    private final TARDISPlugin plugin;

    public TARDISAreaSignListener(TARDISPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a tardis GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onAreaTerminalClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "tardis areas")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 45) {
                // get the tardis the player is in
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (rst.resultSet()) {
                    ItemStack is = view.getItem(slot);
                    if (is != null) {
                        ItemMeta im = is.getItemMeta();
                        assert im != null;
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
                // load tardis saves
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // get the tardis the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rs = new ResultSetTravellers(plugin, wheres, false);
                    if (rs.resultSet()) {
                        TARDISSaveSignInventory sst = new TARDISSaveSignInventory(plugin, rs.getTardisId(), player);
                        ItemStack[] items = sst.getTerminal();
                        Inventory saveinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "tardis saves");
                        saveinv.setContents(items);
                        player.openInventory(saveinv);
                    }
                }, 2L);
            }
        }
    }
}
