/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import org.bukkit.Location;
import org.bukkit.block.Biome;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkLocation {

    private final TARDIS plugin;
    private Location current;
    private Location home;
    private int id;
    private Biome biome;

    public TARDISJunkLocation(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean isNotHome() {
        // check the Junk TARDIS is not home already
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
            id = rs.getTardis_id();
            // get current location
            HashMap<String, Object> wherec = new HashMap<String, Object>();
            wherec.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
            if (rsc.resultSet()) {
                biome = rsc.getBiome();
                // get home location
                HashMap<String, Object> whereh = new HashMap<String, Object>();
                whereh.put("tardis_id", id);
                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
                if (rsh.resultSet()) {
                    current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                    home = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                    // compare locations
                    return !current.equals(home);
                }
            }
        }
        return true;
    }

    public Location getCurrent() {
        return current;
    }

    public Location getHome() {
        return home;
    }

    public int getId() {
        return id;
    }

    public Biome getBiome() {
        return biome;
    }
}
