/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selected_head = new HashMap<>();

    public TARDISCompanionGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public static void removeCompanion(int id, String comps, String uuid, Player player) {
        if (comps.equalsIgnoreCase("everyone")) {
            TARDIS.plugin.getMessenger().sendColouredCommand(player, "COMPANIONS_ALL", "/tardis remove all", TARDIS.plugin);
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
                if (!buf.isEmpty()) {
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
        if (!(event.getInventory().getHolder(false) instanceof TARDISCompanionInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (slot < 0 || slot > 53) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            // info
            case 45 -> { }
            // add
            case 48 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new TARDISCompanionAddInventory(plugin, player).getInventory()), 2L);
            // delete
            case 51 -> {
                if (selected_head.containsKey(uuid)) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", uuid.toString());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        int id = tardis.getTardisId();
                        String comps = tardis.getCompanions();
                        ItemStack h = event.getView().getItem(selected_head.get(uuid));
                        ItemMeta m = h.getItemMeta();
                        List<Component> l = m.lore();
                        String u = ComponentUtils.stripColour(l.getFirst());
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
            }
            case 53 -> close(player); // close
            default -> selected_head.put(uuid, slot);
        }
    }
}
