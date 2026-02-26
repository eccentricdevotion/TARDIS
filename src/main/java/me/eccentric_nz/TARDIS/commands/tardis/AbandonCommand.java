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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * @author eccentric_nz
 */
public class AbandonCommand {

    private final TARDIS plugin;

    AbandonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static Sign getSign(Location l, COMPASS d, ChameleonPreset p) {
        Sign sign = null;
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case JUNK_MODE -> {
                switch (d) {
                    case EAST -> {
                        signx = 0;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = 0;
                        signz = -1;
                    }
                    default -> {
                        signx = 1;
                        signz = 0;
                    }
                }
            }
            case GRAVESTONE -> {
                signx = 0;
                signz = 0;
            }
            case TORCH -> {
                switch (d) {
                    case EAST -> {
                        signx = -1;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = -1;
                    }
                    case WEST -> {
                        signx = 1;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = 1;
                    }
                }
            }
            case TOILET -> {
                switch (d) {
                    case EAST -> {
                        signx = 1;
                        signz = -1;
                    }
                    case SOUTH -> {
                        signx = 1;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = -1;
                        signz = 1;
                    }
                    default -> {
                        signx = -1;
                        signz = -1;
                    }
                }
            }
            case APPERTURE -> {
                switch (d) {
                    case EAST -> {
                        signx = 1;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = 1;
                    }
                    case WEST -> {
                        signx = -1;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = -1;
                    }
                }
            }
            default -> {
                switch (d) {
                    case EAST -> {
                        signx = -2;
                        signz = 0;
                    }
                    case SOUTH -> {
                        signx = 0;
                        signz = -2;
                    }
                    case WEST -> {
                        signx = 2;
                        signz = 0;
                    }
                    default -> {
                        signx = 0;
                        signz = 2;
                    }
                }
            }
        }
        signy = switch (p) {
            case GAZEBO, JAIL, SHROOM, SWAMP -> 3;
            case TOPSYTURVEY, TOILET, TORCH -> 1;
            case ANGEL, APPERTURE, LAMP -> 0;
            default -> 2;
        };
        Block b = new Location(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz).getBlock();
        if (Tag.WALL_SIGNS.isTagged(b.getType())) {
            sign = (Sign) b.getState();
        }
        return sign;
    }
}
