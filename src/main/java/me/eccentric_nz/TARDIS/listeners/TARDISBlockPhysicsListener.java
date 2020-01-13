/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.TARDISRecordingQueue;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Material;
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
        Block block = event.getBlock();
        if (block != null && block.getType().equals(Material.GRASS_PATH)) {
            String loc = block.getRelative(BlockFace.UP).getLocation().toString();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> TARDISRecordingQueue.addToQueue(loc), 7L);
        }
        if (plugin.getTrackerKeeper().getMaterialising().size() > 0) {
            if (block != null) {
                BlockData state = block.getBlockData();
                if (state != null) {
                    if (state instanceof TrapDoor) {
                        Block blockBehind = getBlockBehindAttachable(block, ((TrapDoor) state).getFacing());
                        if (blockBehind != null) {
                            if (blockBehind.getType().equals(Material.GLASS) || blockBehind.getType().equals(Material.ICE) || TARDISMaterials.stained_glass.contains(blockBehind.getType())) {
                                event.setCancelled(true);
                            }
                        }
                    }
                    if (state instanceof Door) {
                        Block blockBelow = getBlockBelow(block);
                        if (blockBelow != null) {
                            if (blockBelow.getType().equals(Material.GLASS) || blockBelow.getType().equals(Material.ICE) || plugin.getGeneralKeeper().getDoors().contains(blockBelow.getType()) || TARDISMaterials.stained_glass.contains(blockBelow.getType()) || blockBelow.getType().equals(Material.AIR) || blockBelow.getType().equals(Material.SEA_LANTERN)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                if (block.getType().equals(Material.VINE)) {
                    event.setCancelled(true);
                }
            }
        } else if (block.getType().equals(Material.BROWN_MUSHROOM_BLOCK) || block.getType().equals(Material.RED_MUSHROOM_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
            event.setCancelled(true);
            event.getBlock().getState().update(true, false);
        }
    }

    private Block getBlockBehindAttachable(Block block, BlockFace face) {
        Block ret;
        switch (face) {
            case NORTH:
                ret = block.getRelative(BlockFace.SOUTH);
                break;
            case WEST:
                ret = block.getRelative(BlockFace.EAST);
                break;
            case SOUTH:
                ret = block.getRelative(BlockFace.NORTH);
                break;
            default:
                ret = block.getRelative(BlockFace.WEST);
                break;
        }
        return ret;
    }

    private Block getBlockBelow(Block block) {
        return block.getRelative(BlockFace.DOWN);
    }
}
