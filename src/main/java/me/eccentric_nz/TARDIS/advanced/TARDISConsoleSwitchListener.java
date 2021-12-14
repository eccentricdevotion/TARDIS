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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.control.TARDISScanner;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A trip stitch circuit-breaker was a circuit that if was enabled in the psycho-kinetic threshold manipulator of the
 * Daleks had the effect of preventing them from controlling their movement.
 *
 * @author eccentric_nz
 */
public class TARDISConsoleSwitchListener implements Listener {

    private final TARDIS plugin;
    private final List<Integer> gui_circuits = Arrays.asList(10001966, 10001973, 10001974, 10001975, 10001976, 10001977, 20001966, 20001973, 20001974, 20001975, 20001976, 20001977);

    public TARDISConsoleSwitchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Console")) {
            Player p = (Player) event.getWhoClicked();
            // check they're in the TARDIS
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", p.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                event.setCancelled(true);
                TARDISMessage.send(p, "NOT_IN_TARDIS");
            }
            if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                if (slot >= 0 && slot < 9) {
                    ItemStack item = view.getItem(slot);
                    if (item != null && item.getType().equals(Material.GLOWSTONE_DUST) && item.hasItemMeta()) {
                        ItemMeta im = item.getItemMeta();
                        int cmd = (im.hasCustomModelData()) ? im.getCustomModelData() : 10001963;
                        if (gui_circuits.contains(cmd)) {
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", p.getUniqueId().toString());
                            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                            if (rs.resultSet()) {
                                Tardis tardis = rs.getTardis();
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    ItemStack[] stack = null;
                                    Inventory new_inv = null;
                                    switch (cmd) { // Chameleon circuit
                                        case 10001966, 20001966 -> {
                                            new_inv = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                                            stack = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                                        } // ARS circuit
                                        case 10001973, 20001973 -> {
                                            new_inv = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
                                            stack = new TARDISARSInventory(plugin, p).getARS();
                                        } // Temporal circuit
                                        case 10001974, 20001974 -> {
                                            new_inv = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "Temporal Locator");
                                            stack = new TARDISTemporalLocatorInventory(plugin).getTemporal();
                                        } // Memory circuit (saves/areas)
                                        case 10001975, 20001975 -> {
                                            new_inv = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "TARDIS saves");
                                            stack = new TARDISSaveSignInventory(plugin, tardis.getTardis_id(), p).getTerminal();
                                        } // Input circuit (terminal)
                                        case 10001976, 20001976 -> {
                                            new_inv = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "Destination Terminal");
                                            stack = new TARDISTerminalInventory(plugin).getTerminal();
                                        }
                                        default -> // scanner circuit
                                            TARDISScanner.scan(p, tardis.getTardis_id(), plugin.getServer().getScheduler());
                                    }
                                    // close inventory
                                    p.closeInventory();
                                    if (new_inv != null && stack != null) {
                                        // open new inventory
                                        new_inv.setContents(stack);
                                        p.openInventory(new_inv);
                                    }
                                }, 1L);
                            } else {
                                TARDISMessage.send(p, "NO_TARDIS");
                            }
                        }
                    }
                }
            }
        }
    }
}
