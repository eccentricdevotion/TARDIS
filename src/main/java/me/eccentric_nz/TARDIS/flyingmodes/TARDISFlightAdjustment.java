/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

/**
 * The Helmic Orientators are slide controls (some consoles have a keyboard)
 * used by the operator for setting the Space-Time Coordinates. The coordinates
 * must take into account the motion of objects relative to a TARDIS' current
 * location, gravity's distortion of the Space-Time Vortex, and the exact time
 * the operator wishes to travel to.
 *
 * @author eccentric_nz
 */
public class TARDISFlightAdjustment {

    private final TARDIS plugin;
    private final List<Integer> angles;

    public TARDISFlightAdjustment(TARDIS plugin) {
        this.plugin = plugin;
        this.angles = Arrays.asList(0, 45, 90, 135, 180, 225, 270, 315);
    }

    public Location getLocation(TARDISMaterialisationData data, int r) {
        Location final_location;
        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
        Location adjusted_location = data.getLocation().clone();
        // randomise the direction
        Collections.shuffle(angles);
        for (Integer a : angles) {
            int wx = (int) (adjusted_location.getX() + r * Math.cos(a)); // x = cx + r * cos(a)
            int wz = (int) (adjusted_location.getZ() + r * Math.sin(a)); // z = cz + r * sin(a)
            adjusted_location.setX(wx);
            adjusted_location.setZ(wz);
            boolean bool = true;
            int y;
            if (adjusted_location.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                y = plugin.getUtils().getHighestNetherBlock(adjusted_location.getWorld(), wx, wz);
            } else {
                y = adjusted_location.getWorld().getHighestBlockAt(adjusted_location).getY();
            }
            adjusted_location.setY(y);
            if (adjusted_location.getBlock().getRelative(BlockFace.DOWN).isLiquid() && !plugin.getConfig().getBoolean("travel.land_on_water") && !data.isSubmarine()) {
                bool = false;
            }
            if (bool) {
                Location sub = null;
                boolean safe;
                if (data.isSubmarine()) {
                    sub = tt.submarine(adjusted_location.getBlock(), data.getDirection());
                    safe = (sub != null);
                } else {
                    int[] start = tt.getStartLocation(adjusted_location, data.getDirection());
                    safe = (tt.safeLocation(start[0], y, start[2], start[1], start[3], adjusted_location.getWorld(), data.getDirection()) < 1);
                }
                if (safe) {
                    final_location = (data.isSubmarine()) ? sub : adjusted_location;
                    if (plugin.getPluginRespect().getRespect(data.getPlayer().getPlayer(), final_location, false)) {
                        return final_location;
                    }
                }
            }
        }
        return data.getLocation();
    }
}
