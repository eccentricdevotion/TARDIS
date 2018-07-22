package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

class TARDISTransmatCommand {

    private final TARDIS plugin;

    public TARDISTransmatCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setLocation(Player player, String[] args) {
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        if (!player.hasPermission("tardis.transmat")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        // must be a time lord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        // player is in TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TARDISMessage.send(player, "NOT_IN_TARDIS");
            return false;
        }
        int id = rs.getTardis_id();
        int thisid = rst.getTardis_id();
        if (thisid != id) {
            TARDISMessage.send(player, "CMD_ONLY_TL");
            return false;
        }
        Location location = player.getLocation();
        // must be in their TARDIS
        if (!plugin.getUtils().inTARDISWorld(location)) {
            TARDISMessage.send(player, "CMD_IN_WORLD");
            return true;
        }
        // get the transmat name
        if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
            TARDISMessage.send(player, "SAVE_NAME_NOT_VALID");
            return true;
        }
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", location.getWorld().getName());
        set.put("x", location.getX());
        set.put("y", location.getY());
        set.put("z", location.getZ());
        set.put("yaw", location.getYaw());
        // check if transmat name exists
        ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[1]);
        if (rsm.resultSet()) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("name", args[1]);
            qf.doUpdate("transmat", set, where);
        } else {
            set.put("tardis_id", id);
            set.put("name", args[1]);
            qf.doInsert("transmats", set);
        }
        TARDISMessage.send(player, "TRANSMAT_SAVED");
        return true;
    }
}
