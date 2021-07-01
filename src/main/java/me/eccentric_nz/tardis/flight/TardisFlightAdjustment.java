/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.travel.TardisTimeTravel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Helmic Orientators are slide controls (some consoles have a keyboard) used by the operator for setting the
 * Space-Time Coordinates. The coordinates must take into account the motion of objects relative to a TARDIS's current
 * location, gravity's distortion of the Space-Time Vortex, and the exact time the operator wishes to travel to.
 *
 * @author eccentric_nz
 */
class TardisFlightAdjustment {

    private final TardisPlugin plugin;
    private final List<Integer> angles;

    TardisFlightAdjustment(TardisPlugin plugin) {
        this.plugin = plugin;
        angles = Arrays.asList(0, 45, 90, 135, 180, 225, 270, 315);
    }

    public Location getLocation(BuildData bd, int r) {
        Location final_location;
        TardisTimeTravel tt = new TardisTimeTravel(plugin);
        Location adjusted_location = bd.getLocation().clone();
        // randomise the direction
        Collections.shuffle(angles);
        for (Integer a : angles) {
            int wx = (int) (adjusted_location.getX() + r * Math.cos(a)); // x = cx + r * cos(a)
            int wz = (int) (adjusted_location.getZ() + r * Math.sin(a)); // z = cz + r * sin(a)
            adjusted_location.setX(wx);
            adjusted_location.setZ(wz);
            boolean bool = true;
            int y;
            if (Objects.requireNonNull(adjusted_location.getWorld()).getEnvironment().equals(World.Environment.NETHER)) {
                y = plugin.getUtils().getHighestNetherBlock(adjusted_location.getWorld(), wx, wz);
            } else {
                y = adjusted_location.getWorld().getHighestBlockAt(adjusted_location).getY();
            }
            adjusted_location.setY(y);
            if (adjusted_location.getBlock().getRelative(BlockFace.DOWN).isLiquid() && !plugin.getConfig().getBoolean("travel.land_on_water") && !bd.isSubmarine()) {
                bool = false;
            }
            if (bool) {
                Location sub = null;
                boolean safe;
                if (bd.isSubmarine()) {
                    sub = tt.submarine(adjusted_location.getBlock(), bd.getDirection());
                    safe = (sub != null);
                } else {
                    int[] start = TardisTimeTravel.getStartLocation(adjusted_location, bd.getDirection());
                    safe = (TardisTimeTravel.safeLocation(start[0], y, start[2], start[1], start[3], adjusted_location.getWorld(), bd.getDirection()) < 1);
                }
                if (safe) {
                    final_location = (bd.isSubmarine()) ? sub : adjusted_location;
                    if (plugin.getPluginRespect().getRespect(final_location, new Parameters(bd.getPlayer().getPlayer(), Flag.getNoMessageFlags()))) {
                        return final_location;
                    }
                }
            }
        }
        return bd.getLocation();
    }
}
