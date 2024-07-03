package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.particles.Sphere;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class EyeOfHarmonyPartcles {

    private final TARDIS plugin;
    public EyeOfHarmonyPartcles(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static int stopTask(TARDIS plugin, int id) {
        int task = -1;
        // stop the current particles
        ResultSetArtronStorage rs = new ResultSetArtronStorage(plugin);
        if (rs.fromID(id)) {
            task = rs.getTask();
            if (task != -1) {
                plugin.getServer().getScheduler().cancelTask(task);
            }
        }
        return task;
    }

    public int stopStart(int id, int capacitors, UUID uuid) {
        int task = stopTask(plugin, id);
        // only restart if there are capacitors in storage
        if (capacitors > 0) {
            // get the new settings
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            wherec.put("type", 53);
            ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
            if (rsc.resultSet()) {
                Location s = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                Capacitor capacitor = Capacitor.values()[capacitors - 1];
                Sphere sphere = new Sphere(plugin, uuid, s, capacitor);
                task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sphere, 0, 10);
                sphere.setTaskID(task);
            }
        }
        return task;
    }
}
