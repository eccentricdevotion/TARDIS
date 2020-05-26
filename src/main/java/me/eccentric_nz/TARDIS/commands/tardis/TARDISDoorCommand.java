package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.move.TARDISDoorToggler;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

public class TARDISDoorCommand {

    private final TARDIS plugin;

    public TARDISDoorCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggleDoors(Player player, String[] args) {
        if (!player.hasPermission("tardis.use")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        // must have a TARDIS
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return false;
        }
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        boolean open = (args[1].equalsIgnoreCase("close"));
        // toggle the door
        new TARDISDoorToggler(plugin, player.getLocation().getBlock(), player, false, open, rs.getTardis_id()).toggleDoors();
        return true;
    }
}
