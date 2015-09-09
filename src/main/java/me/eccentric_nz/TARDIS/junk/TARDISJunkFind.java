/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkFind {

    private final TARDIS plugin;

    public TARDISJunkFind(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean find(CommandSender sender) {
        // get current location
        if (!sender.hasPermission("tardis.junk")) {
            TARDISMessage.send(sender, "JUNK_NO_PERM");
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
                String world = rsc.getWorld().getName();
                if (plugin.isMVOnServer()) {
                    world = plugin.getMVHelper().getAlias(rsc.getWorld());
                }
                TARDISMessage.send(sender, "TARDIS_FIND", world + " at x: " + rsc.getX() + " y: " + rsc.getY() + " z: " + rsc.getZ());
                return true;
            } else {
                TARDISMessage.send(sender, "JUNK_NOT_FOUND");
                return true;
            }
        }
        return true;
    }
}
