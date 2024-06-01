package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSystemUpgrades;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;

public class SystemUpgradeChecker {

    private final TARDIS plugin;

    public SystemUpgradeChecker(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean has(String uuid, SystemTree upgrade) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(uuid)) {
            return false;
        }
        // get player's artron energy level
        ResultSetSystemUpgrades rsp = new ResultSetSystemUpgrades(plugin, rs.getTardisId(), uuid);
        if (!rsp.resultset()) {
            return false;
        }
        return rsp.getData().getUpgrades().get(upgrade);
    }
}
