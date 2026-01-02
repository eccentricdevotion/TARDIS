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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISBuilderPreview;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.World;

public class TARDISDesktopPreview {

    private final TARDIS plugin;
    private long delay = 5;

    public TARDISDesktopPreview(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void create() {
        // message start
        plugin.getMessenger().send(plugin.getConsole(), TardisModule.TARDIS, "PREVIEW_START");
        // get default world
        String dn = plugin.getConfig().getString("creation.default_world_name", "TARDIS_TimeVortex");
        World world = plugin.getServer().getWorld(dn);
        if (world != null) {
            for (Schematic schematic : Desktops.getBY_NAMES().values()) {
                if (schematic.getPreview() < 0 && !hasPreview(schematic)) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getMessenger().send(plugin.getConsole(), TardisModule.TARDIS, "PREVIEW_SCHM", schematic.getPermission());
                        TARDISBuilderPreview builder = new TARDISBuilderPreview(plugin, schematic, world);
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
                        builder.setTask(task);
                    }, delay);
                    delay += 1200;
                }
            }
            // message finish
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getMessenger().send(plugin.getConsole(), TardisModule.TARDIS, "PREVIEW_END"), delay);
        }
    }

    private boolean hasPreview(Schematic schematic) {
        ResultSetTransmat rst = new ResultSetTransmat(plugin, schematic.getPreview(), schematic.getPermission());
        return rst.resultSet();
    }
}
