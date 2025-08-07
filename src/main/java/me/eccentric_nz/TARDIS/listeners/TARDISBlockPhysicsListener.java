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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * @author eccentric_nz
 */
public class TARDISBlockPhysicsListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockPhysicsListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    // prevent hatches from breaking
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (!plugin.getTrackerKeeper().getMaterialising().isEmpty()) {
            Block block = event.getBlock();
            BlockData state = block.getBlockData();
            if (state instanceof TrapDoor trapDoor) {
                Block blockBehind = getBlockBehindAttachable(block, trapDoor.getFacing());
                if (blockBehind.getType().equals(Material.GLASS) || blockBehind.getType().equals(Material.ICE) || Tag.IMPERMEABLE.isTagged(blockBehind.getType())) {
                    event.setCancelled(true);
                }
            }
            if (state instanceof Door) {
                Block blockBelow = block.getRelative(BlockFace.DOWN);
                if (blockBelow.getType().equals(Material.GLASS) || blockBelow.getType().equals(Material.ICE) || Tag.DOORS.isTagged(blockBelow.getType()) || Tag.IMPERMEABLE.isTagged(blockBelow.getType()) || blockBelow.getType().isAir() || blockBelow.getType().equals(Material.SEA_LANTERN)) {
                    event.setCancelled(true);
                }
            }
            if (block.getType().equals(Material.VINE)) {
                event.setCancelled(true);
            }
        }
    }

    private Block getBlockBehindAttachable(Block block, BlockFace face) {
        return switch (face) {
            case NORTH -> block.getRelative(BlockFace.SOUTH);
            case WEST -> block.getRelative(BlockFace.EAST);
            case SOUTH -> block.getRelative(BlockFace.NORTH);
            default -> block.getRelative(BlockFace.WEST);
        };
    }
}
