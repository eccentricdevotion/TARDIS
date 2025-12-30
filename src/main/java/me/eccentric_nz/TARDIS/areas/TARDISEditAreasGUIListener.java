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
package me.eccentric_nz.TARDIS.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
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
public class TARDISEditAreasGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selected = new HashMap<>();

    public TARDISEditAreasGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    private void removeLocation(List<Component> lore) {
        /*
        [0] = world
        [1] = x
        [2] = y
        [3] = z
        [4] = area_id
         */
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_id", getValueFromLore(lore.get(4)));
        where.put("world", ComponentUtils.stripColour(lore.getFirst()));
        where.put("x", getValueFromLore(lore.get(1)));
        where.put("y", getValueFromLore(lore.get(2)));
        where.put("z", getValueFromLore(lore.get(3)));
        plugin.getQueryFactory().doDelete("area_locations", where);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCompanionGUIClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISEditAreasInventory)) {
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
            case 45 -> { // info
            }
            case 48 -> {
                // add
                ItemMeta meta = is.getItemMeta();
                Object area_id = getValueFromLore(meta.lore().getFirst());
                // get player's location
                Location location = player.getLocation();
                HashMap<String, Object> add = new HashMap<>();
                add.put("area_id", area_id);
                add.put("world", location.getWorld().getName());
                add.put("x", location.getBlockX());
                add.put("y", location.getBlockY());
                add.put("z", location.getBlockZ());
                plugin.getQueryFactory().doInsert("area_locations", add);
                close(player);
            }
            case 50 -> {
                // delete
                if (selected.containsKey(uuid)) {
                    ItemStack map = event.getView().getItem(selected.get(uuid));
                    ItemMeta meta = map.getItemMeta();
                    List<Component> lore = meta.lore();
                    removeLocation(lore);
                    close(player);
                }
            }
            case 53 -> close(player);
            default -> selected.put(uuid, slot);
        }
    }

    private Object getValueFromLore(Component s) {
        String[] split = ComponentUtils.stripColour(s).split(": ");
        return TARDISNumberParsers.parseInt(split[1]);
    }
}
