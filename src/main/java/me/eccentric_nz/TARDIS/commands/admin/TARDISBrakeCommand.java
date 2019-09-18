package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHandbrakeCommand;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISBrakeCommand {

    private final TARDIS plugin;

    public TARDISBrakeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(CommandSender sender, String[] args) {
        if (args.length < 3) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        Player player = plugin.getServer().getPlayer(args[2]);
        if (player == null) {
            TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
            return true;
        }
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            return new TARDISHandbrakeCommand(plugin).toggle(player, rs.getTardis_id(), args, true);
        }
        return true;
    }
}
