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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.ars.TardisArsInventory;
import me.eccentric_nz.tardis.chameleon.TardisChameleonInventory;
import me.eccentric_nz.tardis.control.TardisScanner;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisSaveSignInventory;
import me.eccentric_nz.tardis.travel.TardisTemporalLocatorInventory;
import me.eccentric_nz.tardis.travel.TardisTerminalInventory;
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
public class TardisConsoleSwitchListener implements Listener {

    private final TardisPlugin plugin;
    private final List<Integer> guiCircuits = Arrays.asList(10001966, 10001973, 10001974, 10001975, 10001976, 10001977, 20001966, 20001973, 20001974, 20001975, 20001976, 20001977);

    public TardisConsoleSwitchListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInventoryClick(InventoryClickEvent event) {
        InventoryView inventoryView = event.getView();
        if (inventoryView.getTitle().equals(ChatColor.DARK_RED + "TARDIS Console")) {
            Player player = (Player) event.getWhoClicked();
            // check they're in the tardis
            HashMap<String, Object> whereTardis = new HashMap<>();
            whereTardis.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers resultSetTravellers = new ResultSetTravellers(plugin, whereTardis, false);
            if (!resultSetTravellers.resultSet()) {
                event.setCancelled(true);
                TardisMessage.send(player, "NOT_IN_TARDIS");
            }
            if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                if (slot >= 0 && slot < 9) {
                    ItemStack item = inventoryView.getItem(slot);
                    if (item != null && item.getType().equals(Material.GLOWSTONE_DUST) && item.hasItemMeta()) {
                        ItemMeta itemMeta = item.getItemMeta();
                        assert itemMeta != null;
                        int customModelData = (itemMeta.hasCustomModelData()) ? itemMeta.getCustomModelData() : 10001963;
                        if (guiCircuits.contains(customModelData)) {
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", player.getUniqueId().toString());
                            ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, where, "", false, 0);
                            if (resultSetTardis.resultSet()) {
                                Tardis tardis = resultSetTardis.getTardis();
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    ItemStack[] itemStacks = null;
                                    Inventory newInventory = null;
                                    switch (customModelData) { // Chameleon circuit
                                        case 10001966, 20001966 -> {
                                            newInventory = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                                            itemStacks = new TardisChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                                        } // ars circuit
                                        case 10001973, 20001973 -> {
                                            newInventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
                                            itemStacks = new TardisArsInventory(plugin).getArs();
                                        } // Temporal circuit
                                        case 10001974, 20001974 -> {
                                            newInventory = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Temporal Locator");
                                            itemStacks = new TardisTemporalLocatorInventory(plugin).getTemporal();
                                        } // Memory circuit (saves/areas)
                                        case 10001975, 20001975 -> {
                                            newInventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "tardis saves");
                                            itemStacks = new TardisSaveSignInventory(plugin, tardis.getTardisId(), player).getTerminal();
                                        } // Input circuit (terminal)
                                        case 10001976, 20001976 -> {
                                            newInventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Destination Terminal");
                                            itemStacks = new TardisTerminalInventory(plugin).getTerminal();
                                        }
                                        default -> // scanner circuit
                                                TardisScanner.scan(player, tardis.getTardisId(), plugin.getServer().getScheduler());
                                    }
                                    // close inventory
                                    player.closeInventory();
                                    if (newInventory != null && itemStacks != null) {
                                        // open new inventory
                                        newInventory.setContents(itemStacks);
                                        player.openInventory(newInventory);
                                    }
                                }, 1L);
                            } else {
                                TardisMessage.send(player, "NO_TARDIS");
                            }
                        }
                    }
                }
            }
        }
    }
}
