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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.advanced.TARDISStorageConverter;
import me.eccentric_nz.TARDIS.advanced.TARDISStorageInventory;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class DiskStorageAction {

    private final TARDIS plugin;

    public DiskStorageAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(UUID ownerUUID, Player player, int id, Block block) {
        UUID playerUUID = player.getUniqueId();
        if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(playerUUID)) {
            return;
        }
        // only the time lord of this tardis
        if (!ownerUUID.equals(playerUUID)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_OWNER");
            return;
        }
        // do they have a storage record?
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", playerUUID);
        ResultSetDiskStorage rsStore = new ResultSetDiskStorage(plugin, where);
        ItemStack[] stack = new ItemStack[54];
        if (rsStore.resultSet()) {
            try {
                if (!rsStore.getSavesOne().isEmpty()) {
                    // convert stacks if necessary
                    String[] split = rsStore.getVersions().split(",");
                    if (split[0].equals("0")) {
                        plugin.debug("TARDISStorageConverter.updateDisks");
                        stack = TARDISStorageConverter.updateDisks(rsStore.getSavesOne());
                    } else {
                        stack = TARDISSerializeInventory.itemStacksFromString(rsStore.getSavesOne());
                    }
                } else {
                    stack = TARDISSerializeInventory.itemStacksFromString(Storage.SAVE_1.getEmpty());
                }
            } catch (IOException ex) {
                plugin.debug("Could not get Storage Inventory: " + ex.getMessage());
            }
        } else {
            try {
                stack = TARDISSerializeInventory.itemStacksFromString(Storage.SAVE_1.getEmpty());
            } catch (IOException ex) {
                plugin.debug("Could not get default Storage Inventory: " + ex.getMessage());
            }
            // make a record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", player.getUniqueId().toString());
            set.put("tardis_id", id);
            // a non-empty console record is required for area storage
            set.put("console", "rO0ABXcEAAAAEnBwcHBwcHBwcHBwcHBwcHBwcA==");
            plugin.getQueryFactory().doInsert("storage", set);
        }
        player.openInventory(new TARDISStorageInventory(plugin, Storage.SAVE_1.getTitle(), stack).getInventory());
        // update note block if it's not BARRIER + Item Display
        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
            if (block.getType().equals(Material.NOTE_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
                block.setBlockData(TARDISConstants.BARRIER, true);
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, block, id);
            }
        }
    }
}
