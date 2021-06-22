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
package me.eccentric_nz.tardis.blueprints;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BlueprintProcessor {

    public static void addPermission(TardisPlugin plugin, ItemStack is, Player player) {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            PersistentDataContainer pdc = im.getPersistentDataContainer();
            if (pdc.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID())) {
                // check disk UUID is same as player UUID
                UUID diskUuid = pdc.get(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID());
                assert diskUuid != null;
                if (!diskUuid.equals(player.getUniqueId())) {
                    return;
                }
                if (pdc.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
                    // get permission
                    String perm = pdc.get(plugin.getBlueprintKey(), PersistentDataType.STRING);
                    // insert database record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", diskUuid.toString());
                    set.put("permission", perm);
                    plugin.getQueryFactory().doInsert("blueprint", set);
                    TardisMessage.send(player, "BLUEPRINT", Objects.requireNonNull(im.getLore()).get(0));
                }
            }
        }
    }
}
