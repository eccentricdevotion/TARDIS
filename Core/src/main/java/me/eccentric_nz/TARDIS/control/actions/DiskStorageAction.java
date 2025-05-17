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
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.GlowstoneCircuit;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        HashMap<String, Object> wherestore = new HashMap<>();
        wherestore.put("uuid", playerUUID);
        ResultSetDiskStorage rsstore = new ResultSetDiskStorage(plugin, wherestore);
        ItemStack[] stack = new ItemStack[54];
        if (rsstore.resultSet()) {
            try {
                if (!rsstore.getSavesOne().isEmpty()) {
                    stack = TARDISSerializeInventory.itemStacksFromString(rsstore.getSavesOne());
                } else {
                    stack = TARDISSerializeInventory.itemStacksFromString(Storage.SAVE_1.getEmpty());
                }
            } catch (IOException ex) {
                plugin.debug("Could not get Storage Inventory: " + ex.getMessage());
            }
        } else {
            try {
                stack = TARDISSerializeInventory.itemStacksFromString(Storage.SAVE_1.getEmpty());
                for (ItemStack is : stack) {
                    if (is != null && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            if (is.getType().equals(Material.FILLED_MAP)) {
                                GlowstoneCircuit glowstone = GlowstoneCircuit.getByName().get(im.getDisplayName());
                                if (glowstone != null) {
                                    is.setType(Material.GLOWSTONE_DUST);
                                    is.setItemMeta(im);
                                }
                            } else {
                                if (is.getType().equals(Material.LIME_WOOL)) { // next
                                    is.setType(Material.BOWL);
                                    is.setItemMeta(im);
                                } else if (is.getType().equals(Material.RED_WOOL)) { // prev
                                    is.setType(Material.BOWL);
                                    is.setItemMeta(im);
                                }
                                is.setItemMeta(im);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                plugin.debug("Could not get default Storage Inventory: " + ex.getMessage());
            }
            // make a record
            HashMap<String, Object> setstore = new HashMap<>();
            setstore.put("uuid", player.getUniqueId().toString());
            setstore.put("tardis_id", id);
            plugin.getQueryFactory().doInsert("storage", setstore);
        }
        Inventory inv = plugin.getServer().createInventory(player, 54, Storage.SAVE_1.getTitle());
        inv.setContents(stack);
        player.openInventory(inv);
        // update note block if it's not BARRIER + Item Display
        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
            if (block.getType().equals(Material.NOTE_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
                block.setBlockData(TARDISConstants.BARRIER, true);
                TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, block, id);
            }
        }
    }
}
