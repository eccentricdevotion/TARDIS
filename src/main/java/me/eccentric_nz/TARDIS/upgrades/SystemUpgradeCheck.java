package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;

import java.util.HashMap;

public class SystemUpgradeCheck {

    private final TARDIS plugin;

    public SystemUpgradeCheck(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void makeRecord(String uuid) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(uuid)) {
            return;
        }
        // get player's artron energy level
        ResultSetPlayerArtronLevel rsp = new ResultSetPlayerArtronLevel(plugin, rs.getTardisId(), uuid);
        if (!rsp.resultset()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", rs.getTardisId());
            set.put("uuid", uuid);
            plugin.getQueryFactory().doInsert("system_upgrades", set);
        }
    }
}
