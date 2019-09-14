package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISHandlesRemoveCommand {

    private final TARDIS plugin;

    public TARDISHandlesRemoveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean purge(Player player) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", rs.getTardis_id());
            whereh.put("type", 26);
            new QueryFactory(plugin).doDelete("controls", whereh);
            TARDISMessage.send(player, "HANDLES_DELETED");
        } else {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
        }
        return true;
    }
}
