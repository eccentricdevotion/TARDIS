package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISAreaCommands implements CommandExecutor {

    private TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

    public TARDISAreaCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisarea then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisarea")) {
            if (args.length == 0) {
                return false;
            }
            if (player == null) {
                sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " This command can only be run by a player");
                return false;
            }
            if (args[0].equals("start")) {
                // check name is unique and acceptable
                if (args.length < 2 || !args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "That doesn't appear to be a valid area name (it may be too long)" + ChatColor.GREEN + " /tardis admin area start [area_name_goes_here]");
                    return false;
                }
                String queryName = "SELECT area_name FROM areas";
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rsName = statement.executeQuery(queryName);
                    while (rsName.next()) {
                        if (rsName.getString("area_name").equals(args[1])) {
                            sender.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Area name already in use!");
                            return false;
                        }
                    }
                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't get area names: " + e);
                }
                plugin.trackName.put(player.getName(), args[1]);
                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Click the area start block to save its position.");
                return true;
            }
            if (args[0].equals("end")) {
                if (!plugin.trackBlock.containsKey(player.getName())) {
                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " You haven't selected an area start block!");
                    return false;
                }
                plugin.trackEnd.put(player.getName(), "end");
                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Click the area end block to complete the area.");
                return true;
            }
            if (args[0].equals("remove")) {
                String queryRemove = "DELETE FROM areas WHERE area_name = '" + args[1] + "'";
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(queryRemove);
                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Area [" + args[1] + "] deleted!");
                    return true;
                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't delete area: " + e);
                }
            }
            if (args[0].equals("show")) {
                String queryGetArea = "SELECT * FROM areas WHERE area_name = '" + args[1] + "'";
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rsArea = statement.executeQuery(queryGetArea);
                    if (!rsArea.next()) {
                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + "Could not find area [" + args[1] + "]! Did you type the name correctly?");
                        return false;
                    }
                    int mix = rsArea.getInt("minx");
                    int miz = rsArea.getInt("minz");
                    int max = rsArea.getInt("maxx");
                    int maz = rsArea.getInt("maxz");
                    World w = plugin.getServer().getWorld(rsArea.getString("world"));
                    final Block b1 = w.getHighestBlockAt(mix, miz).getRelative(BlockFace.UP);
                    b1.setTypeId(89);
                    final Block b2 = w.getHighestBlockAt(mix, maz).getRelative(BlockFace.UP);
                    b2.setTypeId(89);
                    final Block b3 = w.getHighestBlockAt(max, miz).getRelative(BlockFace.UP);
                    b3.setTypeId(89);
                    final Block b4 = w.getHighestBlockAt(max, maz).getRelative(BlockFace.UP);
                    b4.setTypeId(89);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            b1.setTypeId(0);
                            b2.setTypeId(0);
                            b3.setTypeId(0);
                            b4.setTypeId(0);
                        }
                    }, 300L);
                    return true;
                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + "Couldn't delete area: " + e);
                }
            }
        }
        return false;
    }
}