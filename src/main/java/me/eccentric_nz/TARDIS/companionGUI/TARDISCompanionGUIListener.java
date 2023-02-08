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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
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
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionGUIListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selected_head = new HashMap<>();

    public TARDISCompanionGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public static void removeCompanion(int id, String comps, String uuid, Player player) {
        if (comps.equalsIgnoreCase("everyone")) {
            TARDISMessage.send(player, "COMPANIONS_ALL");
        } else {
            HashMap<String, Object> tid = new HashMap<>();
            HashMap<String, Object> set = new HashMap<>();
            String newList = "";
            String[] split = comps.split(":");
            StringBuilder buf = new StringBuilder();
            if (split.length > 1) {
                // recompile string without the specified player
                for (String c : split) {
                    if (!c.equals(uuid)) {
                        // add to new string
                        buf.append(c).append(":");
                    }
                }
                // remove trailing colon
                if (buf.length() > 0) {
                    newList = buf.substring(0, buf.length() - 1);
                }
                set.put("companions", newList);
            } else {
                set.put("companions", "");
            }
            tid.put("tardis_id", id);
            TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, tid);
        }
    }

    public static void removeFromRegion(String world, String owner, UUID uuid) {
        World w = TARDISAliasResolver.getWorldFromAlias(world);
        if (w != null) {
            TARDIS.plugin.getWorldGuardUtils().removeMemberFromRegion(w, owner, uuid);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCompanionGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Companions")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            if (slot >= 0 && slot < 54) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    switch (slot) {
                        case 45: // info
                            break;
                        case 48: // add
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                ItemStack[] items = new TARDISCompanionAddInventory(plugin, player).getPlayers();
                                Inventory presetinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Add Companion");
                                presetinv.setContents(items);
                                player.openInventory(presetinv);
                            }, 2L);
                            break;
                        case 51: // delete
                            if (selected_head.containsKey(uuid)) {
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("uuid", uuid.toString());
                                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                                if (rs.resultSet()) {
                                    Tardis tardis = rs.getTardis();
                                    int id = tardis.getTardis_id();
                                    String comps = tardis.getCompanions();
                                    ItemStack h = view.getItem(selected_head.get(uuid));
                                    ItemMeta m = h.getItemMeta();
                                    List<String> l = m.getLore();
                                    String u = l.get(0);
                                    removeCompanion(id, comps, u, player);
                                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                                        if (!comps.equalsIgnoreCase("everyone")) {
                                            String[] data = tardis.getChunk().split(":");
                                            removeFromRegion(data[0], tardis.getOwner(), UUID.fromString(u));
                                        }
                                    }
                                    close(player);
                                }
                            }
                            break;
                        case 53: // close
                            close(player);
                            break;
                        default:
                            selected_head.put(uuid, slot);
                            break;
                    }
                }
            }
        }
    }
}
