/*
 * Copyright (C) 2021 eccentric_nz
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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

/**
 * @author eccentric_nz
 */
public class TARDISPistonHarvesterListener implements Listener {

    private final TARDIS plugin;

    public TARDISPistonHarvesterListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHarvestPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isSticky()) {
            return;
        }
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.only_tardis_worlds") && !event.getBlock().getWorld().getName().contains("TARDIS")) {
            return;
        }
        String block = event.getBlock().getRelative(getOppositeDirection(event.getDirection()), 2).getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHarvestPistonExtend(BlockPistonExtendEvent event) {
        if (plugin.getConfig().getBoolean("preferences.nerf_pistons.only_tardis_worlds") && !event.getBlock().getWorld().getName().contains("TARDIS")) {
            return;
        }
        for (Block b : event.getBlocks()) {
            String block = b.getLocation().toString();
            if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    private BlockFace getOppositeDirection(BlockFace bf) {
        switch (bf) {
            case NORTH:
                return BlockFace.SOUTH;
            case EAST:
                return BlockFace.WEST;
            case SOUTH:
                return BlockFace.NORTH;
            default:
                return BlockFace.EAST;
        }
    }
}
