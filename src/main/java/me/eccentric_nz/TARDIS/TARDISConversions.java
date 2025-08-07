/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.ARS.ARSConverter;
import me.eccentric_nz.TARDIS.chameleon.construct.ConstructsConverter;
import me.eccentric_nz.TARDIS.database.converters.*;
import me.eccentric_nz.TARDIS.doors.TARDISInteractionDoorUpdater;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.files.TARDISAllInOneConfigConverter;
import me.eccentric_nz.TARDIS.rooms.eye.EyePopulator;

public class TARDISConversions {

    private final TARDIS plugin;
    private int conversions = 0;

    public TARDISConversions(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void convert() {
        if (!plugin.getConfig().getBoolean("conversions.condenser_materials") || !plugin.getConfig().getBoolean("conversions.player_prefs_materials") || !plugin.getConfig().getBoolean("conversions.block_materials")) {
            TARDISMaterialIDConverter tmic = new TARDISMaterialIDConverter(plugin);
            tmic.checkCondenserData();
            tmic.checkPlayerPrefsData();
            tmic.checkBlockData();
            new TARDISFarmingConverter(plugin).update();
        }
        if (!plugin.getConfig().getBoolean("conversions.block_wall_signs")) {
            new TARDISWallSignConverter(plugin).convertSignBlocks();
            plugin.getConfig().set("conversions.block_wall_signs", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.short_grass")) {
            new TARDISGrassConverter(plugin).checkBlockData();
            plugin.getConfig().set("conversions.short_grass", true);
            conversions++;
        }
        // insert eye records
        if (!plugin.getConfig().getBoolean("conversions.eyes")) {
            new EyePopulator(plugin).insert();
            plugin.getConfig().set("conversions.eyes", true);
            conversions++;
        }
        // update database materials
        if (!plugin.getConfig().getBoolean("conversions.ars_materials")) {
            new ARSConverter(plugin).convertARS();
            plugin.getConfig().set("conversions.ars_materials", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.constructs")) {
            new ConstructsConverter(plugin).convertConstructs();
            plugin.getConfig().set("conversions.constructs", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.controls")) {
            new TARDISControlsConverter(plugin).update();
            plugin.getConfig().set("conversions.controls", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.bind")) {
            new TARDISBindConverter(plugin).update();
            plugin.getConfig().set("conversions.bind", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.icons")) {
            new TARDISSaveIconUpdate(plugin).addIcons();
            plugin.getConfig().set("conversions.icons", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.archive_wall_data")) {
            new TARDISWallConverter(plugin).processArchives();
            plugin.getConfig().set("conversions.archive_wall_data", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.legacy_default")) {
            new TARDISLegacyConverter(plugin).setOriginal();
            plugin.getConfig().set("conversions.legacy_default", true);
            conversions++;
        }
        if (!plugin.getConfig().getBoolean("conversions.all_in_one.helper")) {
            if (new TARDISAllInOneConfigConverter(plugin).transferConfig(TardisModule.HELPER)) {
                plugin.getConfig().set("conversions.all_in_one.helper", true);
                conversions++;
            }
        }
        if (!plugin.getConfig().getBoolean("conversions.interior_door_id")) {
            if (new TARDISInteractionDoorUpdater(plugin).addIds()) {
                plugin.getConfig().set("conversions.interior_door_id", true);
                conversions++;
            }
        }
        if (conversions > 0) {
            plugin.saveConfig();
        }
    }
}
