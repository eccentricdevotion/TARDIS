/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.BoneDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ClassicDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISInnerDoorCloser {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public TARDISInnerDoorCloser(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void closeDoor(boolean outside) {
        // get inner door location
        ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
        if (rs.resultSet()) {
            if (!rs.getInnerBlock().getChunk().isLoaded()) {
                rs.getInnerBlock().getChunk().load();
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> close(rs.getInnerBlock(), outside), 5L);
        }
    }

    /**
     * Close the door.
     *
     * @param block the bottom door block
     */
    private void close(Block block, boolean outside) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
            Openable closeable = (Openable) block.getBlockData();
            closeable.setOpen(false);
            block.setBlockData(closeable, true);
        } else {
            // get and close display door
            ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
            if (display != null) {
                TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                if (tdi != null) {
                    ItemStack itemStack = display.getItemStack();
                    ItemMeta im = itemStack.getItemMeta();
                    if ((tdi.toString().endsWith("OPEN") || tdi == TARDISDisplayItem.CUSTOM_DOOR) && outside) {
                        switch (tdi.getMaterial()) {
                            case IRON_DOOR -> im.setItemModel(TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey());
                            case BIRCH_DOOR -> im.setItemModel(BoneDoorVariant.BONE_DOOR_CLOSED.getKey());
                            case CHERRY_DOOR -> im.setItemModel(ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey());
                            default -> im.setItemModel(Door.getClosedModel(tdi.getMaterial()));
                        }
                    }
                    itemStack.setItemMeta(im);
                    display.setItemStack(itemStack);
                }
            }
        }
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            // get all companion UUIDs
            List<UUID> uuids = new ArrayList<>();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    if (rs.getTardis().getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            uuids.add(p.getUniqueId());
                        }
                    } else {
                        String[] companions = rs.getTardis().getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                uuids.add(UUID.fromString(c));
                            }
                        }
                        uuids.add(uuid);
                    }
                }
            }
            // get locations
            // interior portal
            Location inportal = block.getLocation();
            // exterior portal (from current location)
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            rsc.resultSet();
            Location exportal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // unset trackers
            if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                // players
                uuids.forEach((u) -> plugin.getTrackerKeeper().getMovers().remove(u));
            }
            // locations
            plugin.getTrackerKeeper().getPortals().remove(exportal);
            plugin.getTrackerKeeper().getPortals().remove(inportal);
        }
    }
}
