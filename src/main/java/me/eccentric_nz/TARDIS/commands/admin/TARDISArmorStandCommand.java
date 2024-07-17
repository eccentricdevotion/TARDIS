package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.HashMap;

public class TARDISArmorStandCommand {
    private final TARDIS plugin;

    public TARDISArmorStandCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean checkAndRemove(CommandSender sender) {
        for (World world : plugin.getServer().getWorlds()) {
            int i = 0;
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
                            plugin.getMessenger().message(sender, TardisModule.TARDIS, "Found a valid TARDIS, leaving as is!");
                            continue;
                        }
                        stand.remove();
                        i++;
                    }
                }
            }
            if (i > 0) {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "Removed " + i + " armour stands in " + world.getName());
            }
        }
        return true;
    }
}
