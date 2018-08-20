/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTardisCompanions;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionAddGUIListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISCompanionAddGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCompanionAddGUIClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Add Companion")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    switch (slot) {
                        case 45: // info
                            break;
                        case 47: // list
                            list(player);
                            break;
                        case 53: // close
                            close(player);
                            break;
                        default:
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", player.getUniqueId().toString());
                            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                            if (rs.resultSet()) {
                                Tardis tardis = rs.getTardis();
                                int id = tardis.getTardis_id();
                                String comps = tardis.getCompanions();
                                ItemStack h = inv.getItem(slot);
                                ItemMeta m = h.getItemMeta();
                                List<String> l = m.getLore();
                                String u = l.get(0);
                                addCompanion(id, comps, u);
                                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                                    String[] data = tardis.getChunk().split(":");
                                    addToRegion(data[0], tardis.getOwner(), m.getDisplayName());
                                }
                                list(player);
                            }
                            break;
                    }
                }
            }
        }
    }

    private void list(Player player) {
        ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            String comps = rs.getCompanions();
            close(player);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack[] items = new TARDISCompanionInventory(plugin, comps.split(":")).getSkulls();
                Inventory cominv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Companions");
                cominv.setContents(items);
                player.openInventory(cominv);
            }, 2L);
        }
    }

    private void addCompanion(int id, String comps, String puid) {
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> tid = new HashMap<>();
        HashMap<String, Object> set = new HashMap<>();
        tid.put("tardis_id", id);
        if (comps != null && !comps.isEmpty()) {
            // add to the list
            String newList = comps + ":" + puid;
            set.put("companions", newList);
        } else {
            // make a list
            set.put("companions", puid);
        }
        qf.doUpdate("tardis", set, tid);
    }

    private void addToRegion(String world, String owner, String player) {
        // if using WorldGuard, add them to the region membership
        World w = plugin.getServer().getWorld(world);
        if (w != null) {
            plugin.getWorldGuardUtils().addMemberToRegion(w, owner, player);
        }
    }
}
