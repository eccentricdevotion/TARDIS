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
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPortals;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
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
public class TARDISInnerDoorOpener {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public TARDISInnerDoorOpener(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void openDoor(boolean outside) {
        // get inner door location
        ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
        if (rs.resultSet()) {
            open(rs.getInnerBlock(), outside);
        }
    }

    /**
     * Open the door.
     */
    private void open(Block block, boolean outside) {
        if (Tag.DOORS.isTagged(block.getType())) {
            Openable openable = (Openable) block.getBlockData();
            openable.setOpen(true);
            block.setBlockData(openable, true);
        } else {
            // get and open display door
            ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
            if (display != null) {
                TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                if (tdi != null) {
                    ItemStack itemStack = display.getItemStack();
                    ItemMeta im = itemStack.getItemMeta();
                    if (outside) {
                        switch (itemStack.getType()) {
                            case IRON_DOOR -> im.setItemModel(TardisDoorVariant.TARDIS_DOOR_OPEN.getKey());
                            case BIRCH_DOOR -> im.setItemModel(BoneDoorVariant.BONE_DOOR_OPEN.getKey());
                            case CHERRY_DOOR -> im.setItemModel(ClassicDoorVariant.CLASSIC_DOOR_OPEN.getKey());
                            default -> im.setItemModel(Door.getOpenModel(itemStack.getType()));
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
            uuids.add(uuid);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            Tardis tardis = null;
            ChameleonPreset preset = null;
            boolean abandoned = false;
            if (rs.resultSet()) {
                tardis = rs.getTardis();
                preset = tardis.getPreset();
                abandoned = tardis.isAbandoned();
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            uuids.add(p.getUniqueId());
                        }
                    } else {
                        String[] companions = tardis.getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                uuids.add(UUID.fromString(c));
                            }
                        }
                    }
                }
            }
            // get locations
            // exterior portal (from current location)
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            rsc.resultSet();
            Location exportal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // interior teleport location
            Location indoor = null;
            COMPASS indirection = COMPASS.SOUTH;
            // exterior teleport location
            Location exdoor = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            COMPASS exdirection = COMPASS.SOUTH;
            // interior portal
            Location inportal = null;
            ResultSetPortals rsp = new ResultSetPortals(plugin, id);
            rsp.resultSet();
            for (HashMap<String, String> map : rsp.getData()) {
                COMPASS tmp_direction = COMPASS.valueOf(map.get("door_direction"));
                if (map.get("door_type").equals("1")) {
                    indoor = TARDISStaticLocationGetters.getLocationFromDB(map.get("door_location"));
                    inportal = TARDISStaticLocationGetters.getLocationFromDB(map.get("door_location"));
                    // clone it because we're going to change it!
                    indirection = tmp_direction;
                    // adjust for teleport
                    switch (indirection) {
                        case NORTH -> indoor.add(0.5d, 0.0d, -0.5d); // z -ve
                        case EAST -> indoor.add(1.5d, 0.0d, 0.5d); // x +ve
                        case SOUTH -> indoor.add(0.5d, 0.0d, 1.5d); // z +ve
                        // WEST
                        default -> indoor.add(-0.5d, 0.0d, 0.5d); // x -ve
                    }
                } else {
                    // outer door
                    exdirection = COMPASS.valueOf(map.get("door_direction"));
                    // adjust for teleport
                    switch (rsc.getDirection()) {
                        case NORTH -> exdoor.add(0.5d, 0.0d, 1.75d);
                        case WEST -> exdoor.add(1.75d, 0.0d, 0.5d);
                        case SOUTH -> exdoor.add(0.5d, 0.0d, -1.75d);
                        default -> exdoor.add(-1.75d, 0.0d, 0.5d); // EAST
                    }
                }
            }
            if (plugin.getPresetBuilder().hasBlockBehind(block, indirection)) {
                // set trackers
                TARDISTeleportLocation tp_in = new TARDISTeleportLocation();
                tp_in.setLocation(indoor);
                tp_in.setTardisId(id);
                tp_in.setDirection(indirection);
                tp_in.setAbandoned(abandoned);
                TARDISTeleportLocation tp_out = new TARDISTeleportLocation();
                tp_out.setLocation(exdoor);
                tp_out.setTardisId(id);
                tp_out.setDirection(exdirection);
                tp_out.setAbandoned(abandoned);
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    // players
                    uuids.forEach((u) -> plugin.getTrackerKeeper().getMover().add(u));
                }
                // locations
                if (tardis != null && preset != null) {
                    plugin.getTrackerKeeper().getPortals().put(exportal, tp_in);
                }
                plugin.getTrackerKeeper().getPortals().put(inportal, tp_out);
            }
        }
    }
}
