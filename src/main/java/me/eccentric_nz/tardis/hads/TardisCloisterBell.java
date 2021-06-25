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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.hads;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

/**
 * The Cloister Bell is a signal to the crew that a catastrophe that could threaten even a tardis is occurring or will
 * soon occur. In short it is a call to "battle-stations." Cloister Bells can be found on all TARDISes since the Type
 * 22.
 * <p>
 * The bell will ring for events ranging from the phasing of the tardis engines, to the detection of a vortex
 * discontinuity, to the imminent heat death of the universe. Though it is located in the Cloister Room, the Bell can be
 * heard from anywhere in a tardis. Normally controlled by the Main Logic Junction, the Cloister Bell is linked to the
 * flow of the Universe and thus able to predict many disasters before they occur. Even when the tardis Sentient Matrix
 * is unconscious the Main Logic Junction can still trigger the bell. The Cloister Bell can also be manually rung from
 * the Control Room by crew members.
 */
public class TardisCloisterBell implements Runnable {

    private final TardisPlugin plugin;
    private final int loops;
    private final int id;
    private final Location centre;
    private final Location current;
    private final Player player;
    private final boolean messageOn;
    private final String reason;
    private final boolean messageOff;
    private int i = 0;
    private int task;

    /**
     * Runnable class to action a repeating cloister bell task. Each loop should take 70 ticks.
     *
     * @param plugin an instance of the tardis plugin
     * @param loops  the number of times to ring the cloister bell
     * @param id     the id of the tardis whose cloister bell we are ringing
     */
    public TardisCloisterBell(TardisPlugin plugin, int loops, int id) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        current = getCurrent(this.id);
        player = getPlayer(this.id);
        messageOn = false;
        messageOff = false;
        reason = "";
    }

    public TardisCloisterBell(TardisPlugin plugin, int loops, int id, Player player) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        current = getCurrent(this.id);
        this.player = player;
        messageOn = false;
        reason = "";
        messageOff = false;
    }

    TardisCloisterBell(TardisPlugin plugin, int loops, int id, Location current, Player player, String reason) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        this.current = current;
        this.player = player;
        messageOn = true;
        this.reason = reason;
        messageOff = true;
    }

    public TardisCloisterBell(TardisPlugin plugin, int loops, int id, Location current, Player player, boolean messageOn, String reason, boolean messageOff) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        this.current = current;
        this.player = player;
        this.messageOn = messageOn;
        this.reason = reason;
        this.messageOff = messageOff;
    }

    @Override
    public void run() {
        if (messageOn && i == 0 && player != null && player.isOnline()) {
            TardisMessage.send(player, "CLOISTER_BELL_ON", reason);
        }
        if (i < loops) {
            if (centre != null) {
                // play sound in tardis with range size of ars grid
                Objects.requireNonNull(centre.getWorld()).playSound(centre, "cloister_bell", 10.0f, 1.0f);
            }
            if (current != null) {
                // play sound outside the tardis with a range of 32 blocks
                Objects.requireNonNull(current.getWorld()).playSound(current, "cloister_bell", 2.0f, 1.0f);
            }
            if (player != null && player.isOnline()) {
                // play sound at Time Lords location (if they are not in the tardis and not within range of the tardis exterior)
                Location location = player.getLocation();
                if (!plugin.getUtils().inTardisWorld(player)) {
                    assert current != null;
                    if (!isInPoliceBoxRange(current, location)) {
                        Objects.requireNonNull(location.getWorld()).playSound(location, "cloister_bell", 1.0f, 1.0f);
                    }
                }
            }
            i++;
        } else {
            plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getCloisterBells().get(id));
            task = -1;
            plugin.getTrackerKeeper().getCloisterBells().remove(id);
            if (messageOff && player != null && player.isOnline()) {
                TardisMessage.send(player, "CLOISTER_BELL_OFF");
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }

    private Location getCentre(int id) {
        // get the location of the centre of the tardis
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            if (!rs.getTardis().getCreeper().isEmpty()) {
                return TardisStaticLocationGetters.getLocationFromDB(rs.getTardis().getCreeper());
            } else if (!rs.getTardis().getBeacon().isEmpty()) {
                return TardisStaticLocationGetters.getLocationFromDB(rs.getTardis().getBeacon());
            }
        }
        return null;
    }

    private Location getCurrent(int id) {
        // get the location of the tardis Police Box
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
        if (rs.resultSet()) {
            return new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        }
        return null;
    }

    private Player getPlayer(int id) {
        // get Time Lord of this tardis
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            return plugin.getServer().getPlayer(rs.getTardis().getUuid());
        }
        return null;
    }

    private boolean isInPoliceBoxRange(Location current, Location location) {
        // check locations are in the same world
        if (current.getWorld() != location.getWorld()) {
            return false;
        }
        // same world check the location is contained in a 32 block radius of Police Box
        int x = location.getBlockX();
        int z = location.getBlockZ();
        int minX = current.getBlockX() - 32;
        int maxX = current.getBlockX() + 32;
        int minZ = current.getBlockZ() - 32;
        int maxZ = current.getBlockZ() + 32;
        return (x > minX && x < maxX && z > minZ && z < maxZ);
    }
}
