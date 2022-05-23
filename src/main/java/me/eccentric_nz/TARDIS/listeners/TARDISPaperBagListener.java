/*
 * Copyright (C) 2022 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPaperBag;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class TARDISPaperBagListener implements Listener {

    private final TARDIS plugin;

    public TARDISPaperBagListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPaperBagInteract(InventoryClickEvent event) {
        Inventory inv = event.getView().getBottomInventory();
        if (inv.getType().equals(InventoryType.PLAYER)) {
            Player player = (Player) event.getWhoClicked();
            if (!TARDISPermission.hasPermission(player, "tardis.paper_bag")) {
                return;
            }
            if (event.getRawSlot() >= 0) {
                ItemStack is = event.getCurrentItem();
                if (is != null && is.getType().equals(Material.PAPER) && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName() && im.getDisplayName().equals("Paper Bag")) {
                        if (event.isRightClick()) {
                            event.setCancelled(true);
                            ResultSetPaperBag rs = new ResultSetPaperBag(plugin, player.getUniqueId());
                            String flavour = rs.getJellyBaby();
                            if (flavour != null) {
                                // create a random flavoured Jelly Baby
                                ItemStack jb = new ItemStack(Material.MELON_SLICE, 1);
                                ItemMeta jim = jb.getItemMeta();
                                jim.setDisplayName(flavour + " Jelly Baby");
                                jb.setItemMeta(jim);
                                int slot = inv.firstEmpty();
                                if (slot != -1) {
                                    // add the jelly bean to the inventory
                                    inv.setItem(slot, jb);
                                    player.updateInventory();
                                } else {
                                    // message player
                                    TARDISMessage.send(player, "PAPER_BAG_SLOT");
                                }
                            } else {
                                // create a new record if one doesn't exist
                                if (!rs.resultSet()) {
                                    HashMap<String, Object> bag = new HashMap<>();
                                    bag.put("uuid", player.getUniqueId().toString());
                                    plugin.getQueryFactory().doSyncInsert("paper_bag", bag);
                                }
                                // message player
                                TARDISMessage.send(player, "PAPER_BAG_EMPTY");
                            }
                        }
                        if (event.isShiftClick()) {
                            event.setCancelled(true);
                            int jelly = inv.first(Material.MELON_SLICE);
                            if (jelly >= 0) {
                                int bagId;
                                String flavour1 = "";
                                String flavour2 = "";
                                String flavour3 = "";
                                String flavour4 = "";
                                int amount1 = 0;
                                int amount2 = 0;
                                int amount3 = 0;
                                int amount4 = 0;
                                // get current paper bag contents
                                ResultSetPaperBag rs = new ResultSetPaperBag(plugin, player.getUniqueId());
                                if (rs.resultSet()) {
                                    bagId = rs.getPaperBagID();
                                    flavour1 = rs.getFlavour1();
                                    flavour2 = rs.getFlavour2();
                                    flavour3 = rs.getFlavour3();
                                    flavour4 = rs.getFlavour4();
                                    amount1 = rs.getAmount1();
                                    amount2 = rs.getAmount2();
                                    amount3 = rs.getAmount3();
                                    amount4 = rs.getAmount4();
                                } else {
                                    // create a new record if one doesn't exist
                                    HashMap<String, Object> bag = new HashMap<>();
                                    bag.put("uuid", player.getUniqueId().toString());
                                    bagId = plugin.getQueryFactory().doSyncInsert("paper_bag", bag);
                                }
                                HashMap<Integer, ItemStack> babies = (HashMap<Integer, ItemStack>) inv.all(Material.MELON_SLICE);
                                HashMap<Integer, Integer> adjustments = new HashMap<>();
                                for (Map.Entry<Integer, ItemStack> entry : babies.entrySet()) {
                                    // is it a jelly baby?
                                    if (entry.getValue().hasItemMeta()) {
                                        ItemMeta jim = entry.getValue().getItemMeta();
                                        if (jim.hasDisplayName()) {
                                            String name = jim.getDisplayName();
                                            if (name.endsWith("Jelly Baby")) {
                                                int amount = entry.getValue().getAmount();
                                                String flavour = name.replace(" Jelly Baby", "");
                                                int adjust;
                                                if (flavour1.isEmpty()) {
                                                    flavour1 = flavour;
                                                    amount1 = amount;
                                                    adjustments.put(entry.getKey(), amount);
                                                } else if (flavour1.equals(flavour) && amount1 < 64) {
                                                    adjust = amount1 + amount;
                                                    adjustments.put(entry.getKey(), (adjust < 65) ? 0 : 64 - amount1);
                                                    amount1 = (adjust < 65) ? adjust : 64;
                                                } else {
                                                    if (flavour2.isEmpty()) {
                                                        flavour2 = flavour;
                                                        amount2 = amount;
                                                        adjustments.put(entry.getKey(), amount);
                                                    } else if (flavour2.equals(flavour) && amount2 < 64) {
                                                        adjust = amount2 + amount;
                                                        adjustments.put(entry.getKey(), (adjust < 65) ? 0 : 64 - amount2);
                                                        amount2 = (adjust < 65) ? adjust : 64;
                                                    } else {
                                                        if (flavour3.isEmpty()) {
                                                            flavour3 = flavour;
                                                            amount3 = amount;
                                                            adjustments.put(entry.getKey(), amount);
                                                        } else if (flavour3.equals(flavour) && amount3 < 64) {
                                                            adjust = amount3 + amount;
                                                            adjustments.put(entry.getKey(), (adjust < 65) ? 0 : 64 - amount3);
                                                            amount3 = (adjust < 65) ? adjust : 64;
                                                        } else {
                                                            if (flavour4.isEmpty()) {
                                                                flavour4 = flavour;
                                                                amount4 = amount;
                                                                adjustments.put(entry.getKey(), amount);
                                                            } else if (flavour4.equals(flavour) && amount4 < 64) {
                                                                adjust = amount4 + amount;
                                                                adjustments.put(entry.getKey(), (adjust < 65) ? 0 : 64 - amount4);
                                                                amount4 = (adjust < 65) ? adjust : 64;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("paper_bag_id", bagId);
                                HashMap<String, Object> set = new HashMap<>();
                                set.put("flavour_1", flavour1);
                                set.put("amount_1", amount1);
                                set.put("flavour_2", flavour2);
                                set.put("amount_2", amount2);
                                set.put("flavour_3", flavour3);
                                set.put("amount_3", amount3);
                                set.put("flavour_4", flavour4);
                                set.put("amount_4", amount4);
                                plugin.getQueryFactory().doSyncUpdate("paper_bag", set, where);
                                // update inventory
                                for (Map.Entry<Integer, Integer> entry : adjustments.entrySet()) {
                                    if (entry.getValue() == 0) {
                                        inv.clear(entry.getKey());
                                    } else {
                                        inv.getItem(entry.getKey()).setAmount(entry.getValue());
                                    }
                                }
                                player.updateInventory();
                            }
                        }
                    }
                }
            }
        }
    }
}
