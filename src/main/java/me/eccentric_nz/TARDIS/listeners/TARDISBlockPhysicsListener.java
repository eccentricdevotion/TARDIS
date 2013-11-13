/*
 * Copyright (C) 2013 eccentric_nz
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

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
    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (plugin.isPresetMaterialising.size() > 0) {
            Block block = event.getBlock();
            if (block.getType() == Material.TRAP_DOOR) {
                Block blockBehind = getBlockBehindHatch(block);
                if (blockBehind != null) {
                    if (blockBehind.getType().equals(Material.GLASS) || blockBehind.getType().equals(Material.ICE)) {
                        event.setCancelled(true);
                    }
                }
            }
            if (block.getType() == Material.TORCH || block.getType() == Material.IRON_DOOR_BLOCK || block.getType() == Material.WOODEN_DOOR || block.getType() == Material.RED_ROSE) {
                Block blockBelow = getBlockBelow(block);
                if (blockBelow != null) {
                    if (blockBelow.getType().equals(Material.GLASS) || blockBelow.getType().equals(Material.ICE) || blockBelow.getType().equals(Material.WOODEN_DOOR) || blockBelow.getType().equals(Material.IRON_DOOR_BLOCK)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    Block getBlockBehindHatch(Block block) {
        switch (block.getData()) {
            case 0:
                return block.getRelative(BlockFace.SOUTH);
            case 1:
                return block.getRelative(BlockFace.NORTH);
            case 2:
                return block.getRelative(BlockFace.EAST);
            case 3:
                return block.getRelative(BlockFace.WEST);
            default:
                return null;
        }
    }

    Block getBlockBelow(Block block) {
        return block.getRelative(BlockFace.DOWN);
    }
}
