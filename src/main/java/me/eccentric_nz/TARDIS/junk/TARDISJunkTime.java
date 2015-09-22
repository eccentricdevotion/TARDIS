/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkTime {

    private final TARDIS plugin;

    public TARDISJunkTime(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean elapsed(CommandSender sender) {
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
                    if (!current.equals(home)) {
                        long conf = plugin.getConfig().getLong("junk.return");
                        if (conf > 0) {
                            long waitTime = conf * 1000;
                            long lastUsed = plugin.getGeneralKeeper().getJunkTime();
                            long now = System.currentTimeMillis();
                            long returnTime = (waitTime - (now - lastUsed)) / 1000;
                            long mins = returnTime / 60;
                            long secs = returnTime - (mins * 60);
                            String sub = String.format("%d minutes %d seconds", mins, secs);
                            TARDISMessage.send(sender, "JUNK_RETURN_TIME", sub);
                        } else {
                            TARDISMessage.send(sender, "JUNK_NO_RETURN");
                        }
                    } else {
                        TARDISMessage.send(sender, "JUNK_AT_HOME");
                    }
                }
            }
        }
        return true;
    }
}
