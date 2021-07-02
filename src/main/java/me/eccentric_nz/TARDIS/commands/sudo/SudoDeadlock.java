package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

class SudoDeadlock {

    private final TARDIS plugin;

    SudoDeadlock(TARDIS plugin) {
        this.plugin = plugin;
    }

    void toggleDeadlock(UUID uuid, CommandSender sender) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        // does the player have a TARDIS
        if (rs.fromUUID(uuid.toString())) {
            int id = rs.getTardis_id();
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            ResultSetDoors rsd = new ResultSetDoors(plugin, wheret, false);
            if (rsd.resultSet()) {
                int lock = rsd.isLocked() ? 0 : 1;
                String lockedUnlocked = rsd.isLocked() ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                HashMap<String, Object> setd = new HashMap<>();
                setd.put("locked", lock);
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("doors", setd, whered);
                TARDISMessage.send(sender, "DOOR_LOCK", lockedUnlocked);
            }
        } else {
            TARDISMessage.send(sender, "NO_TARDIS");
        }
    }
}
