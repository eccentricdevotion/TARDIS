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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISCustomModelChanger;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class DisplayItemDoorToggler {

    private final TARDIS plugin;

    public DisplayItemDoorToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openClose(Player player, Block block, boolean close, TARDISDisplayItem tdi) {
        Location block_loc = block.getLocation();
        String bw = block_loc.getWorld().getName();
        int bx = block_loc.getBlockX();
        int by = block_loc.getBlockY();
        int bz = block_loc.getBlockZ();
        String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
        HashMap<String, Object> where = new HashMap<>();
        where.put("door_location", doorloc);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
        if (rsd.resultSet()) {
            int id = rsd.getTardis_id();
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                plugin.getMessenger().sendStatus(player, "SIEGE_NO_EXIT");
                return;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                return;
            }
            // handbrake must be on
            HashMap<String, Object> tid = new HashMap<>();
            tid.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
            if (rs.resultSet()) {
                if (!rs.getTardis().isHandbrakeOn()) {
                    plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                    return;
                }
                UUID playerUUID = player.getUniqueId();
                // must be Time Lord or companion
                ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                if (rsc.getCompanions().contains(playerUUID) || rs.getTardis().isAbandoned()) {
                    if (!rsd.isLocked()) {
                        // toggle the door
                        if (rs.getTardis().getPreset().usesArmourStand()) {
                            new TARDISCustomModelChanger(plugin, player, id, rs.getTardis().getPreset()).toggleOuterDoor();
                        }
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUUID.toString());
                        boolean minecart = false;
                        if (rsp.resultSet()) {
                            minecart = rsp.isMinecartOn();
                        }
                        new TARDISDoorToggler(plugin, block, player, minecart, close, id).toggleDoorsWithoutSound(tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR_OPEN || tdi == TARDISDisplayItem.BONE_DOOR || tdi == TARDISDisplayItem.BONE_DOOR_OPEN || tdi == TARDISDisplayItem.CUSTOM_DOOR);
                    } else if (!rs.getTardis().getUuid().equals(playerUUID)) {
                        plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_NEED_UNLOCK");
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_COMPANION");
                }
            }
        }
    }
}
