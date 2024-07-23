package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChunkContainsTARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class TARDISCleanEntitiesCommand {
    private final TARDIS plugin;

    public TARDISCleanEntitiesCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean checkAndRemove(CommandSender sender) {
        for (World world : plugin.getServer().getWorlds()) {
            int stands = 0;
            int interactions = 0;
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand stand) {
                    if (stand.isInvisible() && stand.isInvulnerable()) {
                        // check it has no TARDIS record
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("world", world.getName());
                        where.put("x", stand.getLocation().getBlockX());
                        where.put("y", stand.getLocation().getBlockY());
                        where.put("z", stand.getLocation().getBlockZ());
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
                        if (rsc.resultSet()) {
                            plugin.getMessenger().message(sender, TardisModule.TARDIS, "Found a valid TARDIS armour stand, leaving as is!");
                            continue;
                        }
                        stand.remove();
                        stands++;
                    }
                }
                if (entity instanceof Interaction interaction) {
                    if (interaction.getPersistentDataContainer().has(plugin.getTardisIdKey(), PersistentDataType.INTEGER)) {
                        // check it has no TARDIS record
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("world", world.getName());
                        where.put("x", interaction.getLocation().getBlockX());
                        where.put("y", interaction.getLocation().getBlockY());
                        where.put("z", interaction.getLocation().getBlockZ());
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
                        if (rsc.resultSet()) {
                            plugin.getMessenger().message(sender, TardisModule.TARDIS, "Found a valid TARDIS interaction, leaving as is!");
                            continue;
                        }
                        interaction.remove();
                        interactions++;
                    }
                }
            }
            // set chunks to be not force loaded
            int forced = 0;
            for (Chunk chunk : world.getForceLoadedChunks()) {
//                if (chunk.getPluginChunkTickets().contains(plugin)) {
                // check if a tardis is in this chunk
                ResultSetChunkContainsTARDIS rsc = new ResultSetChunkContainsTARDIS(plugin, world.getName(), chunk.getX() * 16, chunk.getZ() * 16);
                if (!rsc.resultSet()) {
                    chunk.removePluginChunkTicket(plugin);
                    forced++;
//                    }
                }
            }
            if (stands > 0) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "Removed " + stands + " armour stands in " + world.getName());
            }
            if (interactions > 0) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "Removed " + interactions + " interactions in " + world.getName());
            }
            if (forced > 0) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "Removed " + forced + " force loaded chunks in " + world.getName());
            }
        }
        return true;
    }
}
