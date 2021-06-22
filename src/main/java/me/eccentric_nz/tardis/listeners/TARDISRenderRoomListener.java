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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisEntityTracker;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisRenderRoomListener implements Listener {

    private final TardisPlugin plugin;

    public TardisRenderRoomListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getRenderRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            if (Objects.equals(event.getHand(), EquipmentSlot.HAND) && (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
                // tp the player back to the tardis console
                transmat(player);
                player.updateInventory();
            }
        }
    }

    public void transmat(Player p) {
        TardisMessage.send(p, "TRANSMAT");
        // get the tardis the player is in
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", p.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
        if (rst.resultSet()) {
            int id = rst.getTardisId();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            whered.put("door_type", 1);
            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
            if (rsd.resultSet()) {
                CardinalDirection d = rsd.getDoorDirection();
                Location tp_loc = TardisStaticLocationGetters.getLocationFromDB(rsd.getDoorLocation());
                assert tp_loc != null;
                int getx = tp_loc.getBlockX();
                int getz = tp_loc.getBlockZ();
                switch (d) {
                    case NORTH -> {
                        // z -ve
                        tp_loc.setX(getx + 0.5);
                        tp_loc.setZ(getz - 0.5);
                    }
                    case EAST -> {
                        // x +ve
                        tp_loc.setX(getx + 1.5);
                        tp_loc.setZ(getz + 0.5);
                    }
                    case SOUTH -> {
                        // z +ve
                        tp_loc.setX(getx + 0.5);
                        tp_loc.setZ(getz + 1.5);
                    }
                    case WEST -> {
                        // x -ve
                        tp_loc.setX(getx - 0.5);
                        tp_loc.setZ(getz + 0.5);
                    }
                }
                tp_loc.setPitch(p.getLocation().getPitch());
                tp_loc.setYaw(p.getLocation().getYaw());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    p.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    p.teleport(tp_loc);
                    plugin.getTrackerKeeper().getRenderRoomOccupants().remove(p.getUniqueId());
                    if (plugin.getTrackerKeeper().getRenderedNPCs().containsKey(p.getUniqueId())) {
                        new TardisEntityTracker(plugin).removeNPCs(p.getUniqueId());
                    }
                }, 10L);
            } else {
                TardisMessage.send(p, "TRANSMAT_NO_CONSOLE");
            }
        } else {
            TardisMessage.send(p, "TRANSMAT_NO_TARDIS");
        }
    }
}
