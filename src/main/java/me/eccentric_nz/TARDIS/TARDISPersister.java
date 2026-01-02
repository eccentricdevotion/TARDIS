/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.builders.utility.TARDISSeedBlockPersister;
import me.eccentric_nz.TARDIS.camera.CameraPersister;
import me.eccentric_nz.TARDIS.database.TARDISTimeRotorLoader;
import me.eccentric_nz.TARDIS.desktop.TARDISPreviewPersister;
import me.eccentric_nz.TARDIS.flight.FlightPersister;
import me.eccentric_nz.TARDIS.flight.TARDISVortexPersister;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceFieldPersister;
import me.eccentric_nz.TARDIS.hads.TARDISHadsPersister;
import me.eccentric_nz.TARDIS.move.TARDISPortalPersister;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomPersister;
import me.eccentric_nz.TARDIS.sensor.SensorTracker;
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
        new TARDISPortalPersister(plugin).load();
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
        if (plugin.getConfig().getBoolean("desktop.previews")) {
            new TARDISPreviewPersister(plugin).load();
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
        // resume recharging
        SensorTracker.restartCharging(plugin);
    }

    public void save() {
        // persist any room growing
        new TARDISRoomPersister(plugin).saveProgress();
        new TARDISPortalPersister(plugin).save();
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new TARDISArchPersister(plugin).saveAll();
        }
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            new TARDISSiegePersister(plugin).saveCubes();
        }
        if (plugin.getConfig().getBoolean("desktop.previews")) {
            new TARDISPreviewPersister(plugin).save();
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
