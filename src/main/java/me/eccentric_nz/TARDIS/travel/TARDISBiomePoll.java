/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel;

import com.google.common.base.Strings;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISBiomePoll implements Runnable {

    private final TARDIS plugin;
    private final TARDISBiomeFinder biomeFinder;
    private final long timeout;
    private final Player player;
    private final int id;
    private final COMPASS direction;
    private int taskid;
    private int dots = 0;

    public TARDISBiomePoll(TARDIS plugin, TARDISBiomeFinder biomeFinder, long timeout, Player player, int id, COMPASS direction) {
        this.plugin = plugin;
        this.biomeFinder = biomeFinder;
        this.timeout = timeout;
        this.player = player;
        this.id = id;
        this.direction = direction;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() < timeout) {
            // display action bar so players know the task is still running
            String message = "§6Searching biome" + Strings.repeat(" .", dots);
            plugin.getTardisHelper().sendActionBarMessage(player, message);
            dots++;
            if (biomeFinder.poll()) {
                Location tb = biomeFinder.getLocation();
                // cancel biome finder
                plugin.getServer().getScheduler().cancelTask(biomeFinder.getTaskid());
                if (!plugin.getPluginRespect().getRespect(tb, new Parameters(player, Flag.getDefaultFlags()))) {
                    if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                        plugin.getTrackerKeeper().getMalfunction().put(id, true);
                    } else {
                        // cancel
                        TARDISMessage.send(player, "PROTECTED");
                        plugin.getServer().getScheduler().cancelTask(taskid);
                        taskid = -1;
                        player.resetTitle();
                    }
                }
                World bw = tb.getWorld();
                // check location
                while (!bw.getChunkAt(tb).isLoaded()) {
                    bw.getChunkAt(tb).load();
                }
                int[] start_loc = TARDISTimeTravel.getStartLocation(tb, direction);
                int tmp_y = tb.getBlockY();
                for (int up = 0; up < 10; up++) {
                    int count = TARDISTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], tb.getWorld(), direction);
                    if (count == 0) {
                        tb.setY(tmp_y + up);
                        break;
                    }
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("world", tb.getWorld().getName());
                set.put("x", tb.getBlockX());
                set.put("y", tb.getBlockY());
                set.put("z", tb.getBlockZ());
                set.put("direction", direction.toString());
                set.put("submarine", 0);
                HashMap<String, Object> tid = new HashMap<>();
                tid.put("tardis_id", id);
                plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                TARDISMessage.send(player, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
                plugin.getTrackerKeeper().getRescue().remove(id);
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    new TARDISLand(plugin, id, player).exitVortex();
                }
                plugin.getServer().getScheduler().cancelTask(taskid);
                taskid = -1;
                player.resetTitle();
            }
        } else {
            TARDISMessage.send(player, "BIOME_NOT_FOUND");
            // cancel biome finder
            plugin.getServer().getScheduler().cancelTask(biomeFinder.getTaskid());
            // cancel this task
            plugin.getServer().getScheduler().cancelTask(taskid);
            taskid = -1;
            player.resetTitle();
        }
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }
}
