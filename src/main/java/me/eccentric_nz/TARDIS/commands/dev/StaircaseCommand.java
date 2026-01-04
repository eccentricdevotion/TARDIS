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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StaircaseCommand {

    private final Slab slab = (Slab) Material.STONE_BRICK_SLAB.createBlockData();
    private final List<Vector> vectors = List.of(
            new Vector(9, 0, 3),
            new Vector(11, 0, 4),
            new Vector(12, 1, 5),
            new Vector(13, 1, 7),
            new Vector(13, 2, 9),
            new Vector(12, 2, 11),
            new Vector(11, 3, 12),
            new Vector(9, 3, 13),
            new Vector(7, 4, 13),
            new Vector(5, 4, 12),
            new Vector(4, 5, 11),
            new Vector(3, 5, 9),
            new Vector(3, 6, 7),
            new Vector(4, 6, 5),
            new Vector(5, 7, 4),
            new Vector(7, 7, 3),
            new Vector(9, 8, 3),
            new Vector(11, 8, 4),
            new Vector(12, 9, 5),
            new Vector(13, 9, 7),
            new Vector(13, 10, 9),
            new Vector(12, 10, 11),
            new Vector(11, 11, 12),
            new Vector(9, 11, 13),
            new Vector(7, 12, 13),
            new Vector(5, 12, 12),
            new Vector(4, 13, 11),
            new Vector(3, 13, 9),
            new Vector(3, 14, 7),
            new Vector(4, 14, 5),
            new Vector(5, 15, 4),
            new Vector(7, 15, 3)
    );

    private final List<Vector> smaller = List.of(
            new Vector(9, 0, 5),
            new Vector(10, 0, 6),
            new Vector(11, 1, 7),
            new Vector(11, 1, 8),
            new Vector(11, 2, 9),
            new Vector(10, 2, 10),
            new Vector(9, 3, 11),
            new Vector(8, 3, 11),
            new Vector(7, 4, 11),
            new Vector(6, 4, 10),
            new Vector(5, 5, 9),
            new Vector(5, 5, 8),
            new Vector(5, 6, 7),
            new Vector(6, 6, 6),
            new Vector(7, 7, 5),
            new Vector(8, 7, 5),
            new Vector(9, 8, 5),
            new Vector(10, 8, 6),
            new Vector(11, 9, 7),
            new Vector(11, 9, 8),
            new Vector(11, 10, 9),
            new Vector(10, 10, 10),
            new Vector(9, 11, 11),
            new Vector(8, 11, 11),
            new Vector(7, 12, 11),
            new Vector(6, 12, 10),
            new Vector(5, 13, 9),
            new Vector(5, 13, 8),
            new Vector(5, 14, 7),
            new Vector(6, 14, 6),
            new Vector(6, 15, 5),
            new Vector(7, 15, 5)
    );

    public boolean spiral(Player player) {
        Location location = player.getLocation().clone();
        Chunk chunk = location.getChunk();
        int x = (chunk.getX() * 16);
        int z = (chunk.getZ() * 16);
        int y = location.getBlockY();
        Block block = location.getWorld().getBlockAt(x, y, z);
        walls(block.getLocation().clone().add(8, 0, 8));
        build(block.getLocation().add(0.5d, 0, 0.5d), false);
        return true;
    }

    public boolean smaller(Player player) {
        Location location = player.getLocation().clone();
        Chunk chunk = location.getChunk();
        int x = (chunk.getX() * 16);
        int z = (chunk.getZ() * 16);
        int y = location.getBlockY();
        Block block = location.getWorld().getBlockAt(x, y, z);
        walls(block.getLocation().clone().add(8, 0, 8));
        build(block.getLocation().add(0.5d, 0, 0.5d), true);
        return true;
    }

    private void build(Location location, boolean bool) {
        for (int i = 0; i < 32; i++) {
            Vector v = bool ? smaller.get(i) : vectors.get(i);
            location.add(v.getX(), v.getY(), v.getZ());
            threeByThree(location.getBlock(), i);
            location.subtract(v.getX(), v.getY(), v.getZ());
        }
    }

    public boolean mark(Player player) {
        Location location = player.getLocation().clone();
        Chunk chunk = location.getChunk();
        int x = (chunk.getX() * 16);
        int z = (chunk.getZ() * 16);
        int y = location.getBlockY();
        Block block = location.getWorld().getBlockAt(x, y, z);
        walls(block.getLocation().clone().add(8, 0, 8));
        placeMarkers(block.getLocation().add(0.5d, 0, 0.5d));
        return true;
    }

    private void placeMarkers(Location location) {
        for (int i = 0; i < 32; i++) {
            Vector v = vectors.get(i);
            location.add(v.getX(), v.getY(), v.getZ());
            location.getBlock().setType(Material.VERDANT_FROGLIGHT);
            location.subtract(v.getX(), v.getY(), v.getZ());
        }
    }

    private void threeByThree(Block block, int i) {
        slab.setType((i % 2 == 1) ? Slab.Type.TOP : Slab.Type.BOTTOM);
        block.setBlockData(slab);
        for (BlockFace face : TARDIS.plugin.getGeneralKeeper().getSurrounding()) {
            slab.setType((i % 2 == 1) ? Slab.Type.TOP : Slab.Type.BOTTOM);
            if (!block.getRelative(face).getType().isAir()) {
                slab.setType(Slab.Type.DOUBLE);
            }
            block.getRelative(face).setBlockData(slab);
        }
    }

    private void walls(Location loc) {
        for (int y = 0; y < 16; y++) {
            for (int x = -7; x < 8; x++) {
                for (int z = -7; z < 8; z++) {
                    loc.add(x, y, z);
                    if ((x == 0 || z == 0)) {
                        loc.getBlock().setType((y == 5) ? Material.BLACK_TERRACOTTA : Material.ORANGE_TERRACOTTA);
                    } else {
                        loc.getBlock().setType(Material.PURPLE_TERRACOTTA);
                    }
                    loc.subtract(x, y, z);
                }
            }
        }
        for (int y = 0; y < 16; y++) {
            for (int x = -6; x < 7; x++) {
                for (int z = -6; z < 7; z++) {
                    loc.add(x, y, z);
                    loc.getBlock().setType(Material.AIR);
                    loc.subtract(x, y, z);
                }
            }
        }
    }

    public boolean scan(Player player) {
        Location location = player.getLocation().clone();
        Chunk chunk = location.getChunk();
        int sx = (chunk.getX() * 16);
        int sz = (chunk.getZ() * 16);
        int sy = location.getBlockY();
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (location.getWorld().getBlockAt(sx + x, sy + y, sz + z).getType() == Material.PEARLESCENT_FROGLIGHT) {
                        builder.append("new Vector(").append(x).append(", ").append(y).append(", ").append(z).append("),").append("\n");
                    }
                }
            }
        }
        File file = new File(TARDIS.plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + "staircase.java");
        // save to file
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                bw.write(builder.toString());
            }
        } catch (IOException ignored) { }
        return true;
    }
}
