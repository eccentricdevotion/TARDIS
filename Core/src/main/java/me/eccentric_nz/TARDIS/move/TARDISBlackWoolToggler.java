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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISBlackWoolToggler {

    private final TARDIS plugin;

    public TARDISBlackWoolToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleBlocks(int id, Player player) {
        ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
        if (rsd.resultSet()) {
            Block b = rsd.getInnerBlock().getRelative(BlockFace.NORTH);
            BlockData mat;
            if (b.getType().isAir()) {
                mat = TARDISConstants.BLACK;
                plugin.getTrackerKeeper().getWoolToggles().remove(id);
            } else {
                mat = TARDISConstants.AIR;
                plugin.getTrackerKeeper().getWoolToggles().add(id);
            }
            b.setBlockData(mat);
            b.getRelative(BlockFace.UP).setBlockData(mat);
            Block door = b.getRelative(BlockFace.SOUTH);
            if (Tag.DOORS.isTagged(door.getType()) && TARDISStaticUtils.isDoorOpen(door)) {
                ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
                if (rs.fromID(id)) {
                    Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                    boolean outerDisplayDoor = rs.getPreset().usesArmourStand();
                    UUID playerUUID = player.getUniqueId();
                    // toggle doors shut / deactivate portals
                    // close inner
                    if (innerDisplayDoor.display()) {
                        new InnerDisplayDoorCloser(plugin).close(door, id, playerUUID, false);
                    } else {
                        new InnerMinecraftDoorCloser(plugin).close(door, id, playerUUID);
                    }
                    // close outer
                    if (outerDisplayDoor) {
                        new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                    } else if (rs.getPreset().hasDoor()) {
                        new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                    }
                }
            }
        }
    }

    public boolean isOpen(int id) {
        ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
        if (rsd.resultSet()) {
            Block b = rsd.getInnerBlock().getRelative(BlockFace.NORTH);
            return b.getType().isAir();
        } else {
            return false;
        }
    }
}
