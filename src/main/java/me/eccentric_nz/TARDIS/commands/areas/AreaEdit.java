package me.eccentric_nz.TARDIS.commands.areas;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.areas.EditAreasInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AreaEdit {

    public void open(TARDIS plugin, String name, Player player) {
        // get area_id of specified area
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", name);
        ResultSetAreas rsaId = new ResultSetAreas(plugin, wherea, false, false);
        if (!rsaId.resultSet()) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        if (rsaId.getArea().grid()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NOT_GRID");
            return;
        }
        // open edit gui to allow removal of added locations
        player.openInventory(new EditAreasInventory(plugin, rsaId.getArea().areaId()).getInventory());
    }
}
