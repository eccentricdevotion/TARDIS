/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
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
        TARDISJunkLocation tjl = new TARDISJunkLocation(plugin);
        if (tjl.isNotHome()) {
            Location home = tjl.getHome();
            // fly home
            TARDISMaterialisationData pdd = new TARDISMaterialisationData();
            pdd.setLocation(tjl.getCurrent());
            pdd.setChameleon(false);
            pdd.setDirection(COMPASS.SOUTH);
            pdd.setDematerialise(true);
            pdd.setHide(false);
            pdd.setOutside(false);
            pdd.setSubmarine(false);
            pdd.setTardisID(tjl.getId());
            pdd.setBiome(tjl.getBiome());
            plugin.getPresetDestroyer().destroyPreset(pdd);
            // fly my pretties
            plugin.getGeneralKeeper().setJunkTravelling(true);
            plugin.getGeneralKeeper().setJunkDestination(home);
            TARDISMessage.send(sender, "JUNK_RETURN");
            return true;
        } else {
            TARDISMessage.send(sender, "JUNK_AT_HOME");
            return true;
        }
    }
}
