/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.monitor;

import java.awt.Color;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SnapshotRenderer extends MapRenderer {

    private final Location location;
    private final int distance;

    public SnapshotRenderer(Location location, int distance) {
        super();
        this.location = location;
        this.distance = distance;
    }

    @Override
    public void render(MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (map.isLocked()) {
            return;
        }
        // get pitch and yaw to calculate ray trace directions
        double pitch = -Math.toRadians(location.getPitch());
        double yaw = Math.toRadians(location.getYaw() + 90);
        World world = location.getWorld();
        Color[][] canvasColours = new Color[128][128];
        // loop through every pixel on map
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                // calculate ray rotations
                double yrotate = -((y) * .9 / 128 - .45);
                double xrotate = ((x) * .9 / 128 - .45);
                Vector rayTraceVector = new Vector(Math.cos(yaw + xrotate) * Math.cos(pitch + yrotate), Math.sin(pitch + yrotate), Math.sin(yaw + xrotate) * Math.cos(pitch + yrotate));
                RayTraceResult result = world.rayTraceBlocks(location, rayTraceVector, distance);
                // colour change for liquids
                RayTraceResult liquidResult = world.rayTraceBlocks(location, rayTraceVector, distance, FluidCollisionMode.ALWAYS, false);
                double[] dye = new double[]{1, 1, 1}; // values colour is multiplied by
                if (liquidResult != null) {
                    if (liquidResult.getHitBlock().getType().equals(Material.WATER)) {
                        dye = new double[]{.5, .5, 1};
                    }
                    if (liquidResult.getHitBlock().getType().equals(Material.LAVA)) {
                        dye = new double[]{1, .3, .3};
                    }
                }
                if (result != null) {
                    byte lightLevel = result.getHitBlock().getRelative(result.getHitBlockFace()).getLightLevel();
                    if (lightLevel > 0) {
                        for (int i = 0; i < dye.length; i++) {
                            dye[i] = dye[i] * (lightLevel / 15.0);
                        }
                    }
                    Color color = MaterialColour.colourFromType(result.getHitBlock(), dye);
                    canvas.setPixelColor(x, y, color);
                    canvasColours[x][y] = color;
                } else if (liquidResult != null) {
                    // set map pixel to colour of liquid block found
                    Color color = MaterialColour.colourFromType(liquidResult.getHitBlock(), new double[]{1, 1, 1});
                    canvas.setPixelColor(x, y, color);
                    canvasColours[x][y] = color;
                } else {
                    Color c = Color.gray;
                    // no block was hit, so we will assume we are looking at the distance
                    if (!world.getName().contains("TARDIS")) {
                        switch (world.getEnvironment()) {
                            case NETHER -> {
                                switch (location.getBlock().getBiome()) {
                                    case NETHER_WASTES -> c = new Color(50, 15, 15);
                                    case BASALT_DELTAS -> c = new Color(90, 80, 95);
                                    case CRIMSON_FOREST -> c = new Color(35, 5, 5);
                                    case WARPED_FOREST -> c = new Color(10, 10, 50);
                                    case SOUL_SAND_VALLEY -> c = new Color(35, 90, 80);
                                }
                            }
                            case THE_END ->  c = new Color(30, 20, 50);
                            default -> {
                                // get time of day
                                long ticks = world.getTime();
                                if (world.getName().equals("skaro")) {
                                    c = SkyColour.getSkaroFromTime(ticks);
                                } else if (world.getName().equals("gallifrey")) {
                                    c = SkyColour.getGallifreyFromTime(ticks);
                                } else {
                                    c = SkyColour.getNormalFromTime(ticks);
                                }
                            }
                        }
                    }
                    canvas.setPixelColor(x, y, c);
                    canvasColours[x][y] = c;
                }
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(TARDIS.plugin, () -> new SnapshotStorage().store(map.getId(), canvasColours));
        map.setLocked(true);
    }
}
