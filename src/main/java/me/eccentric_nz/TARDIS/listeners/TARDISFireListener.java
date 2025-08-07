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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;

/**
 * The Tenth Doctor once commented that fire was "so fun to look at! But bad for the skin!"
 *
 * @author eccentric_nz
 */
public class TARDISFireListener implements Listener {

    private final TARDIS plugin;

    public TARDISFireListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for block burn and ignite events around the TARDIS. If the affected block is part of the TARDIS, then the
     * event is canceled there by providing protection for the TARDIS blocks
     *
     * @param event a block catching fire
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block b = event.getBlock();
        for (BlockFace bf : plugin.getGeneralKeeper().getBlockFaces()) {
            Block chkBlock = b.getRelative(bf);
            String l = chkBlock.getLocation().toString();
            if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(l) && !chkBlock.getType().equals(Material.BEDROCK)) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(l)) {
            event.setCancelled(true);
        }
    }
}
