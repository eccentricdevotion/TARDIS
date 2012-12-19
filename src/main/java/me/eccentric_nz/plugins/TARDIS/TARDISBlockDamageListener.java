package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class TARDISBlockDamageListener implements Listener {

    TARDISDatabase service = TARDISDatabase.getInstance();

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        Statement statement = null;
        ResultSet rsBlockLoc = null;
        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();
            String queryBlock = "SELECT location FROM blocks WHERE location = '" + l + "'";
            rsBlockLoc = statement.executeQuery(queryBlock);
            if (rsBlockLoc.next()) {
                event.setCancelled(true);
                p.sendMessage(Constants.MY_PLUGIN_NAME + " You cannot break the TARDIS blocks!");
            }
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Could not get block damage locations from DB!");
        } finally {
            if (rsBlockLoc != null) {
                try {
                    rsBlockLoc.close();
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
    }
}
