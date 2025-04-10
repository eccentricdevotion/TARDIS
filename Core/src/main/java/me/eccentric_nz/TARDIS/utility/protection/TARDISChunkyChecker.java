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
package me.eccentric_nz.TARDIS.utility.protection;

import me.eccentric_nz.TARDIS.TARDIS;
import org.popcraft.chunky.platform.Player;
import org.popcraft.chunky.platform.World;
import org.popcraft.chunky.platform.util.Location;
import org.popcraft.chunkyborder.ChunkyBorder;
import org.popcraft.chunkyborder.event.server.BlockPlaceEvent;
import org.popcraft.chunky.platform.BukkitPlayer;
import org.popcraft.chunky.platform.BukkitWorld;
/**
 * The TARDIS grants its passengers the ability to understand and speak other languages. This is due to the TARDIS's
 * telepathic field.
 *
 * @author eccentric_nz
 */
public class TARDISChunkyChecker {

    private final ChunkyBorder chunky;

    public TARDISChunkyChecker(TARDIS plugin) {
        chunky = plugin.getServer().getServicesManager().load(ChunkyBorder.class);
    }

    /**
     * Checks to see whether the specified location is outside a chunky border.
     *
     * @param p the player to check access for.
     * @param l the location to check.
     * @return true or false depending on whether the location is outside the border
     */
    public boolean isOutsideBorder(org.bukkit.entity.Player p, org.bukkit.Location l) {
        boolean bool = false;
        if (chunky != null) {
            final World world = new BukkitWorld(l.getWorld());
            final Location location = new Location(world, l.getX(), l.getY(), l.getZ());
            final Player player = new BukkitPlayer(p);
            final BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(player, location);
            chunky.getChunky().getEventBus().call(blockPlaceEvent);
            if (blockPlaceEvent.isCancelled()) {
                return true;
            }
        }
        return bool;
    }
}
