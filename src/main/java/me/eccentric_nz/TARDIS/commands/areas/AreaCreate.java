package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.regex.Pattern;

public class AreaCreate {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_]{2,16}");

    public void make(TARDIS plugin, String name, Player player) {
        // check name is unique and acceptable
        if (!LETTERS_NUMBERS.matcher(name).matches()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NAME_NOT_VALID");
            return;
        }
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        if (rsa.resultSet()) {
            for (String s : rsa.getNames()) {
                if (s.equals(name)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_IN_USE");
                    return;
                }
            }
        }
        // add new area without min/max, grid = 0
        HashMap<String, Object> create = new HashMap<>();
        create.put("area_name", name);
        create.put("world", player.getLocation().getWorld().getName());
        create.put("grid", 0);
        plugin.getQueryFactory().doInsert("areas", create);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_SAVED", name);
    }
}
