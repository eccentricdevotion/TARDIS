/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.portal;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Cast {

    private final TARDIS plugin;
    private final Location location;

    public Cast(TARDIS plugin, Location location) {
        this.plugin = plugin;
        this.location = location;
    }

    /**
     * Cast a 3D array of BlockData, rotating it depending on the direction the player is facing, and then sending the
     * player a bunch of fake block changes
     *
     * @param uuid    The UUID of the player who is casting
     * @param capture The 3D array of blocks that will be cast
     */
    public void castInterior(UUID uuid, BlockData[][][] capture) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) {
            return;
        }
        CastData castData = plugin.getTrackerKeeper().getCasters().get(uuid);
        if (castData == null) {
            return;
        }
        int layers = capture.length;
        int size = capture[0].length;
        // show blocks
        World world = location.getWorld();
        int bx;
        int bz;
        int sx;
        int sz;
        // get exterior direction
        COMPASS facing = castData.getDirection();
        // rotate capture array depending on exterior direction
        // & adjust start position
        BlockData[][][] rotated;
        switch (facing) {
            case NORTH -> { // towards negative z
                bx = -size / 2;
                bz = -size;
                sx = -4;
                sz = -9;
                rotated = MatrixUtils.rotateToNorth(capture);
            }
            case WEST -> { // towards negative x
                bx = -size;
                bz = -size / 2;
                sx = -9;
                sz = -4;
                rotated = MatrixUtils.rotateToWest(capture);
            }
            case EAST -> { // towards positive x
                bx = 1;
                bz = -size / 2;
                sx = 1;
                sz = -4;
                rotated = MatrixUtils.rotateToEast(capture);
            }
            // SOUTH
            default -> { // towards positive z
                bx = -size / 2;
                bz = 1;
                sx = -4;
                sz = 1;
                rotated = capture;
            }
        }
        int startX = location.getBlockX() + bx;
        int startY = location.getBlockY() - 1;
        int startZ = location.getBlockZ() + bz;
        int restoreX = location.getBlockX() + sx;
        int restoreY = location.getBlockY() - 1;
        int restoreZ = location.getBlockZ() + sz;
        // remember blocks
        Set<Block> restore = new HashSet<>();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                for (int z = 0; z < 9; z++) {
                    int xx = restoreX + x;
                    int yy = restoreY + y;
                    int zz = restoreZ + z;
                    // save block states for restoring
                    restore.add(world.getBlockAt(xx, yy, zz));
                }
            }
        }
        // cast fake blocks
        for (int y = 0; y < layers; y++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    int xx = startX + x;
                    int yy = startY + y;
                    int zz = startZ + z;
                    player.sendBlockChange(new Location(world, xx, yy, zz), rotated[y][x][z]);
                }
            }
        }
        plugin.getTrackerKeeper().getCastRestore().put(uuid, restore);
    }

    /**
     * It restores the blocks that were changed by the casting
     *
     * @param uuid The UUID of the player who is casting
     */
    public void restoreExterior(UUID uuid) {
        Set<Block> restore = plugin.getTrackerKeeper().getCastRestore().get(uuid);
        if (restore != null) {
            for (Block b : restore) {
                b.getState().update();
            }
        }
    }

    public void castRotor(ItemFrame rotor, Player player, Vector offset, COMPASS direction) {
        // adjust offset for exterior direction
        double tx = offset.getX();
        double tz = offset.getZ();
        switch (direction) {
            case EAST -> {
                // tz, -tx
                offset.setX(tz);
                offset.setZ(-tx);
            }
            case WEST -> {
                // -tz, tx
                offset.setX(-tz);
                offset.setZ(tx);
            }
            case NORTH -> {
                // -tx, -tz
                offset.setX(-tx);
                offset.setZ(-tz);
            }
            default -> { // SOUTH
                // no change
            }
        }
        Location frame = location.clone().add(offset);
        // show fake item frame
        int rotorId = plugin.getTardisHelper().castFakeItemFrame(rotor, player, new Vector(frame.getBlockX(), frame.getBlockY(), frame.getBlockZ()));
        // remember id for removal
        plugin.getTrackerKeeper().getRotorRestore().put(player.getUniqueId(), rotorId);
    }
}
