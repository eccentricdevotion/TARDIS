package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class AreaStart {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_]{2,16}");

    public void track(TARDIS plugin, String name, Player player) {
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
        plugin.getTrackerKeeper().getArea().put(player.getUniqueId(), name);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_CLICK_START");
    }
}
