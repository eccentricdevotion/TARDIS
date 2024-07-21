package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
            int i = 0;
            int j = 0;
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
                        i++;
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
                        j++;
                    }
                }
            }
            if (i > 0) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "Removed " + i + " armour stands in " + world.getName());
            }
            if (j > 0) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "Removed " + j + " interactions in " + world.getName());
            }
        }
        return true;
    }
}
