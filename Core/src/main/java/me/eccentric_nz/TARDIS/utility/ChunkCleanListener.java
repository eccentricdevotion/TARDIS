package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class ChunkCleanListener implements Listener {

    private final TARDIS plugin;

    public ChunkCleanListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.getWorld().getName().contains("TARDIS")) {
            return;
        }
        if (!plugin.getConfig().getBoolean("preferences.clean")) {
            return;
        }
        for (Entity entity : event.getChunk().getEntities()) {
            int i = 0;
            int j = 0;
            if (entity instanceof ArmorStand stand) {
                if (stand.isInvisible() && stand.isInvulnerable()) {
                    // check it has no TARDIS record
                    if (checkLocation(stand)) {
                        continue;
                    }
                    stand.remove();
                    i++;
                }
            }
            if (entity instanceof Interaction interaction) {
                if (interaction.getPersistentDataContainer().has(plugin.getTardisIdKey(), PersistentDataType.INTEGER)) {
                    // check it has no TARDIS record
                    if (checkLocation(interaction)) {
                        continue;
                    }
                    interaction.remove();
                    j++;
                }
            }
            if (i > 0) {
                plugin.debug("Removed " + i + " armour stands in " + event.getWorld().getName());
            }
            if (j > 0) {
                plugin.debug("Removed " + j + " interactions in " + event.getWorld().getName());
            }
        }
    }

    private boolean checkLocation(Entity entity) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("world", entity.getWorld().getName());
        where.put("x", entity.getLocation().getBlockX());
        where.put("y", entity.getLocation().getBlockY());
        where.put("z", entity.getLocation().getBlockZ());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        return rsc.resultSet();
    }
}