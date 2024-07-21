/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardissonicblaster;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicBlasterAction {

    private final TARDIS plugin;

    public TARDISSonicBlasterAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    // TODO power should affect area and distance
    public void blast(Location target, COMPASS direction, double angle, double distance, double power, UUID uuid) {
        if (distance > plugin.getBlasterSettings().getMaxUsableDistance()) {
            return;
        }
        switch ((int) distance) {
            case 1 -> {
                // 1x2x13
                drill(target, direction, angle, uuid);
            }
            case 2 -> {
                // 2x2x6
            }
            case 3 -> {
                // 3x3x3
            }
            case 4 -> {
                // 4x4 + 3x3
            }
            default -> {
                // 5x5x1
            }
        }
        plugin.getBlasterSettings().getIsBlasting().remove(uuid);
    }

    private void drill(Location target, COMPASS direction, double angle, UUID uuid) {
        int max_blocks = plugin.getBlasterConfig().getInt("max_blocks");
        int block_count = 0;
        int depth = determineHorizontalSectionDepth(angle);
        System.out.println("depth: " + depth);
        int ax = getAddX(direction);
        int az = getAddZ(direction);
        int x = target.getBlockX();
        int y = target.getBlockY();
        int z = target.getBlockZ();
        int l = 0;
        World w = target.getWorld();
        if (depth == 1) {
            // vertical
        } else {
            // horizontal
            end:
            while (block_count <= max_blocks) {
                for (int i = 0; i < depth; i++) {
                    // calculate x,z
                    int xx = x + (i * ax);
                    int yy = y - l;
                    int zz = z + (i * az);
//                    System.out.println("x:" + xx + ", y:" + yy + ", z:" + zz);
                    w.getBlockAt(xx, yy, zz).setType(Material.AIR);
                    w.getBlockAt(xx, yy - 1, zz).setType(Material.AIR);
                    if ((block_count += 2) > max_blocks) {
                        // track last use
                        plugin.getBlasterSettings().getIsBlasting().put(uuid, System.currentTimeMillis());
                        break end;
                    }
                }
                // level down
                l++;
                // come back one block
                switch (direction) {
                    case NORTH -> z -= (depth - 1);
                    case EAST -> x += (depth - 1);
                    case SOUTH -> z += (depth - 1);
                    default -> x -= (depth - 1);
                }
            }
        }
    }

    private int determineHorizontalSectionDepth(double angle) {
        if (angle < -52 || angle > 52) {
            return 1;
        } else if ((angle < -44 && angle > -53) || (angle > 44 && angle < 53)) {
            return 2;
        } else if ((angle < -36 && angle > -45) || (angle > 36 && angle < 45)) {
            return 3;
        } else if ((angle < -28 && angle > -37) || (angle > 28 && angle < 37)) {
            return 4;
        } else if ((angle < -20 && angle > -29) || (angle > 20 && angle < 29)) {
            return 5;
        } else if ((angle < -12 && angle > -21) || (angle > 12 && angle < 21)) {
            return 6;
        } else if ((angle < -4 && angle > -13) || (angle > 4 && angle < 13)) {
            return 7;
        } else {
            return 8;
        }
    }

    private int getAddX(COMPASS direction) {
        return switch (direction) {
            case EAST -> 1;
            case WEST -> -1;
            default -> 0;
        };
    }

    private int getAddZ(COMPASS direction) {
        return switch (direction) {
            case SOUTH -> 1;
            case NORTH -> -1;
            default -> 0;
        };
    }
}
