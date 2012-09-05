package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TARDISBlockBreakListener implements Listener {

    private TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

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
            boolean isTimelord = false;
            if (line1.equals("¤fPOLICE") && line2.equals("¤fBOX")) {
                String queryCheck = "SELECT * FROM tardis WHERE owner = '" + playerNameStr + "'";
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(queryCheck);
                    if (rs != null && rs.next()) {
                        String saveLoc = rs.getString("save");
                        String chunkLoc = rs.getString("chunk");
                        int id = rs.getInt("tardis_id");
                        Constants.COMPASS d = Constants.COMPASS.valueOf(rs.getString("direction"));

                        // check the sign location
                        Location sign_loc = block.getLocation();
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
                            TARDISDestroyer destroyer = new TARDISDestroyer(plugin);
                            // clear the torch
                            destroyer.destroyTorch(bb_loc);
                            destroyer.destroySign(bb_loc, d);
                            // also remove the location of the chunk from chunks table
                            String[] chunkworld = chunkLoc.split(":");
                            World cw = plugin.getServer().getWorld(chunkworld[0]);
                            String queryDeleteChunk = "DELETE FROM chunks WHERE world = '" + chunkworld[0] + "' AND x = " + chunkworld[1] + " AND z = " + chunkworld[2];
                            statement.executeUpdate(queryDeleteChunk);
                            destroyer.destroyTARDIS(id, cw, d, 1);
                            if (cw.getWorldType() == WorldType.FLAT) {
                                // replace stone blocks with AIR
                                destroyer.destroyTARDIS(id, cw, d, 0);
                            }
                            destroyer.destroyBlueBox(bb_loc, d, id);
                            // remove record from tardis table
                            String queryDeleteTardis = "DELETE FROM tardis WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDeleteTardis);
                            // remove doors from doors table
                            String queryDeleteDoors = "DELETE FROM doors WHERE tardis_id = " + id;
                            statement.executeUpdate(queryDeleteDoors);
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
