package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class TARDISWitherDragonListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISWitherDragonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void bossBlockBreak(EntityChangeBlockEvent event) {
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        EntityType eType;
        try {
            eType = event.getEntityType();
        } catch (Exception e) {
            eType = null;
        }
        if ((eType != null) && (eType == EntityType.WITHER || eType == EntityType.ENDER_DRAGON)) {
            Statement statement = null;
            ResultSet rsBlockLoc = null;
            try {
                Connection connection = service.getConnection();
                statement = connection.createStatement();
                String queryBlock = "SELECT location FROM blocks WHERE location = '" + l + "'";
                rsBlockLoc = statement.executeQuery(queryBlock);
                if (rsBlockLoc.next()) {
                    event.setCancelled(true);
                }
            } catch (SQLException e) {
                plugin.console.sendMessage(plugin.pluginName + " Could not get block locations from DB!");
            } finally {
                try {
                    rsBlockLoc.close();
                } catch (Exception e) {
                }
                try {
                    statement.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
