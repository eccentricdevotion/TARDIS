package me.eccentric_nz.TARDIS.maze;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public final class TARDISMazeBuilder {

    private final boolean[][] maze;
    private final BlockData wall;
    private final int size;
    private final int height;
    private final World world;
    private final Location location;

    public TARDISMazeBuilder(boolean[][] maze, World world, Location location, Material wall, int height) {
        this.maze = maze;
        this.world = world;
        this.location = location;
        this.wall = wall.createBlockData();
        size = this.maze.length;
        this.height = height;
    }

    public void build() {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < size; w++) {
                for (int d = 0; d < size; d++) {
                    Block block = world.getBlockAt(x + w, y + h, z + d);
                    if (maze[w][d]) {
                        block.setBlockData(wall);
                    } else {
                        block.setBlockData(TARDISConstants.AIR);
                    }
                }
            }
        }
    }
}
