/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkReturnRunnable implements Runnable {

    private final TARDIS plugin;
    private final long waitTime;

    public TARDISJunkReturnRunnable(TARDIS plugin) {
        this.plugin = plugin;
        this.waitTime = this.plugin.getConfig().getLong("junk.return") * 1000;
    }

    @Override
    public void run() {
        // get time junk tardis was last used
        long lastUsed = plugin.getGeneralKeeper().getJunkTime();
        // get current time
        long now = System.currentTimeMillis();
        if (lastUsed + waitTime > now) {
            // check the Junk TARDIS is not home already
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                // get current location
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", rs.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                if (rsc.resultSet()) {
                    // get home location
                    HashMap<String, Object> whereh = new HashMap<String, Object>();
                    whereh.put("tardis_id", rs.getTardis_id());
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
                    if (rsh.resultSet()) {
                        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        Location home = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                        // compare locations
                        if (current.equals(home)) {
                            // bring her home
                            new TARDISJunkReturn(plugin).recall(plugin.getConsole());
                        }
                    }
                }
            }
        }
    }
}
