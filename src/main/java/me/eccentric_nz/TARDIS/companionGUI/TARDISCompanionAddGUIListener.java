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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisCompanions;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
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
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Add Companion")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    switch (slot) {
                        case 45: // info
                            break;
                        case 47: // list
                            list(player);
                            break;
                        case 49: // add everyone
                            HashMap<String, Object> wherea = new HashMap<>();
                            wherea.put("uuid", player.getUniqueId().toString());
                            ResultSetTardis rsa = new ResultSetTardis(plugin, wherea, "", false, 0);
                            if (rsa.resultSet()) {
                                Tardis tardis = rsa.getTardis();
                                int id = tardis.getTardis_id();
                                String comps = tardis.getCompanions();
                                addCompanion(id, comps, "everyone");
                                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                                    // remove all members
                                    String[] data = tardis.getChunk().split(":");
                                    plugin.getWorldGuardUtils().removeAllMembersFromRegion(TARDISAliasResolver.getWorldFromAlias(data[0]), player.getName(), player.getUniqueId());
                                    // set entry and exit flags to allow
                                    plugin.getWorldGuardUtils().setEntryExitFlags(data[0], player.getName(), true);
                                }
                                list(player);
                            }
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
                                ItemStack h = view.getItem(slot);
                                ItemMeta m = h.getItemMeta();
                                List<String> l = m.getLore();
                                String u = l.get(0);
                                addCompanion(id, comps, u);
                                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                                    String[] data = tardis.getChunk().split(":");
                                    addToRegion(data[0], tardis.getOwner(), m.getDisplayName());
                                    // set entry and exit flags to deny
                                    plugin.getWorldGuardUtils().setEntryExitFlags(data[0], player.getName(), false);
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
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
            if (rs.fromUUID(player.getUniqueId().toString())) {
                String comps = rs.getCompanions();
                ItemStack[] items;
                if (comps.equalsIgnoreCase("everyone")) {
                    items = new TARDISEveryoneCompanionInventory(plugin, player).getSkulls();
                } else {
                    items = new TARDISCompanionInventory(plugin, comps.split(":")).getSkulls();
                }
                Inventory cominv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Companions");
                cominv.setContents(items);
                player.openInventory(cominv);
            }
        }, 5L);
    }

    private void addCompanion(int id, String comps, String puid) {
        HashMap<String, Object> tid = new HashMap<>();
        HashMap<String, Object> set = new HashMap<>();
        tid.put("tardis_id", id);
        if (comps != null && !comps.isEmpty() && !puid.equalsIgnoreCase("everyone")) {
            // add to the list
            String newList = comps + ":" + puid;
            set.put("companions", newList);
        } else {
            // make a list
            set.put("companions", puid);
        }
        plugin.getQueryFactory().doUpdate("tardis", set, tid);
    }

    private void addToRegion(String world, String owner, String companion) {
        // if using WorldGuard, add them to the region membership
        World w = TARDISAliasResolver.getWorldFromAlias(world);
        if (w != null) {
            Player player = plugin.getServer().getPlayer(companion);
            if (player != null) {
                plugin.getWorldGuardUtils().addMemberToRegion(w, owner, player.getUniqueId());
            }
        }
    }
}
