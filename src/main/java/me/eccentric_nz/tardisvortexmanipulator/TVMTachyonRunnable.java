/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetTachyon;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TVMTachyonRunnable implements Runnable {

    private final TARDIS plugin;
    private final int recharge;
    private final int max;
    private final TVMQueryFactory qf;

    public TVMTachyonRunnable(TARDIS plugin) {
        this.plugin = plugin;
        recharge = this.plugin.getVortexConfig().getInt("tachyon_use.recharge");
        max = this.plugin.getVortexConfig().getInt("tachyon_use.max");
        qf = new TVMQueryFactory(this.plugin);
    }

    @Override
    public void run() {
        // get Vortex manipulators
        TVMResultSetTachyon rs = new TVMResultSetTachyon(plugin);
        if (rs.resultSet()) {
            rs.getMaipulators().forEach((t) -> {
                // player must be online to recharge
                Player p = plugin.getServer().getPlayer(t.uuid());
                if (p != null && p.isOnline()) {
                    // check their tachyon level
                    if (t.level() + recharge <= max) {
                        // recharge them if they are not full
                        qf.alterTachyons(t.uuid().toString(), recharge);
                    } else {
                        // catch slightly off levels ie 98%
                        qf.alterTachyons(t.uuid().toString(), max - t.level());
                    }
                }
            });
        }
    }
}
