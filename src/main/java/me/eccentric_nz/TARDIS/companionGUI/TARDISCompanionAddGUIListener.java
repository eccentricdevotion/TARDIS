/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionAddGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISCompanionAddGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public static void addCompanion(int id, String comps, String puid) {
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
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, tid);
    }

    public static void addToRegion(String world, String owner, String companion) {
        // if using WorldGuard, add them to the region membership
        World w = TARDISAliasResolver.getWorldFromAlias(world);
        if (w != null) {
            Player player = TARDIS.plugin.getServer().getPlayer(companion);
            if (player != null) {
                TARDIS.plugin.getWorldGuardUtils().addMemberToRegion(w, owner, player.getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCompanionAddGUIClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISCompanionAddInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 53) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 45 -> { // info
            }
            case 47 -> list(player); // list
            case 49 -> {
                // add everyone
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rsa = new ResultSetTardis(plugin, wherea, "", false);
                if (rsa.resultSet()) {
                    Tardis tardis = rsa.getTardis();
                    int id = tardis.getTardisId();
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
            }
            case 53 -> close(player); // close
            default -> {
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    int id = tardis.getTardisId();
                    String comps = tardis.getCompanions();
                    ItemMeta m = is.getItemMeta();
                    List<Component> l = m.lore();
                    String u = ComponentUtils.stripColour(l.getFirst());
                    addCompanion(id, comps, u);
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        String[] data = tardis.getChunk().split(":");
                        addToRegion(data[0], tardis.getOwner(), ComponentUtils.stripColour(m.displayName()));
                        // set entry and exit flags to deny
                        plugin.getWorldGuardUtils().setEntryExitFlags(data[0], player.getName(), false);
                    }
                    list(player);
                }
            }
        }
    }

    private void list(Player player) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
            if (rs.fromUUID(player.getUniqueId().toString())) {
                String comps = rs.getCompanions();
                InventoryHolder items;
                if (comps.equalsIgnoreCase("everyone")) {
                    items = new TARDISEveryoneCompanionInventory(plugin, player);
                } else {
                    items = new TARDISCompanionInventory(plugin, comps.split(":"));
                }
                player.openInventory(items.getInventory());
            }
        }, 5L);
    }
}
