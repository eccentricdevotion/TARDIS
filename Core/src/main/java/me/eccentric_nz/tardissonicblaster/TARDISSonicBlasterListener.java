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
package me.eccentric_nz.tardissonicblaster;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardissonicblaster.database.ResultSetBlaster;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TARDISSonicBlasterListener implements Listener {

    private final TARDIS plugin;

    public TARDISSonicBlasterListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (TARDISSonicBlasterUtils.checkBlasterInHand(player)) {
                UUID uuid = player.getUniqueId();
                if (!player.isSneaking()) {
                    // remove blocks
                    long time = System.currentTimeMillis();
                    long lastuse = (plugin.getBlasterSettings().getIsBlasting().containsKey(uuid)) ? plugin.getBlasterSettings().getIsBlasting().get(uuid) : 0;
                    if (time > lastuse + plugin.getBlasterSettings().getCooldown()) {
                        // get distance
                        Location target = event.getClickedBlock().getLocation();
                        double distance = TARDISSonicBlasterUtils.getDistanceToTargetBlock(target, player);
                        double angle = TARDISSonicBlasterUtils.getLineOfSightAngle(player);
                        COMPASS direction = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                        ResultSetBlaster rsb = new ResultSetBlaster(plugin, uuid);
                        if (rsb.resultSet()) {
                            // get power
                            double power = rsb.getTachyonLevel() / 10.0d;
                            new TARDISSonicBlasterAction(plugin).blast(target, direction, angle, distance, power, uuid);
                        }
                    }
                } else {
                    // TODO restore blocks
                }
            }
        }
    }
}
