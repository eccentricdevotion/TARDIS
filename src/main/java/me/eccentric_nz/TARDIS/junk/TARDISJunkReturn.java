/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
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
            DestroyData dd = new DestroyData(plugin, "00000000-aaaa-bbbb-cccc-000000000000");
            dd.setLocation(tjl.getCurrent());
            dd.setDirection(COMPASS.SOUTH);
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSubmarine(false);
            dd.setTardisID(tjl.getId());
            dd.setBiome(tjl.getBiome());
            plugin.getPresetDestroyer().destroyPreset(dd);
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
