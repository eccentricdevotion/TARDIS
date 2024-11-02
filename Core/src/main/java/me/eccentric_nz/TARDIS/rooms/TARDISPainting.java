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
package me.eccentric_nz.TARDIS.rooms;

import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class TARDISPainting {

    public static Location calculatePosition(Art art, BlockFace facing, Location loc) {
        switch (art.getKey().getKey()) {
            // 1x2
            case "graham", "wanderer" -> {
                return loc.clone().add(0, -1, 0);
            }
            // 2x1 & 4x3
            case "creebet", "courbet", "pool", "sea", "sunset", "donkey_kong", "skeleton" -> {
                if (facing == BlockFace.WEST) {
                    return loc.clone().add(0, 0, -1);
                } else if (facing == BlockFace.SOUTH) {
                    return loc.clone().add(-1, 0, 0);
                }
                return loc;
            }
            // 2x2, 4x2 & 4x4
            case "bust", "match", "skull_and_roses", "stage", "void", "wither", "fighters", "burning_skull", "pigscene", "pointer", "earth", "wind", "water", "fire" -> {
                if (facing == BlockFace.WEST) {
                    return loc.clone().add(0, -1, -1);
                } else if (facing == BlockFace.SOUTH) {
                    return loc.clone().add(-1, -1, 0);
                }
                return loc.add(0, -1, 0);
            }
            // 1x1 or unsupported artwork
            default -> {
                return loc;
            }
        }
    }

    public static Location calculatePosition(String art, BlockFace facing, Location loc) {
        switch (art) {
            // 1x2
            case "eye_chart" -> {
                return loc.clone().add(0, -1, 0);
            }
            // 2x1 & 4x3
            case "magnatise", "melt", "periodic_table", "spectacles", "world" -> {
                if (facing == BlockFace.WEST) {
                    return loc.clone().add(0, 0, -1);
                } else if (facing == BlockFace.SOUTH) {
                    return loc.clone().add(-1, 0, 0);
                }
                return loc;
            }
            // 2x2, 4x2 & 4x4
            case "aorta", "beaker", "chemistry", "gallifrey_falls_no_more", "lava", "pi", "sulphur" -> {
                if (facing == BlockFace.WEST) {
                    return loc.clone().add(0, -1, -1);
                } else if (facing == BlockFace.SOUTH) {
                    return loc.clone().add(-1, -1, 0);
                }
                return loc.add(0, -1, 0);
            }
            // 1x1 or unsupported artwork
            default -> {
                return loc;
            }
        }
    }
}
