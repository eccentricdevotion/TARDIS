package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSystemUpgrades;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;

import java.util.HashMap;

public class SystemUpgradeRecord {

    private final TARDIS plugin;

    public SystemUpgradeRecord(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void make(String uuid) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(uuid)) {
            return;
        }
        // get player's artron energy level
        ResultSetSystemUpgrades rsp = new ResultSetSystemUpgrades(plugin, rs.getTardisId(), uuid);
        if (!rsp.resultset()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", rs.getTardisId());
            set.put("uuid", uuid);
            plugin.getQueryFactory().doInsert("system_upgrades", set);
        }
    }
}
