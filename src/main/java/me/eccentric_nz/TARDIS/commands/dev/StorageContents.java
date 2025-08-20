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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;

public class StorageContents {

    private final TARDIS plugin;

    public StorageContents(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(Player player) {
        // get the storage record
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (!rs.resultSet()) {
            return;
        }
        try {
            for (ItemStack stack : TARDISSerializeInventory.itemStacksFromString(rs.getSavesOne())) {
                if (stack != null) {
                    player.getInventory().addItem(stack);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
