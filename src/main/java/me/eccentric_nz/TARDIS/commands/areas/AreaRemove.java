package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AreaRemove {

    public void delete(TARDIS plugin, String name, Player player) {
        HashMap<String, Object> wherer = new HashMap<>();
        wherer.put("area_name", name);
        plugin.getQueryFactory().doDelete("areas", wherer);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_DELETE", name);
    }
}
