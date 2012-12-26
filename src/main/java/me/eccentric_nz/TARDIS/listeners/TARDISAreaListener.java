package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TARDISAreaListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISAreaListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAreaInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();
        int cx = 0, cy = 0, cz = 0;

        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (plugin.trackName.containsKey(playerNameStr) && !plugin.trackBlock.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.ta.areaCheckInExisting(block_loc)) {
                    String locStr = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockZ();
                    plugin.trackBlock.put(playerNameStr, locStr);
                    player.sendMessage(plugin.pluginName + " You have 60 seconds to select the area end block - use the " + ChatColor.GREEN + "/TARDIS admin area end" + ChatColor.RESET + " command.");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.trackName.remove(playerNameStr);
                            plugin.trackBlock.remove(playerNameStr);
                        }
                    }, 1200L);
                } else {
                    player.sendMessage(plugin.pluginName + " That block is inside an already defined area! Try somewhere else.");
                }
            } else if (plugin.trackBlock.containsKey(playerNameStr) && plugin.trackEnd.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.ta.areaCheckInExisting(block_loc)) {
                    String[] firstblock = plugin.trackBlock.get(playerNameStr).split(":");
                    if (!block_loc.getWorld().getName().equals(firstblock[0])) {
                        player.sendMessage(plugin.pluginName + ChatColor.RED + " Area start and end blocks must be in the same world! Try again");
                    }
                    int minx, minz, maxx, maxz;
                    if (plugin.utils.parseNum(firstblock[1]) < block_loc.getBlockX()) {
                        minx = plugin.utils.parseNum(firstblock[1]);
                        maxx = block_loc.getBlockX();
                    } else {
                        minx = block_loc.getBlockX();
                        maxx = plugin.utils.parseNum(firstblock[1]);
                    }
                    if (plugin.utils.parseNum(firstblock[2]) < block_loc.getBlockZ()) {
                        minz = plugin.utils.parseNum(firstblock[2]);
                        maxz = block_loc.getBlockZ();
                    } else {
                        minz = block_loc.getBlockZ();
                        maxz = plugin.utils.parseNum(firstblock[2]);
                    }
                    String n = plugin.trackName.get(playerNameStr);
                    try {
                        Connection connection = service.getConnection();
                        PreparedStatement psArea = connection.prepareStatement("INSERT INTO areas (area_name, world, minx, minz, maxx, maxz) VALUES (?,?,?,?,?,?)");
                        psArea.setString(1, n);
                        psArea.setString(2, firstblock[0]);
                        psArea.setInt(3, minx);
                        psArea.setInt(4, minz);
                        psArea.setInt(5, maxx);
                        psArea.setInt(6, maxz);
                        psArea.executeUpdate();
                        player.sendMessage(plugin.pluginName + " The area [" + plugin.trackName.get(playerNameStr) + "] was saved successfully");
                        plugin.trackName.remove(playerNameStr);
                        plugin.trackBlock.remove(playerNameStr);
                        plugin.trackEnd.remove(playerNameStr);
                        psArea.close();
                    } catch (SQLException e) {
                        plugin.console.sendMessage(plugin.pluginName + " Area save error: " + e);
                    }
                } else {
                    player.sendMessage(plugin.pluginName + " That block is inside an already defined area! Try somewhere else.");
                }
            }
        }
    }
}