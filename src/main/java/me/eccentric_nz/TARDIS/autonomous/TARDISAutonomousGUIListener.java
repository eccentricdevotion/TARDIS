/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.autonomous;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISAutonomousGUIListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final ItemStack on;
    private final ItemStack off;

    public TARDISAutonomousGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        on = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta onMeta = on.getItemMeta();
        onMeta.setDisplayName(ChatColor.GREEN + plugin.getLanguage().getString("SET_ON"));
        on.setItemMeta(onMeta);
        off = new ItemStack(Material.LIGHT_GRAY_CARPET, 1);
        ItemMeta offMeta = off.getItemMeta();
        offMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
        off.setItemMeta(offMeta);
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onChameleonMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Autonomous Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 36) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    HashMap<String, Object> set = new HashMap<>();
                    switch (slot) {
                        case 3 -> {
                            // home
                            set.put("auto_type", "HOME");
                            setTypeSlots(view, 12);
                        }
                        case 4 -> {
                            // areas
                            if (hasAreas()) {
                                set.put("auto_type", "AREAS");
                                setTypeSlots(view, 13);
                            } else {
                                TARDISMessage.send(player, "NO_CONFIGURED_AREAS");
                            }
                        }
                        case 5 -> {
                            // configured areas
                            if (plugin.getConfig().getStringList("autonomous_areas").size() > 0) {
                                set.put("auto_type", "CONFIGURED_AREAS");
                                setTypeSlots(view, 14);
                            } else {
                                TARDISMessage.send(player, "NO_CONFIGURED_AREAS");
                            }
                        }
                        case 6 -> {
                            // closest
                            set.put("auto_type", "CLOSEST");
                            setTypeSlots(view, 15);
                        }
                        case 21 -> {
                            // go home
                            set.put("auto_default", "HOME");
                            setDefaultSlots(view, 30);
                        }
                        case 22 -> {
                            // stay
                            set.put("auto_default", "STAY");
                            setDefaultSlots(view, 31);
                        }
                        case 35 -> close(player);
                        default -> {
                            //ignore
                        }
                    }
                    if (set.size() > 0) {
                        // update player prefs
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", player.getUniqueId().toString());
                        plugin.getQueryFactory().doUpdate("player_prefs", set, where);
                    }
                }
            }
        }
    }

    private boolean hasAreas() {
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        return rsa.resultSet();
    }

    private void setTypeSlots(InventoryView view, int slot) {
        for (int i = 12; i < 16; i++) {
            ItemStack is = (i == slot) ? on : off;
            view.setItem(i, is);
        }
    }

    private void setDefaultSlots(InventoryView view, int slot) {
        view.setItem(30, (slot == 30) ? on : off);
        view.setItem(31, (slot == 31) ? on : off);
    }
}
