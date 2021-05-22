package me.eccentric_nz.tardis.maze;

import me.eccentric_nz.tardis.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public final class TARDISMazeBuilder {

	private final boolean[][] maze;
	private final BlockData log = Material.DARK_OAK_LOG.createBlockData();
	private final BlockData leaves = Material.DARK_OAK_LEAVES.createBlockData();
	private final int size;
	private final Location location;

	public TARDISMazeBuilder(boolean[][] maze, Location location) {
		this.maze = maze;
		this.location = location;
		size = this.maze.length;
	}

	public void build(boolean reconfigure) {
		World world = location.getWorld();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		// create walls
		for (int h = 0; h < 4; h++) {
			for (int w = 0; w < size; w++) {
				for (int d = 0; d < size; d++) {
					Block block = world.getBlockAt(x + w, y + h, z + d);
					if (maze[w][d]) {
						block.setBlockData(h < 2 ? log : leaves);
					} else {
						block.setBlockData(TARDISConstants.AIR);
					}
				}
			}
		}
		// make openings
		for (int r = 0; r < 2; r++) {
			world.getBlockAt(x + 10, y + r, z + 5).setBlockData(TARDISConstants.AIR);
			world.getBlockAt(x + 5, y + r, z).setBlockData(TARDISConstants.AIR);
			world.getBlockAt(x, y + r, z + 5).setBlockData(TARDISConstants.AIR);
			world.getBlockAt(x + 5, y + r, z + 10).setBlockData(TARDISConstants.AIR);
		}
		if (reconfigure) {
			BlockData grass = Material.GRASS_BLOCK.createBlockData();
			for (int w = 0; w < size; w++) {
				for (int d = 0; d < size; d++) {
					Block block = world.getBlockAt(x + w, y - 1, z + d);
					block.setBlockData(grass);
				}
			}
		}
	}
}
