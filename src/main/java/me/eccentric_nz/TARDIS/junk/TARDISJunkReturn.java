/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkReturn {

    private final TARDIS plugin;

    public TARDISJunkReturn(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean recall(CommandSender sender) {
        if (!sender.hasPermission("tardis.admin")) {
            TARDISMessage.send(sender, "CMD_ADMIN");
            return true;
        }
        // get junk TARDIS id
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            // get current location
            HashMap<String, Object> wherec = new HashMap<String, Object>();
            wherec.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
            if (rsc.resultSet()) {
                // get home location
                HashMap<String, Object> whereh = new HashMap<String, Object>();
                whereh.put("tardis_id", id);
                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
                Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                if (rsh.resultSet()) {
                    Location home = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                    // fly home
                    TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                    pdd.setLocation(current);
                    pdd.setChameleon(false);
                    pdd.setDirection(COMPASS.SOUTH);
                    pdd.setDematerialise(true);
                    pdd.setHide(false);
                    pdd.setOutside(false);
                    pdd.setSubmarine(rsc.isSubmarine());
                    pdd.setTardisID(id);
                    pdd.setBiome(rsc.getBiome());
                    plugin.getPresetDestroyer().destroyPreset(pdd);
                    // fly my pretties
                    plugin.getGeneralKeeper().setJunkTravelling(true);
                    plugin.getGeneralKeeper().setJunkDestination(home);
                    TARDISMessage.send(sender, "JUNK_RETURN");
                    return true;
                }
            } else {
                TARDISMessage.send(sender, "JUNK_NOT_FOUND");
                return true;
            }
        }
        return true;
    }
}
