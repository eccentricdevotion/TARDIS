package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.builders.TARDISSeedBlockPersister;
import me.eccentric_nz.TARDIS.camera.CameraPersister;
import me.eccentric_nz.TARDIS.database.TARDISTimeRotorLoader;
import me.eccentric_nz.TARDIS.flight.FlightPersister;
import me.eccentric_nz.TARDIS.flight.TARDISVortexPersister;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceFieldPersister;
import me.eccentric_nz.TARDIS.hads.TARDISHadsPersister;
import me.eccentric_nz.TARDIS.move.TARDISPortalPersister;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomPersister;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegePersister;
import me.eccentric_nz.TARDIS.skins.SkinPersister;
import me.eccentric_nz.TARDIS.utility.TARDISJunkPlayerPersister;
import me.eccentric_nz.tardisweepingangels.nms.FollowerSaver;

public class TARDISPersister {

    private final TARDIS plugin;

    public TARDISPersister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void load() {
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            new TARDISPortalPersister(plugin).load();
        }
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new TARDISArchPersister(plugin).checkAll();
        }
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            TARDISSiegePersister tsp = new TARDISSiegePersister(plugin);
            tsp.loadSiege();
            tsp.loadCubes();
        }
        if (plugin.getConfig().getBoolean("allow.hads")) {
            TARDISHadsPersister thp = new TARDISHadsPersister(plugin);
            thp.load();
        }
        if (plugin.getConfig().getInt("allow.force_field") > 0) {
            new TARDISForceFieldPersister(plugin).load();
        }
        new TARDISTimeRotorLoader(plugin).load();
        new TARDISVortexPersister(plugin).load();
        new FlightPersister(plugin).load();
        new CameraPersister(plugin).load();
        new TARDISJunkPlayerPersister(plugin).load();
        new TARDISSeedBlockPersister(plugin).load();
        // resume any room growing
        new TARDISRoomPersister(plugin).resume();
        // load skins
        new SkinPersister(plugin).load();
    }

    public void save() {
        // persist any room growing
        new TARDISRoomPersister(plugin).saveProgress();
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            new TARDISPortalPersister(plugin).save();
        }
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new TARDISArchPersister(plugin).saveAll();
        }
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            new TARDISSiegePersister(plugin).saveCubes();
        }
        if (plugin.getConfig().getBoolean("allow.hads")) {
            new TARDISHadsPersister(plugin).save();
        }
        new TARDISVortexPersister(plugin).save();
        new FlightPersister(plugin).save();
        new CameraPersister(plugin).save();
        if (plugin.getConfig().getInt("allow.force_field") > 0) {
            new TARDISForceFieldPersister(plugin).save();
        }
        new TARDISSeedBlockPersister(plugin).save();
        if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
            new FollowerSaver(plugin).persist();
        }
        // save skins
        new SkinPersister(plugin).save();
    }
}
