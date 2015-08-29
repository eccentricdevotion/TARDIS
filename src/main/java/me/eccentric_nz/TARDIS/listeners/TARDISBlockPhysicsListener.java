/*
 * Copyright (C) 2014 eccentric_nz
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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SimpleAttachableMaterialData;

/**
 *
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
        if (plugin.getTrackerKeeper().getInVortex().size() > 0) {
            Block block = event.getBlock();
            if (block != null) {
                BlockState state;
                try {
                    state = block.getState();
                    if (state != null) {
                        MaterialData md = state.getData();
                        if (md instanceof SimpleAttachableMaterialData) {
                            Block blockBehind = getBlockBehindAttachable(block, ((SimpleAttachableMaterialData) md).getFacing());
                            if (blockBehind != null) {
                                if (blockBehind.getType().equals(Material.GLASS) || blockBehind.getType().equals(Material.ICE) || blockBehind.getType().equals(Material.STAINED_GLASS)) {
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    plugin.debug("Invalid Tile Entity detected at " + block.getLocation());
                }
                if (block.getType().equals(Material.VINE)) {
                    event.setCancelled(true);
                }
                if (plugin.getGeneralKeeper().getDoors().contains(block.getType())) {
                    Block blockBelow = getBlockBelow(block);
                    if (blockBelow != null) {
                        if (blockBelow.getType().equals(Material.GLASS) || blockBelow.getType().equals(Material.ICE) || plugin.getGeneralKeeper().getDoors().contains(blockBelow.getType()) || blockBelow.getType().equals(Material.STAINED_GLASS) || blockBelow.getType().equals(Material.AIR)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    Block getBlockBehindAttachable(Block block, BlockFace face) {
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

    Block getBlockBelow(Block block) {
        return block.getRelative(BlockFace.DOWN);
    }
}
