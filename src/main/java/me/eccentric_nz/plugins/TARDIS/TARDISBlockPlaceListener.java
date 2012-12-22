package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TARDISBlockPlaceListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    public static final List<String> MIDDLE_BLOCKS = Arrays.asList(new String[]{"LAPIS_BLOCK", "STONE", "DIRT", "WOOD", "SANDSTONE", "WOOL", "BRICK", "NETHERRACK", "SOUL_SAND", "SMOOTH_BRICK", "HUGE_MUSHROOM_1", "HUGE_MUSHROOM_2", "ENDER_STONE"});

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {

        Block block = event.getBlockPlaced();
        // only listen for redstone torches
        if (block.getType() == Material.REDSTONE_TORCH_ON) {
            Block blockBelow = block.getRelative(BlockFace.DOWN);
            int middle_id = blockBelow.getTypeId();
            byte middle_data = blockBelow.getData();
            Block blockBottom = blockBelow.getRelative(BlockFace.DOWN);
            // only continue if the redstone torch is placed on top of [JUST ABOUT ANY] BLOCK on top of an IRON/GOLD/DIAMOND_BLOCK
            if (MIDDLE_BLOCKS.contains(blockBelow.getType().toString()) && (blockBottom.getType() == Material.IRON_BLOCK || blockBottom.getType() == Material.GOLD_BLOCK || blockBottom.getType() == Material.DIAMOND_BLOCK)) {
                Constants.SCHEMATIC schm;
                Player player = event.getPlayer();
                switch (blockBottom.getType()) {
                    case GOLD_BLOCK:
                        if (player.hasPermission("tardis.bigger")) {
                            schm = Constants.SCHEMATIC.BIGGER;
                        } else {
                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You don't have permission to create a 'bigger' TARDIS!");
                            return;
                        }
                        break;
                    case DIAMOND_BLOCK:
                        if (player.hasPermission("tardis.deluxe")) {
                            schm = Constants.SCHEMATIC.DELUXE;
                        } else {
                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You don't have permission to create a 'deluxe' TARDIS!");
                            return;
                        }
                        break;
                    default:
                        schm = Constants.SCHEMATIC.BUDGET;
                        break;
                }
                if (player.hasPermission("tardis.create")) {
                    String playerNameStr = player.getName();
                    // check to see if they already have a TARDIS
                    Statement statement = null;
                    PreparedStatement pstatement = null;
                    ResultSet rs = null;
                    try {
                        Connection connection = service.getConnection();
                        statement = connection.createStatement();
                        // check if the chunk already contains a TARDIS
                        String queryTardis = "SELECT * FROM tardis WHERE owner = ?";
                        pstatement = connection.prepareStatement(queryTardis);
                        pstatement.setString(1, playerNameStr);
                        rs = pstatement.executeQuery();
                        if (!rs.next()) {
                            // get this chunk co-ords
                            Chunk chunk = blockBottom.getChunk();
                            String cw;
                            World chunkworld;
                            // check config to see whether we are using a default world to store TARDII
                            if (plugin.config.getBoolean("default_world") == Boolean.valueOf("true")) {
                                cw = plugin.config.getString("default_world_name");
                                chunkworld = plugin.getServer().getWorld(cw);
                            } else {
                                chunkworld = chunk.getWorld();
                                cw = chunkworld.getName();
                            }
                            int cx = chunk.getX();
                            int cz = chunk.getZ();
                            if (!plugin.utils.checkChunk(cw, cx, cz, schm)) {
                                // get player direction
                                float pyaw = player.getLocation().getYaw();
                                if (pyaw >= 0) {
                                    pyaw = (pyaw % 360);
                                } else {
                                    pyaw = (360 + (pyaw % 360));
                                }
                                Location block_loc = blockBottom.getLocation();
                                // determine direction player is facing
                                String d = "";
                                if (pyaw >= 315 || pyaw < 45) {
                                    d = "SOUTH";
                                }
                                if (pyaw >= 225 && pyaw < 315) {
                                    d = "EAST";
                                }
                                if (pyaw >= 135 && pyaw < 225) {
                                    d = "NORTH";
                                }
                                if (pyaw >= 45 && pyaw < 135) {
                                    d = "WEST";
                                }
                                // save data to database (tardis table)
                                int lastInsertId = 0;
                                String chun = cw + ":" + cx + ":" + cz;
                                String home = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ();
                                String save = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ();
                                String queryInsert = "INSERT INTO tardis (owner,chunk,direction,home,save,size) VALUES ('" + playerNameStr + "','" + chun + "','" + d + "','" + home + "','" + save + "','" + schm.name() + "')";
                                statement.executeUpdate(queryInsert);
                                ResultSet idRS = statement.getGeneratedKeys();
                                if (idRS.next()) {
                                    lastInsertId = idRS.getInt(1);
                                }
                                idRS.close();
                                // remove redstone torch
                                block.setTypeId(0);
                                // turn the block stack into a TARDIS
                                plugin.builder.buildOuterTARDIS(lastInsertId, block_loc, Constants.COMPASS.valueOf(d), false, player, false);
                                plugin.builder.buildInnerTARDIS(schm, chunkworld, Constants.COMPASS.valueOf(d), lastInsertId, player, middle_id, middle_data);
                            } else {
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " A TARDIS already exists at this location, please try another chunk!");
                            }
                        } else {
                            String leftLoc = rs.getString("save");
                            String[] leftData = leftLoc.split(":");
                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You already have a TARDIS, you left it in " + leftData[0] + " at x:" + leftData[1] + " y:" + leftData[2] + " z:" + leftData[3]);
                        }
                    } catch (SQLException e) {
                        System.err.println(Constants.MY_PLUGIN_NAME + " Block Place Listener Error: " + e + ", " + e.getErrorCode() + ", " + e.getSQLState());
                    } finally {
                        if (pstatement != null) {
                            try {
                                pstatement.close();
                            } catch (Exception e) {
                            }
                        }
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (Exception e) {
                            }
                        }
                        if (statement != null) {
                            try {
                                statement.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You don't have permission to build a TARDIS!");
                }
            }
        }
    }
}
