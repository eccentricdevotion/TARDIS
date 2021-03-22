/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonTemplateListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonTemplateListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onChameleonConstructorClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Chameleon Template")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            switch (slot) {
                                case 0:
                                    // back
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonHelpGUI tci = new TARDISChameleonHelpGUI(plugin);
                                        ItemStack[] items = tci.getHelp();
                                        Inventory chamhelp = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Chameleon Help");
                                        chamhelp.setContents(items);
                                        player.openInventory(chamhelp);
                                    }, 2L);
                                    break;
                                case 8:
                                    // construct
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonConstructorGUI tci = new TARDISChameleonConstructorGUI(plugin);
                                        ItemStack[] items = tci.getConstruct();
                                        Inventory chamcon = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Chameleon Construction");
                                        chamcon.setContents(items);
                                        player.openInventory(chamcon);
                                    }, 2L);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
