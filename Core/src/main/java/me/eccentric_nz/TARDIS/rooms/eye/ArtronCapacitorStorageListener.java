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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class ArtronCapacitorStorageListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public ArtronCapacitorStorageListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCapacitorStorageClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "Artron Capacitor Storage")) {
            UUID uuid = event.getPlayer().getUniqueId();
            // get the TARDIS the player is in
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (!rst.resultSet()) {
                // should never happen - gui is only accessible from a room inside the TARDIS
                return;
            }
            int id = rst.getTardis_id();
            // scan the inventory for capacitors
            int capacitors = 0;
            int damaged = 0;
            for (int i = 2; i < 7; i++) {
                ItemStack stack = view.getItem(i);
                if (stack == null || !stack.getType().equals(Material.BUCKET) || !stack.hasItemMeta()) {
                    continue;
                }
                ItemMeta im = stack.getItemMeta();
                if (!im.hasDisplayName() || !im.getDisplayName().endsWith("Artron Capacitor")) {
                    continue;
                }
                if (!im.hasItemModel()) {
                    // check name
                    if (im.getDisplayName().startsWith("Damaged")) {
                        damaged++;
                    }
                } else {
                    NamespacedKey model = im.getItemModel();
                    if (!Whoniverse.ARTRON_CAPACITOR.getKey().equals(model) && !Whoniverse.ARTRON_CAPACITOR_DAMAGED.getKey().equals(model)) {
                        continue;
                    }
                    if (Whoniverse.ARTRON_CAPACITOR_DAMAGED.getKey().equals(model)) {
                        damaged++;
                    }
                }
                capacitors++;
            }
            // always stop the particles
            EyeOfHarmonyParticles.stop(plugin, id);
            HashMap<String, Object> set = new HashMap<>();
            // start the particles if configured
            if (plugin.getConfig().getBoolean("eye_of_harmony.particles")) {
                int task = new EyeOfHarmonyParticles(plugin).start(id, capacitors, event.getPlayer().getUniqueId());
                set.put("task", task);
            }
            plugin.debug("capacitors = " + capacitors + ", damaged = " + damaged);
            // update eyes record
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            set.put("capacitors", capacitors);
            set.put("damaged", damaged);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getQueryFactory().doSyncUpdate("eyes", set, where), 2L);
            // not a real inventory, so any random items left in there will be vapourised
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCapacitorStorageInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(ChatColor.DARK_RED + "Artron Capacitor Storage")) {
            int slot = event.getRawSlot();
            if ((slot < 2 || slot == 7 || slot == 8) || event.isShiftClick()) {
                event.setCancelled(true);
                if (slot == 8) {
                    // close
                    close((Player) event.getWhoClicked());
                }
            }
        }
    }
}
