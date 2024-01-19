/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TARDISAutonomousGUIListener extends TARDISMenuListener {

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
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Autonomous Menu")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 35) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
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
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_CONFIGURED_AREAS");
                }
            }
            case 5 -> {
                // configured areas
                if (!plugin.getConfig().getStringList("autonomous_areas").isEmpty()) {
                    set.put("auto_type", "CONFIGURED_AREAS");
                    setTypeSlots(view, 14);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_CONFIGURED_AREAS");
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
        if (!set.isEmpty()) {
            // update player prefs
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", set, where);
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
