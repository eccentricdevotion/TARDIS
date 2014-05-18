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
package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetRepeaters;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * After materialization, the Astrosextant Rectifier will attempt to confirm
 * that a TARDIS has arrived at the correct coordinates. A damaged Sterometer
 * will reduce the accuracy of the Rectifier to within a few thousand light
 * years.
 *
 * @author eccentric_nz
 */
public class TARDISManualFlightRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<Location> target;
    private final List<String> controls = Arrays.asList("Helmic Regulator", "Astrosextant Rectifier", "Gravitic Anomaliser", "Absolute Tesseractulator");
    private int taskID;
    private final int loops = 10;
    private int i = 0;
    private final Random random = new Random();
    private final Player player;
    private final int id;
    private final UUID uuid;

    public TARDISManualFlightRunnable(TARDIS plugin, Player player, int id) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        target = getRepeaterList(this.id);
        this.uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getRepeaters().put(uuid, target);
    }

    @Override
    public void run() {
        if (i < loops) {
            int r = random.nextInt(4);
            Location loc = target.get(r);
            player.sendMessage("Click the " + controls.get(r));
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 152);
            plugin.getTrackerKeeper().getFlight().put(player.getUniqueId(), loc.toString());
            i++;
        } else {
            int blocks = 10 - plugin.getTrackerKeeper().getCount().get(player.getUniqueId());
            plugin.debug("You hit the correct control " + plugin.getTrackerKeeper().getCount().get(player.getUniqueId()) + " times out of 10!");
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            plugin.getTrackerKeeper().getCount().remove(player.getUniqueId());
            // adjust location
            if (blocks != 0) {
                Location adjusted = new TARDISFlightAdjustment(plugin).getLocation(plugin.getTrackerKeeper().getFlightData().get(uuid), blocks);
                plugin.getTrackerKeeper().getFlightData().get(uuid).setLocation(adjusted);
            }
        }
    }

    private List<Location> getRepeaterList(int id) {
        List<Location> repeaters = new ArrayList<Location>();
        ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, id, 0);
        if (rsr.resultSet()) {
            List<String> locs = rsr.getLocations();
            for (String l : locs) {
                repeaters.add(plugin.getUtils().getLocationFromDB(l, 0.0f, 0.0f));
            }
        }
        return repeaters;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
