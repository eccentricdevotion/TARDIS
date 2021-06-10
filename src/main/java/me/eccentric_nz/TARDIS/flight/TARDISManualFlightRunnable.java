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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetRepeaters;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * After materialization, the Astrosextant Rectifier will attempt to confirm that a tardis has arrived at the correct
 * coordinates. A damaged Sterometer will reduce the accuracy of the Rectifier to within a few thousand light years.
 *
 * @author eccentric_nz
 */
class TARDISManualFlightRunnable implements Runnable {

    private static final int LOOPS = 10;
    private final TARDISPlugin plugin;
    private final List<Location> target;
    private final List<String> controls = Arrays.asList("Helmic Regulator", "Astrosextant Rectifier", "Gravitic Anomaliser", "Absolute Tesseractulator");
    private final Player player;
    private final UUID uuid;
    private int taskId;
    private int i = 0;

    TARDISManualFlightRunnable(TARDISPlugin plugin, Player player, int id) {
        this.plugin = plugin;
        this.player = player;
        target = getRepeaterList(id);
        uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getRepeaters().put(uuid, target);
    }

    @Override
    public void run() {
        // always add them to the tracker, in case they sit there and do nothing...
        plugin.getTrackerKeeper().getCount().put(uuid, 0);
        if (i < LOOPS) {
            i++;
            if (target.size() < 4) {
                TARDISMessage.send(player, "FLIGHT_BAD");
                return;
            }
            int r = TARDISConstants.RANDOM.nextInt(4);
            Location loc = target.get(r);
            TARDISMessage.send(player, "FLIGHT_CLICK", controls.get(r));
            Objects.requireNonNull(loc.getWorld()).playEffect(loc, Effect.STEP_SOUND, 152);
            plugin.getTrackerKeeper().getFlight().put(player.getUniqueId(), loc.toString());
        } else {
            int blocks = 10 - plugin.getTrackerKeeper().getCount().get(player.getUniqueId());
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = 0;
            plugin.getTrackerKeeper().getCount().remove(player.getUniqueId());
            plugin.getTrackerKeeper().getFlight().remove(uuid);
            // adjust location
            if (blocks != 0) {
                Location adjusted = new TARDISFlightAdjustment(plugin).getLocation(plugin.getTrackerKeeper().getFlightData().get(uuid), blocks);
                plugin.getTrackerKeeper().getFlightData().get(uuid).setLocation(adjusted);
            }
            plugin.getTrackerKeeper().getRepeaters().remove(uuid);
        }
    }

    private List<Location> getRepeaterList(int id) {
        ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, id, 0);
        if (rsr.resultSet()) {
            return rsr.getLocations();
        }
        return null;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
