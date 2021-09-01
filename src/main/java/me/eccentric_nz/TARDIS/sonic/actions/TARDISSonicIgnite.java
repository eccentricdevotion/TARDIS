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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;

public class TARDISSonicIgnite {

    public static void ignite(TARDIS plugin, Block b, Player p) {
        if (TARDISSonicRespect.checkBlockRespect(plugin, p, b)) {
            if (b.getBlockData() instanceof Lightable lightable) {
                if (!lightable.isLit()) {
                    lightable.setLit(true);
                    b.setBlockData(lightable);
                    plugin.getPM().callEvent(new BlockIgniteEvent(b, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, p));
                }
                return;
            }
            Block above = b.getRelative(BlockFace.UP);
            if (b.getType().equals(Material.TNT)) {
                b.setBlockData(TARDISConstants.AIR);
                b.getWorld().spawnEntity(b.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.PRIMED_TNT);
                plugin.getPM().callEvent(new BlockIgniteEvent(b, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, p));
                return;
            }
            if (above.getType().isAir()) {
                // delay 2 ticks as player may have clicked top of block automatically extinguishing the fire
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    above.setBlockData(TARDISConstants.FIRE);
                    // call a block ignite event
                    plugin.getPM().callEvent(new BlockIgniteEvent(b, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, p));
                }, 2L);
            }
        }
    }
}
