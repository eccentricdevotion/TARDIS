package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TARDISBlockBreakListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISBlockBreakListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        int signx = 0, signz = 0;
        Player player = event.getPlayer();
        String playerNameStr = player.getName();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.WALL_SIGN) {
            // check the text on the sign
            Sign sign = (Sign) block.getState();
            String line1 = sign.getLine(1);
            String line2 = sign.getLine(2);
            String queryCheck;
            Location sign_loc = block.getLocation();
            if (line1.equals("¤fPOLICE") && line2.equals("¤fBOX")) {
                if (player.hasPermission("tardis.delete")) {
                    Block blockbehind = null;
                    byte data = block.getData();
                    if (data == 4) {
                        blockbehind = block.getRelative(BlockFace.SOUTH, 2);
                    }
                    if (data == 5) {
                        blockbehind = block.getRelative(BlockFace.NORTH, 2);
                    }
                    if (data == 3) {
                        blockbehind = block.getRelative(BlockFace.EAST, 2);
                    }
                    if (data == 2) {
                        blockbehind = block.getRelative(BlockFace.WEST, 2);
                    }
                    Block blockDown = blockbehind.getRelative(BlockFace.DOWN, 2);
                    Location bd_loc = blockDown.getLocation();
                    String bd_str = bd_loc.getWorld().getName() + ":" + bd_loc.getBlockX() + ":" + bd_loc.getBlockY() + ":" + bd_loc.getBlockZ();
                    queryCheck = "SELECT * FROM tardis WHERE save = '" + bd_str + "'";
                } else {
                    queryCheck = "SELECT * FROM tardis WHERE owner = '" + playerNameStr + "'";
                }
                occupied:
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(queryCheck);
                    if (rs.next()) {
                        String saveLoc = rs.getString("save");
                        String chunkLoc = rs.getString("chunk");
                        String owner = rs.getString("owner");
                        Constants.SCHEMATIC schm = Constants.SCHEMATIC.valueOf(rs.getString("size"));
                        int id = rs.getInt("tardis_id");
                        Constants.COMPASS d = Constants.COMPASS.valueOf(rs.getString("direction"));

                        // need to check that a player is not currently in the TARDIS (if admin delete - maybe always?)
                        if (player.hasPermission("tardis.delete")) {
                            String queryOccupied = "SELECT player FROM travellers WHERE tardis_id = " + id;
                            ResultSet rsOcc = statement.executeQuery(queryOccupied);
                            if (rsOcc.next()) {
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " You cannot delete this TARDIS as it is occupied!");
                                event.setCancelled(true);
                                sign.update();
                                break occupied;
                            }
                        }
                        // check the sign location
                        Location bb_loc = Constants.getLocationFromDB(saveLoc, yaw, pitch);
                        // get TARDIS direction
                        switch (d) {
                            case EAST:
                                signx = -2;
                                signz = 0;
                                break;
                            case SOUTH:
                                signx = 0;
                                signz = -2;
                                break;
                            case WEST:
                                signx = 2;
                                signz = 0;
                                break;
                            case NORTH:
                                signx = 0;
                                signz = 2;
                                break;
                        }
                        int signy = -2;
                        // if the sign was on the TARDIS destroy the TARDIS!
                        if (sign_loc.getBlockX() == bb_loc.getBlockX() + signx && sign_loc.getBlockY() + signy == bb_loc.getBlockY() && sign_loc.getBlockZ() == bb_loc.getBlockZ() + signz) {
                            int cwx = 0, cwz = 0;
                            // don't drop the sign
                            //block.getDrops().clear();
                            // clear the torch
                            plugin.destroyer.destroyTorch(bb_loc);
                            plugin.destroyer.destroySign(bb_loc, d);
                            // also remove the location of the chunk from chunks table
                            String[] chunkworld = chunkLoc.split(":");
                            World cw = plugin.getServer().getWorld(chunkworld[0]);
                            Environment env = cw.getEnvironment();
                            int restore;
                            switch (env) {
                                case NETHER:
                                    restore = 87;
                                    break;
                                case THE_END:
                                    restore = 121;
                                    break;
                                default:
                                    restore = 1;
                            }
                            String queryDeleteChunk = "DELETE FROM chunks WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDeleteChunk);
                            plugin.destroyer.destroyTARDIS(schm, id, cw, d, restore, playerNameStr);
                            if (cw.getWorldType() == WorldType.FLAT) {
                                // replace stone blocks with AIR
                                plugin.destroyer.destroyTARDIS(schm, id, cw, d, 0, playerNameStr);
                            }
                            plugin.destroyer.destroyBlueBox(bb_loc, d, id);
                            // remove record from tardis table
                            String queryDeleteTardis = "DELETE FROM tardis WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDeleteTardis);
                            // remove doors from doors table
                            String queryDeleteDoors = "DELETE FROM doors WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDeleteDoors);
                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The TARDIS was removed from the world and database successfully.");
                            // remove world guard region protection
                            if (plugin.WorldGuardOnServer && plugin.config.getBoolean("use_worldguard")) {
                                plugin.wgchk.removeRegion(cw, owner);
                            }
                        } else {
                            // cancel the event because it's not the player's TARDIS
                            event.setCancelled(true);
                            sign.update();
                            player.sendMessage(Constants.NOT_OWNER);
                        }
                    } else {
                        event.setCancelled(true);
                        sign.update();
                        player.sendMessage("Don't grief the TARDIS!");
                    }
                    rs.close();
                    statement.close();

                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + " Block Break Listener Error: " + e);
                }
            }
        }
    }
}
