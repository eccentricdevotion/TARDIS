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
package me.eccentric_nz.TARDIS.rooms;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.block.BlockFace;

public class TARDISPainting {

    static Registry<Art> variants = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT);

    public static Location calculatePosition(Art art, BlockFace facing, Location loc) {
        switch (variants.getKey(art).getKey()) {
            // 1x2 & 3x4, 3x2
            case "backyard", "exploding_tardis", "eye_chart", "graham", "prairie_ride", "pond", "wanderer" -> {
                return loc.clone().add(0, -1, 0);
            }
            // 2x1 & 4x3
            case "creebet", "courbet", "donkey_kong", "magnatise", "melt", "periodic_table", "pool", "sea", "skeleton",
                 "spectacles", "sunset", "world" -> {
                if (facing == BlockFace.WEST) {
                    return loc.clone().add(0, 0, -1);
                } else if (facing == BlockFace.SOUTH) {
                    return loc.clone().add(-1, 0, 0);
                }
                return loc;
            }
            // 2x2, 4x2 & 4x4
            case "aorta", "baroque", "beaker", "burning_skull", "bust", "changing", "chemistry", "earth", "fighters",
                 "finding", "fire", "gallifrey_falls_no_more", "humble", "lava", "lowmist", "match", "orb", "passage",
                 "pi", "pigscene", "pointer", "skull_and_roses", "stage", "sulphur", "unpacked", "void", "water",
                 "wind", "wither" -> {
                if (facing == BlockFace.WEST) {
                    return loc.clone().add(0, -1, -1);
                } else if (facing == BlockFace.SOUTH) {
                    return loc.clone().add(-1, -1, 0);
                }
                return loc.add(0, -1, 0);
            }
            // 1x1 "alban", "aztec", "aztec2", "bomb", "kebab", "meditative, "plant", "wasteland""
            // 3x3 "bouquet", "cavebird", "cotan", "dennis", "endboss", "fern", "owlemons", "sunflowers", "tides"
            // or unsupported artwork
            default -> {
                return loc;
            }
        }
    }

//    public static Location calculatePosition(String art, BlockFace facing, Location loc) {
//        switch (art) {
//            // 1x2
//            case "eye_chart" -> {
//                return loc.clone().add(0, -1, 0);
//            }
//            // 2x1 & 4x3
//            case "magnatise", "melt", "periodic_table", "spectacles", "world" -> {
//                if (facing == BlockFace.WEST) {
//                    return loc.clone().add(0, 0, -1);
//                } else if (facing == BlockFace.SOUTH) {
//                    return loc.clone().add(-1, 0, 0);
//                }
//                return loc;
//            }
//            // 2x2, 4x2 & 4x4
//            case "aorta", "beaker", "chemistry", "gallifrey_falls_no_more", "lava", "pi", "sulphur" -> {
//                if (facing == BlockFace.WEST) {
//                    return loc.clone().add(0, -1, -1);
//                } else if (facing == BlockFace.SOUTH) {
//                    return loc.clone().add(-1, -1, 0);
//                }
//                return loc.add(0, -1, 0);
//            }
//            // 1x1 or unsupported artwork
//            default -> {
//                return loc;
//            }
//        }
//    }
}
