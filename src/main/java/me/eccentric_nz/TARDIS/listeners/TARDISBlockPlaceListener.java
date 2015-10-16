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
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * TARDISes are bioships that are grown from a species of coral presumably
 * indigenous to Gallifrey.
 *
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private
 * study". Inside it were momentos of his many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISBlockPlaceListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player block placing. If the player places a stack of blocks
     * in a certain pattern for example (but not limited to): IRON_BLOCK,
     * LAPIS_BLOCK, RESTONE_TORCH the pattern of blocks is turned into a TARDIS.
     *
     * @param event a player placing a block
     */
    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NOT_IN_ZERO");
            return;
        }
        String block = event.getBlockPlaced().getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(block)) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NO_PLACE");
        }
    }
}
