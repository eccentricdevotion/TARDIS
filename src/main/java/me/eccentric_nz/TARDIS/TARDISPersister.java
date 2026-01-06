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

import me.eccentric_nz.TARDIS.arch.ArchPersister;
import me.eccentric_nz.TARDIS.builders.utility.SeedBlockPersister;
import me.eccentric_nz.TARDIS.camera.CameraPersister;
import me.eccentric_nz.TARDIS.database.TimeRotorLoader;
import me.eccentric_nz.TARDIS.desktop.PreviewPersister;
import me.eccentric_nz.TARDIS.flight.FlightPersister;
import me.eccentric_nz.TARDIS.flight.VortexPersister;
import me.eccentric_nz.TARDIS.forcefield.ForceFieldPersister;
import me.eccentric_nz.TARDIS.hads.HadsPersister;
import me.eccentric_nz.TARDIS.move.PortalPersister;
import me.eccentric_nz.TARDIS.rooms.RoomPersister;
import me.eccentric_nz.TARDIS.sensor.SensorTracker;
import me.eccentric_nz.TARDIS.siegemode.SiegePersister;
import me.eccentric_nz.TARDIS.skins.SkinPersister;
import me.eccentric_nz.TARDIS.utility.JunkPlayerPersister;
import me.eccentric_nz.tardisweepingangels.nms.FollowerSaver;

public class TARDISPersister {

    private final TARDIS plugin;

    public TARDISPersister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void load() {
        new PortalPersister(plugin).load();
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new ArchPersister(plugin).checkAll();
        }
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            SiegePersister tsp = new SiegePersister(plugin);
            tsp.loadSiege();
            tsp.loadCubes();
        }
        if (plugin.getConfig().getBoolean("allow.hads")) {
            HadsPersister thp = new HadsPersister(plugin);
            thp.load();
        }
        if (plugin.getConfig().getInt("allow.force_field") > 0) {
            new ForceFieldPersister(plugin).load();
        }
        if (plugin.getConfig().getBoolean("desktop.previews")) {
            new PreviewPersister(plugin).load();
        }
        new TimeRotorLoader(plugin).load();
        new VortexPersister(plugin).load();
        new FlightPersister(plugin).load();
        new CameraPersister(plugin).load();
        new JunkPlayerPersister(plugin).load();
        new SeedBlockPersister(plugin).load();
        // resume any room growing
        new RoomPersister(plugin).resume();
        // load skins
        new SkinPersister(plugin).load();
        // resume recharging
        SensorTracker.restartCharging(plugin);
    }

    public void save() {
        // persist any room growing
        new RoomPersister(plugin).saveProgress();
        new PortalPersister(plugin).save();
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled")) {
            new ArchPersister(plugin).saveAll();
        }
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            new SiegePersister(plugin).saveCubes();
        }
        if (plugin.getConfig().getBoolean("desktop.previews")) {
            new PreviewPersister(plugin).save();
        }
        if (plugin.getConfig().getBoolean("allow.hads")) {
            new HadsPersister(plugin).save();
        }
        new VortexPersister(plugin).save();
        new FlightPersister(plugin).save();
        new CameraPersister(plugin).save();
        if (plugin.getConfig().getInt("allow.force_field") > 0) {
            new ForceFieldPersister(plugin).save();
        }
        new SeedBlockPersister(plugin).save();
        if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
            new FollowerSaver(plugin).persist();
        }
        // save skins
        new SkinPersister(plugin).save();
    }
}
