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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSonicLocation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISSonicData {

    private final List<String> upgrades = List.of("bio", "diamond", "emerald", "redstone", "painter", "ignite", "arrow", "knockback", "brush", "conversion");

    public static List<Integer> getSonicData(List<Component> lore) {
        return List.of(
                lore.contains(Component.text("Bio-scanner Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Diamond Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Emerald Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Redstone Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Painter Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Ignite Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Pickup Arrows Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Knockback Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Brush Upgrade")) ? 1 : 0,
                lore.contains(Component.text("Conversion Upgrade")) ? 1 : 0
        );
    }

    public void saveOrUpdate(TARDIS plugin, String scanned, int type, ItemStack is, Player player) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("last_scanned", scanned);
        set.put("scan_type", type);
        UUID sonic_uuid;
        ItemMeta im = is.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        if (pdc.has(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID())) {
            sonic_uuid = pdc.getOrDefault(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID(), TARDISConstants.UUID_ZERO);
            // is there a sonic record?
            ResultSetSonicLocation rssl = new ResultSetSonicLocation(plugin, sonic_uuid);
            if (rssl.resultset()) {
                // update
                HashMap<String, Object> where = new HashMap<>();
                where.put("sonic_uuid", sonic_uuid.toString());
                plugin.getQueryFactory().doUpdate("sonic", set, where);
            } else {
                set.put("sonic_uuid", sonic_uuid.toString());
                // get sonic data
                set.put("uuid", player.getUniqueId().toString());
                List<Integer> settings = getSonicData(im.lore());
                for (int i = 0; i < settings.size(); i++) {
                    set.put(upgrades.get(i), settings.get(i));
                }
                // insert
                plugin.getQueryFactory().doInsert("sonic", set);
            }
        } else {
            sonic_uuid = UUID.randomUUID();
            pdc.set(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID(), sonic_uuid);
            is.setItemMeta(im);
            set.put("sonic_uuid", sonic_uuid.toString());
            // get sonic data
            set.put("uuid", player.getUniqueId().toString());
            List<Integer> settings = getSonicData(im.lore());
            for (int i = 0; i < settings.size(); i++) {
                set.put(upgrades.get(i), settings.get(i));
            }
            // insert
            plugin.getQueryFactory().doInsert("sonic", set);
        }
    }
}
