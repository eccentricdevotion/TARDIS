package me.eccentric_nz.TARDIS.hads;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * The Cloister Bell is a signal to the crew that a catastrophe that could threaten even a TARDIS is occurring or will
 * soon occur. In short it is a call to "battle-stations." Cloister Bells can be found on all TARDISes since the Type
 * 22.
 * <p>
 * The bell will ring for events ranging from the phasing of the TARDIS engines, to the detection of a vortex
 * discontinuity, to the imminent heat death of the universe. Though it is located in the Cloister Room, the Bell can be
 * heard from anywhere in a TARDIS. Normally controlled by the Main Logic Junction, the Cloister Bell is linked to the
 * flow of the Universe and thus able to predict many disasters before they occur. Even when the TARDIS Sentient Matrix
 * is unconscious the Main Logic Junction can still trigger the bell. The Cloister Bell can also be manually rung from
 * the Control Room by crew members.
 */
public class TARDISCloisterBell implements Runnable {

    private final TARDIS plugin;
    private final int loops;
    private final int id;
    private int i = 0;
    private final Location centre;
    private final Location current;
    private final Player player;
    private int task;

    /**
     * Runnable class to action a repeating cloister bell task. Each loop should take 70 ticks.
     *
     * @param plugin an instance of the TARDIS plugin
     * @param loops  the number of times to ring the cloister bell
     */
    public TARDISCloisterBell(TARDIS plugin, int loops, int id) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        current = getCurrent(this.id);
        player = getPlayer(this.id);
    }

    public TARDISCloisterBell(TARDIS plugin, int loops, int id, Player player) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        current = getCurrent(this.id);
        this.player = player;
    }

    public TARDISCloisterBell(TARDIS plugin, int loops, int id, Location current, Player player) {
        this.plugin = plugin;
        this.loops = loops;
        this.id = id;
        centre = getCentre(this.id);
        this.current = current;
        this.player = player;
    }

    @Override
    public void run() {
        if (i == 0 && player != null && player.isOnline()) {
            TARDISMessage.send(player, "CLOISTER_BELL_ON");
        }
        if (i < loops) {
            if (centre != null) {
                // play sound in TARDIS with range size of ARS grid
                centre.getWorld().playSound(centre, "cloister_bell", 10.0f, 1.0f);
            }
            if (current != null) {
                // play sound outside the TARDIS with a range of 32 blocks
                current.getWorld().playSound(current, "cloister_bell", 2.0f, 1.0f);
            }
            if (player != null && player.isOnline()) {
                // play sound at Time Lords location (if they are not in the TARDIS and not within range of the TARDIS exterior)
                Location location = player.getLocation();
                if (!plugin.getUtils().inTARDISWorld(player) && !isInPoliceBoxRange(current, location)) {
                    location.getWorld().playSound(location, "cloister_bell", 1.0f, 1.0f);
                }
            }
            i++;
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            plugin.getTrackerKeeper().getCloisterBells().remove(id);
            if (player != null && player.isOnline()) {
                TARDISMessage.send(player, "CLOISTER_BELL_OFF");
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }

    private Location getCentre(int id) {
        // get the location of the centre of the TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            if (!rs.getTardis().getCreeper().isEmpty()) {
                return TARDISStaticLocationGetters.getLocationFromDB(rs.getTardis().getCreeper());
            } else if (!rs.getTardis().getBeacon().isEmpty()) {
                return TARDISStaticLocationGetters.getLocationFromDB(rs.getTardis().getBeacon());
            }
        }
        return null;
    }

    private Location getCurrent(int id) {
        // get the location of the TARDIS Police Box
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
        if (rs.resultSet()) {
            return new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        }
        return null;
    }

    private Player getPlayer(int id) {
        // get Time Lord of this TARDIS
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
