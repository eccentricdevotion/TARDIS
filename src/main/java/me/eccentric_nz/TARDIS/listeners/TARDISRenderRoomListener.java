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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRenderRoomListener implements Listener {

    private final TARDIS plugin;

    public TARDISRenderRoomListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (plugin.trackTransmat.contains(player.getName())) {
            event.setCancelled(true);
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                // tp the player back to the TARDIS console
                transmat(player);
            }
        }
    }

    public void transmat(final Player p) {
        p.sendMessage(plugin.pluginName + "Stand by for transmat...");
        // get the TARDIS the player is in
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("player", p.getName());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("tardis_id", id);
            whered.put("door_type", 1);
            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
            if (rsd.resultSet()) {
                COMPASS d = rsd.getDoor_direction();
                String doorLocStr = rsd.getDoor_location();
                String[] split = doorLocStr.split(":");
                World cw = plugin.getServer().getWorld(split[0]);
                int cx = plugin.utils.parseInt(split[1]);
                int cy = plugin.utils.parseInt(split[2]);
                int cz = plugin.utils.parseInt(split[3]);
                Location tmp_loc = new Location(cw, cx, cy, cz);
                int getx = tmp_loc.getBlockX();
                int getz = tmp_loc.getBlockZ();
                switch (d) {
                    case NORTH:
                        // z -ve
                        tmp_loc.setX(getx + 0.5);
                        tmp_loc.setZ(getz - 0.5);
                        break;
                    case EAST:
                        // x +ve
                        tmp_loc.setX(getx + 1.5);
                        tmp_loc.setZ(getz + 0.5);
                        break;
                    case SOUTH:
                        // z +ve
                        tmp_loc.setX(getx + 0.5);
                        tmp_loc.setZ(getz + 1.5);
                        break;
                    case WEST:
                        // x -ve
                        tmp_loc.setX(getx - 0.5);
                        tmp_loc.setZ(getz + 0.5);
                        break;
                }
                tmp_loc.setPitch(p.getLocation().getPitch());
                tmp_loc.setYaw(p.getLocation().getYaw());
                final Location tp_loc = tmp_loc;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        p.playSound(tp_loc, Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        p.teleport(tp_loc);
                        plugin.trackTransmat.remove(p.getName());
                    }
                }, 10L);
            } else {
                p.sendMessage(plugin.pluginName + "The Transmat device couldn't find the TARDIS console!");
            }
        } else {
            p.sendMessage(plugin.pluginName + "The Transmat device couldn't determine which TARDIS you are in!");
        }
    }
}
