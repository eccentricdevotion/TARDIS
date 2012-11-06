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

    private TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

    public TARDISBlockDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String queryBlock = "SELECT location FROM blocks WHERE location = '" + l + "'";
            ResultSet rsBlockLoc = statement.executeQuery(queryBlock);
            if (rsBlockLoc.next()) {
                event.setCancelled(true);
                p.sendMessage(Constants.MY_PLUGIN_NAME + " You cannot break the TARDIS blocks!");
            }
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Could not save block location to DB!");
        }
    }
}
