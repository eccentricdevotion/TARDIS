package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.maze.TARDISMazeBuilder;
import me.eccentric_nz.TARDIS.maze.TARDISMazeGenerator;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MazeAction {

    private final TARDIS plugin;

    public MazeAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void reconfigure(int type, Player player, int id, Location location) {
        switch (type) {
            case 40 -> // WEST
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in a northerly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockX() < location.getBlockX()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            case 41 -> // NORTH
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in a westerly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockZ() < location.getBlockZ()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            case 42 -> // SOUTH
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in an easterly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockZ() > location.getBlockZ()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            case 43 -> // EAST
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // has player moved out of the maze in a southerly direction
                        Location playerLocation = player.getLocation();
                        if (playerLocation.getBlockX() > location.getBlockX()) {
                            // reconfigure maze
                            reconfigureMaze(id);
                        }
                    }, 20L);
            default -> {
            }
        }
    }

    private void reconfigureMaze(int id) {
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        wherec.put("type", 44);
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
        if (rsc.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                TARDISMazeGenerator generator = new TARDISMazeGenerator();
                generator.makeMaze();
                TARDISMazeBuilder builder = new TARDISMazeBuilder(generator.getMaze(), location);
                builder.build(true);
            }
        }
    }
}
