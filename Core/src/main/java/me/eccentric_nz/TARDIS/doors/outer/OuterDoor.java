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
package me.eccentric_nz.TARDIS.doors.outer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOuterPortal;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class OuterDoor {

    private final TARDIS plugin;
    private final int id;

    public OuterDoor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public Block getMinecraft() {
        // get from door record
        ResultSetOuterPortal resultSetPortal = new ResultSetOuterPortal(plugin, id);
        if (resultSetPortal.resultSet()) {
            Location location = resultSetPortal.getLocation();
            return location.getBlock();
        }
        return null;
    }

    public ArmorStand getDisplay() {
        // get from current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            for (Entity e : rsc.getCurrent().location().getWorld().getNearbyEntities(rsc.getCurrent().location(), 1.0d, 1.0d, 1.0d)) {
                if (e instanceof ArmorStand a && ((CraftArmorStand) a).getHandle() instanceof TARDISArmourStand) {
                    return a;
                }
            }
        }
        return null;
    }
}
