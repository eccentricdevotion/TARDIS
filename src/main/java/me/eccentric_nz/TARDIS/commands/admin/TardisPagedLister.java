package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TardisPagedLister {

    private final TARDIS plugin;
    private final CommandSender sender;

    public TardisPagedLister(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    public void send(int page) {
        // get all tardis positions - max 18
        int start = (page * 18) - 18;
        int end = page * 18;
        String limit = start + ", " + end;
        HashMap<String, Object> where = new HashMap<>();
        where.put("abandoned", 0);
        ResultSetTardis rsl = new ResultSetTardis(plugin, where, limit, true);
        if (rsl.resultSet()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_LOCS");
            if (sender instanceof Player) {
                plugin.getMessenger().message(sender, "Hover to see location (world x, y, z)");
                plugin.getMessenger().message(sender, "Click to enter the TARDIS");
            }
            plugin.getMessenger().message(sender, "");
            for (Tardis tardis : rsl.getData()) {
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, tardis.getTardisId());
                if (!rsc.resultSet()) {
                    plugin.getMessenger().message(
                            sender,
                            TardisModule.TARDIS,
                            Component.text(tardis.getTardisId() + " ", NamedTextColor.GREEN)
                                    .append(Component.text(tardis.getOwner() + " TARDIS is in an unloaded world!", NamedTextColor.WHITE))
                    );
                    continue;
                }
                Current current = rsc.getCurrent();
                String world = (!plugin.getPlanetsConfig().getBoolean("planets." + current.location().getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(current.location().getWorld()) : TARDISAliasResolver.getWorldAlias(current.location().getWorld());
                plugin.getMessenger().sendTARDISForList(sender, tardis, world, current.location().getBlockX(), current.location().getBlockY(), current.location().getBlockZ());
            }
            if (rsl.getData().size() > 18) {
                plugin.getMessenger().sendColouredCommand(sender, "TARDIS_LOCS_INFO", "/tardisadmin list 2", plugin);
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_LOCS_NONE");
        }
    }
}
