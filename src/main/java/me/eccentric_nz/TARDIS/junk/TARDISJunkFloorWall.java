/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkFloorWall {

    private final TARDIS plugin;

    public TARDISJunkFloorWall(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setJunkWallOrFloor(CommandSender sender, String[] args) {
        String pref = args[0].toLowerCase(Locale.ENGLISH);
        // check args
        if (args.length < 2) {
            TARDISMessage.send(sender, "PREF_WALL", pref);
            return false;
        }
        String wall_mat;
        if (args.length > 2) {
            int count = args.length;
            StringBuilder buf = new StringBuilder();
            for (int i = 1; i < count; i++) {
                buf.append(args[i]).append("_");
            }
            String tmp = buf.toString();
            String t = tmp.substring(0, tmp.length() - 1);
            wall_mat = t.toUpperCase(Locale.ENGLISH);
        } else {
            wall_mat = args[1].toUpperCase(Locale.ENGLISH);
        }
        if (!plugin.getTardisWalls().blocks.containsKey(wall_mat)) {
            String message = (wall_mat.equals("HELP")) ? "WALL_LIST" : "WALL_NOT_VALID";
            TARDISMessage.send(sender, message, pref);
            List<String> sortedKeys = new ArrayList<>(plugin.getTardisWalls().blocks.keySet());
            Collections.sort(sortedKeys);
            sortedKeys.forEach((w) -> {
                sender.sendMessage(w);
            });
            return true;
        }
        final QueryFactory qf = new QueryFactory(plugin);
        // check if player_prefs record
        HashMap<String, Object> wherepp = new HashMap<>();
        wherepp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        HashMap<String, Object> setpp = new HashMap<>();
        if (!rsp.resultSet()) {
            String otherpref = (pref.equals("floor")) ? "wall" : "floor";
            String otherset = (pref.equals("floor")) ? "ORANGE_WOOL" : "GREY_WOOL";
            // create a player_prefs record
            setpp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            setpp.put("player", "junk");
            setpp.put(pref, wall_mat);
            setpp.put(otherpref, otherset);
            qf.doInsert("player_prefs", setpp);
        } else {
            setpp.put(pref, wall_mat);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            qf.doUpdate("player_prefs", setpp, where);
        }
        TARDISMessage.send(sender, "PREF_MAT_SET", TARDISPrefsCommands.ucfirst(pref));
        return true;
    }
}
